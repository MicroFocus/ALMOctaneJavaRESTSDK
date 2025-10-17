/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.examples.oauth2;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.OAuth2Authentication;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OAuth2OctaneHttpClientExample {

    // Configuration constants (replace with your environment values)

    private static final String IDP_TOKEN_ENDPOINT = "";

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    private static final String OCTANE_SERVER = "";

    private static final long SHARED_SPACE_ID = 1001L;
    private static final long WORKSPACE_ID = 1002L;

    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    /**
     * Calls the token endpoint with client_credentials using form-encoded body and Basic Auth.
     */
    public static String requestClientCredentialsResponse(
            String tokenEndpoint,
            String clientId,
            String clientSecret
    ) throws IOException {
        HttpRequestFactory factory = TRANSPORT.createRequestFactory();

        // grant_type=client_credentials
        Map<String, String> form = new HashMap<>();
        form.put("grant_type", "client_credentials");
        HttpContent content = new UrlEncodedContent(form);

        GenericUrl url = new GenericUrl(tokenEndpoint);
        HttpRequest req = factory.buildPostRequest(url, content);

        // Add Basic auth header
        String basic = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = req.getHeaders();
        headers.setAuthorization("Basic " + basic);
        headers.setAccept("application/json");
        headers.setContentType("application/x-www-form-urlencoded");

        req.setConnectTimeout(15_000);
        req.setReadTimeout(30_000);

        HttpResponse resp = req.execute();
        String body = resp.parseAsString();
        if (!resp.isSuccessStatusCode()) {
            throw new IOException("Token request failed: " + resp.getStatusCode() + " " + resp.getStatusMessage()
                    + " | body: " + body);
        }
        return body;
    }

    /** Returns only the access_token extracted from JSON body. */
    public static String requestClientCredentialsAccessToken(
            String tokenEndpoint,
            String clientId,
            String clientSecret
    ) throws IOException {
        String body = requestClientCredentialsResponse(tokenEndpoint, clientId, clientSecret);
        return extractAccessToken(body);
    }

    /** Regex extractor for "access_token":"...". */
    public static String extractAccessToken(String jsonBody) {
        Pattern p = Pattern.compile("\"access_token\"\\s*:\\s*\"([^\"]+)\"");
        Matcher m = p.matcher(jsonBody);
        return m.find() ? m.group(1) : null;
    }

    public static void main(String[] args) {
        // configureSSLToTrustAll(); // for dev only
        new OAuth2OctaneHttpClientExample().createContext();
    }

    public void createContext() {
        String idpAccessToken;
        try {
            // Get IDP token using constants above
            idpAccessToken = requestClientCredentialsAccessToken(IDP_TOKEN_ENDPOINT, CLIENT_ID, CLIENT_SECRET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (idpAccessToken == null) {
            System.out.println("Failed to get access token from IDP");
            return;
        }

        // Build Octane client using obtained token
        Authentication clientAuthentication = new OAuth2Authentication(idpAccessToken, "tx", "txpass");
        final Octane.Builder octaneBuilder = new Octane.Builder(clientAuthentication);

        octaneBuilder.Server(OCTANE_SERVER);
        octaneBuilder.sharedSpace(SHARED_SPACE_ID);
        octaneBuilder.workSpace(WORKSPACE_ID);
        octaneBuilder.isHttp2(true);

        Octane octane = octaneBuilder.build();

        // Fetch and print some entities
        octane.entityList("defects").get().addFields("name").execute().forEach(System.out::println);
        octane.entityList("features").get().addFields("name").execute().forEach(System.out::println);
    }

    /**
     * Globally disables SSL validation (use only in development).
     */
    private static void configureSSLToTrustAll() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to configure SSL", e);
        }
    }
}

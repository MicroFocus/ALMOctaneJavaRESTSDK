/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.network.google;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.hpe.adm.nga.sdk.OctaneWrapper;
import com.hpe.adm.nga.sdk.network.Octane304Exception;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * HTTP Client using Google's API
 * <p>This will be refactored in future releases to enable the use of different underlying APIs</p>
 */
public class GoogleHttpClient extends OctaneHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(GoogleHttpClient.class.getName());

    private static final String LOGGER_REQUEST_FORMAT = "Request: {} - {} - {}";
    private static final String LOGGER_RESPONSE_FORMAT = "Response: {} - {} - {}";

    private static final String SET_COOKIE = "set-cookie";
    private static final String HTTP_MEDIA_TYPE_MULTIPART_NAME = "multipart/form-data";
    private static final String HTTP_MULTIPART_BOUNDARY_NAME = "boundary";
    private static final String HTTP_MULTIPART_BOUNDARY_VALUE = "---------------------------92348603315617859231724135434";
    private static final String HTTP_MULTIPART_PART_DISPOSITION_NAME = "Content-Disposition";
    private static final String HTTP_MULTIPART_PART1_DISPOSITION_FORMAT = "form-data; name=\"%s\"; filename=\"blob\"";
    private static final String HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE = "entity";
    private static final String HTTP_MULTIPART_PART2_DISPOSITION_FORMAT = "form-data; name=\"content\"; filename=\"%s\"";

    protected HttpRequestFactory requestFactory;
    protected String lwssoValue = "";
    protected String octaneUserValue;
    protected final String urlDomain;

    // identity operation by default. do nothing
    private Consumer<HttpRequest> customRequestInitializer = request -> {
    };

    private Consumer<HttpRequest> basicAuthenticationRequestInitializer = request -> {
    };

    private final Consumer<HttpRequest> defaultRequestInitializer = request -> {
        request.setResponseInterceptor(response -> {
            // retrieve new LWSSO in response if any
            HttpHeaders responseHeaders = response.getHeaders();
            updateLWSSOCookieValue(responseHeaders);
        });

        request.setUnsuccessfulResponseHandler((httpRequest, httpResponse, b) -> false);

        final StringBuilder cookieBuilder = new StringBuilder();
        if (lwssoValue != null && !lwssoValue.isEmpty()) {
            cookieBuilder.append(LWSSO_COOKIE_KEY).append("=").append(lwssoValue);
        }
        if (octaneUserValue != null && !octaneUserValue.isEmpty()) {
            cookieBuilder.append(";").append(OCTANE_USER_COOKIE_KEY).append("=").append(octaneUserValue);
        }

        request.getHeaders().setCookie(cookieBuilder.toString());

        request.setReadTimeout(60000);
    };

    /**
     * Request initializer called on every request made by the requestFactory
     */
    protected HttpRequestInitializer requestInitializer = request -> defaultRequestInitializer
            .andThen(customRequestInitializer)
            .andThen(basicAuthenticationRequestInitializer)
            .accept(request);

    public GoogleHttpClient(final String urlDomain) {
        this(urlDomain, (com.hpe.adm.nga.sdk.authentication.BasicAuthentication) null);
    }

    public GoogleHttpClient(final String urlDomain, com.hpe.adm.nga.sdk.authentication.BasicAuthentication basicAuthentication) {
        this.urlDomain = urlDomain;
        initializeBasicAuthentication(basicAuthentication);
        logSystemProperties();
        requestFactory = buildRequestFactory();
    }

    /**
     * Constructor accepting custom settings for the underlying http transport layer
     *
     * @param urlDomain the Octane domain
     * @param settings  the object containing he custom settings
     * @throws RuntimeException in case of an underlying security exception
     */
    public GoogleHttpClient(final String urlDomain, OctaneWrapper.OctaneCustomSettings settings) throws RuntimeException {
        this(urlDomain, settings, null);
    }

    public GoogleHttpClient(final String urlDomain, OctaneWrapper.OctaneCustomSettings settings
            , com.hpe.adm.nga.sdk.authentication.BasicAuthentication basicAuthentication) throws RuntimeException {
        this.urlDomain = urlDomain;
        initializeBasicAuthentication(basicAuthentication);
        logSystemProperties();

        extractCustomSettings(settings);
    }

    private void initializeBasicAuthentication(com.hpe.adm.nga.sdk.authentication.BasicAuthentication basicAuthentication) {
        if (basicAuthentication != null) {
            basicAuthenticationRequestInitializer = request -> request.getHeaders().setBasicAuthentication(basicAuthentication.getAuthenticationId(),
                    basicAuthentication.getAuthenticationSecret());
        }
    }

    private void extractCustomSettings(OctaneWrapper.OctaneCustomSettings settings) {
        customRequestInitializer = request -> {
            request.setReadTimeout((int) settings.get(OctaneWrapper.OctaneCustomSettings.Setting.READ_TIMEOUT));
            request.setConnectTimeout((int) settings.get(OctaneWrapper.OctaneCustomSettings.Setting.CONNECTION_TIMEOUT));
        };

        HttpTransport httpTransport = (HttpTransport) settings.get(OctaneWrapper.OctaneCustomSettings.Setting.SHARED_HTTP_TRANSPORT);
        boolean trustAllCerts = (boolean) settings.get(OctaneWrapper.OctaneCustomSettings.Setting.TRUST_ALL_CERTS);

        if (httpTransport != null) {
            requestFactory = httpTransport.createRequestFactory(requestInitializer);
        } else {
            requestFactory = trustAllCerts ? buildPermissiveRequestFactory() : buildRequestFactory();
        }
    }

    /**
     * Builds a HttpRequestFactory that accepts self-signed certificates
     *
     * @return an http request factory
     */
    private HttpRequestFactory buildPermissiveRequestFactory() {
        NetHttpTransport.Builder builder = new NetHttpTransport.Builder();

        try {
            builder.doNotValidateCertificate();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("A security exception occurred while disabling certificate validation", e);
        }

        HttpTransport httpTransport = builder.build();
        return httpTransport.createRequestFactory(requestInitializer);
    }

    /**
     * Builds a regular HttpRequestFactory
     *
     * @return an http request factory
     */
    private HttpRequestFactory buildRequestFactory() {
        HttpTransport httpTransport = new NetHttpTransport();
        return httpTransport.createRequestFactory(requestInitializer);
    }

    /**
     * Logs proxy system properties
     */
    private void logSystemProperties() {
        logProxySystemProperties();
        logSystemProxyForUrlDomain(urlDomain);
    }

//    public synchronized void signOut() {
//        GenericUrl genericUrl = new GenericUrl(urlDomain + OAUTH_SIGNOUT_URL);
//        HttpRequest httpRequest = null;
//        try {
//            httpRequest = requestFactory.buildPostRequest(genericUrl, null);
//            HttpResponse response = executeRequest(httpRequest);
//
//            if (response.isSuccessStatusCode()) {
//                HttpHeaders hdr1 = response.getHeaders();
//                updateLWSSOCookieValue(hdr1);
//                lastUsedAuthentication = null;
//            }
//        } catch (Exception e) {
//            throw wrapException(e, httpRequest);
//        }
//    }

    /**
     * Convert the abstract {@link OctaneHttpRequest}Request object to a specific {@link HttpRequest} for the google http client
     *
     * @param octaneHttpRequest input {@link OctaneHttpRequest}
     * @return {@link HttpRequest}
     */
    protected HttpRequest convertOctaneRequestToGoogleHttpRequest(OctaneHttpRequest octaneHttpRequest) {
        final HttpRequest httpRequest;
        try {
            switch (octaneHttpRequest.getOctaneRequestMethod()) {
                case GET: {
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    httpRequest = requestFactory.buildGetRequest(domain);
                    httpRequest.getHeaders().setAccept(((OctaneHttpRequest.GetOctaneHttpRequest) octaneHttpRequest).getAcceptType());
                    final String eTagHeader = requestToEtagMap.get(octaneHttpRequest);
                    if (eTagHeader != null) {
                        httpRequest.getHeaders().setETag(eTagHeader);
                    }
                    break;
                }
                case POST: {
                    OctaneHttpRequest.PostOctaneHttpRequest postOctaneHttpRequest = (OctaneHttpRequest.PostOctaneHttpRequest) octaneHttpRequest;
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    httpRequest = requestFactory.buildPostRequest(domain, ByteArrayContent.fromString(null, postOctaneHttpRequest.getContent()));
                    httpRequest.getHeaders().setAccept(postOctaneHttpRequest.getAcceptType());
                    httpRequest.getHeaders().setContentType(postOctaneHttpRequest.getContentType());
                    break;
                }
                case POST_BINARY: {
                    httpRequest = buildBinaryPostRequest((OctaneHttpRequest.PostBinaryOctaneHttpRequest) octaneHttpRequest);
                    break;
                }
                case PUT: {
                    OctaneHttpRequest.PutOctaneHttpRequest putHttpOctaneHttpRequest = (OctaneHttpRequest.PutOctaneHttpRequest) octaneHttpRequest;
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    httpRequest = requestFactory.buildPutRequest(domain, ByteArrayContent.fromString(null, putHttpOctaneHttpRequest.getContent()));
                    httpRequest.getHeaders().setAccept(putHttpOctaneHttpRequest.getAcceptType());
                    httpRequest.getHeaders().setContentType(putHttpOctaneHttpRequest.getContentType());
                    break;
                }
                case DELETE: {
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    httpRequest = requestFactory.buildDeleteRequest(domain);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Request method not known!");
                }
            }

            // Process any custom set headers
            octaneHttpRequest.getHeaders()
                    .forEach(header -> httpRequest.getHeaders().set(header.getHeaderKey(), header.getHeaderValue()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return httpRequest;
    }

    /**
     * Convert google implementation of {@link HttpResponse} to an implementation abstract {@link OctaneHttpResponse}
     *
     * @param httpResponse implementation specific {@link HttpResponse}
     * @return {@link OctaneHttpResponse} created from the impl response object
     */
    protected OctaneHttpResponse convertHttpResponseToOctaneHttpResponse(HttpResponse httpResponse) {
        try {
            // According to the {@link https://tools.ietf.org/html/rfc2616#section-3.7.1} spec the correct encoding should be returned.
            // Currently Octane does not return UTF-8 for the REST API even though that is the encoding.  Manually changing here to fix some encoding issues
            // {@See https://github.com/MicroFocus/ALMOctaneJavaRESTSDK/issues/79}
            final Charset charset = (httpResponse.getContentType().equals("application/json")) ? StandardCharsets.UTF_8 : httpResponse.getContentCharset();
            return new OctaneHttpResponse(httpResponse.getStatusCode(), httpResponse.getContent(), charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method can be used internally to retry the request in case of auth token timeout
     * Careful, this method calls itself recursively to retry the request
     *
     * @param octaneHttpRequest abstract request, has to be converted into a specific implementation of http request
     * @return OctaneHttpResponse
     */
    protected OctaneHttpResponse internalExecute(OctaneHttpRequest octaneHttpRequest) {
        final HttpRequest httpRequest = convertOctaneRequestToGoogleHttpRequest(octaneHttpRequest);
        final HttpResponse httpResponse;

        try {
            httpResponse = executeRequest(httpRequest, octaneHttpRequest.isSensitive());

            final OctaneHttpResponse octaneHttpResponse = convertHttpResponseToOctaneHttpResponse(httpResponse);
            final String eTag = httpResponse.getHeaders().getETag();
            if (eTag != null) {
                requestToEtagMap.put(octaneHttpRequest, eTag);
                cachedRequestToResponse.put(octaneHttpRequest, octaneHttpResponse);
            }
            return octaneHttpResponse;

        } catch (RuntimeException exception) {

            //Return cached response
            if (exception.getCause() instanceof HttpResponseException) {
                HttpResponseException httpResponseException = (HttpResponseException) exception.getCause();

                final int statusCode = httpResponseException.getStatusCode();
                if (statusCode == HttpStatusCodes.STATUS_CODE_NOT_MODIFIED) {
                    throw new Octane304Exception();
                }
            }

            throw exception;
        }
    }

    private HttpResponse executeRequest(final HttpRequest httpRequest, boolean isSensitive) {
        logger.debug(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), httpRequest.getUrl().toString(), httpRequest.getHeaders().toString());

        final HttpContent content = httpRequest.getContent();

        // Make sure you don't log any http content send to the login rest api, since you don't want credentials in the logs
        if (content != null && logger.isDebugEnabled() && !isSensitive) {
            logHttpContent(content);
        }

        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception e) {
            throw wrapException(e, httpRequest);
        }

        logger.debug(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage(), response.getHeaders().toString());
        return response;
    }

    private RuntimeException wrapException(Exception exception, HttpRequest httpRequest) {
        if (exception instanceof HttpResponseException) {

            HttpResponseException httpResponseException = (HttpResponseException) exception;
            logger.debug(LOGGER_RESPONSE_FORMAT, httpResponseException.getStatusCode(), httpResponseException.getStatusMessage(), httpResponseException.getHeaders().toString());

            // It seems that Octane returns a message in 401 but this is swallowed by the HttpConnection as expected by the HTTP spec
            // So the only way to know if this should be re-authenticated is to see if there is a cookie in the request.  If so - we can fake the error and
            // ensure re-authentication
            if (httpResponseException.getStatusCode() == 401) {
                try {
                    final String cookie = httpRequest.getHeaders().getCookie();
                    if (cookie != null) {
                        for (String splitCookie : cookie.split(";")) {
                            if (splitCookie.startsWith(LWSSO_COOKIE_KEY)) {
                                return createUnauthorisedException();
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    // do nothing
                }
            }

            RuntimeException octaneException = createOctaneException(httpResponseException.getStatusMessage(),
                    httpResponseException.getContent(), httpResponseException.getStatusCode());
            if (octaneException != null) {
                return octaneException;
            }
        }

        //In case nothing in exception is parsable
        return new RuntimeException(exception);
    }

    /**
     * Util method to debug log {@link HttpContent}. This method will avoid logging {@link InputStreamContent}, since
     * reading from the stream will probably make it unusable when the actual request is sent
     *
     * @param content {@link HttpContent}
     */
    private static void logHttpContent(HttpContent content) {
        if (content instanceof MultipartContent) {
            MultipartContent multipartContent = ((MultipartContent) content);
            logger.debug("MultipartContent: {}", content.getType());
            multipartContent.getParts().forEach(part -> {
                logger.debug("Part: encoding: {}, headers: {}", part.getEncoding(), part.getHeaders());
                logHttpContent(part.getContent());
            });
        } else if (content instanceof InputStreamContent) {
            logger.debug("InputStreamContent: type: {}", content.getType());
        } else if (content instanceof FileContent) {
            logger.debug("FileContent: type: {}, filepath: {}", content.getType(), ((FileContent) content).getFile().getAbsolutePath());
        } else {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                content.writeTo(byteArrayOutputStream);
                logger.debug("Content: type: {}, {}", content.getType(), byteArrayOutputStream.toString());
            } catch (IOException ex) {
                logger.error("Failed to log content of {} {}", content, ex);
            }
        }
    }

    private HttpRequest buildBinaryPostRequest(OctaneHttpRequest.PostBinaryOctaneHttpRequest octaneHttpRequest) throws IOException {
        GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());

        final HttpRequest httpRequest = requestFactory.buildPostRequest(domain,
                generateMultiPartContent(octaneHttpRequest));
        httpRequest.getHeaders().setAccept(octaneHttpRequest.getAcceptType());

        return httpRequest;
    }

    /**
     * Generates HTTP content based on input parameters and stream.
     *
     * @param octaneHttpRequest - JSON entity model.
     * @return - Generated HTTP content.
     */
    private MultipartContent generateMultiPartContent(OctaneHttpRequest.PostBinaryOctaneHttpRequest octaneHttpRequest) {
        // Add parameters
        MultipartContent content = new MultipartContent()
                .setMediaType(new HttpMediaType(HTTP_MEDIA_TYPE_MULTIPART_NAME)
                        .setParameter(HTTP_MULTIPART_BOUNDARY_NAME, HTTP_MULTIPART_BOUNDARY_VALUE));

        ByteArrayContent byteArrayContent = new ByteArrayContent("application/json", octaneHttpRequest.getContent().getBytes(StandardCharsets.UTF_8));
        MultipartContent.Part part1 = new MultipartContent.Part(byteArrayContent);
        String contentDisposition = String.format(HTTP_MULTIPART_PART1_DISPOSITION_FORMAT, HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE);
        HttpHeaders httpHeaders = new HttpHeaders()
                .set(HTTP_MULTIPART_PART_DISPOSITION_NAME, contentDisposition);

        part1.setHeaders(httpHeaders);
        content.addPart(part1);

        // Add Stream
        InputStreamContent inputStreamContent = new InputStreamContent(octaneHttpRequest.getBinaryContentType(), octaneHttpRequest.getBinaryInputStream());
        MultipartContent.Part part2 = new MultipartContent.Part(inputStreamContent);
        part2.setHeaders(new HttpHeaders().set(HTTP_MULTIPART_PART_DISPOSITION_NAME, String.format(HTTP_MULTIPART_PART2_DISPOSITION_FORMAT, octaneHttpRequest.getBinaryContentName())));
        content.addPart(part2);
        return content;
    }

    /**
     * Retrieve new cookie from set-cookie header
     *
     * @param headers The headers containing the cookie
     * @return true if LWSSO cookie is renewed
     */
    private boolean updateLWSSOCookieValue(HttpHeaders headers) {
        boolean renewed = false;
        List<String> cookieHeaderValue = headers.getHeaderStringValues(SET_COOKIE);
        if (cookieHeaderValue.isEmpty()) {
            return false;
        }

        /* Following code failed to parse set-cookie to get LWSSO cookie due to cookie version, check RFC 2965
        String strCookies = cookieHeaderValue.toString();
        List<HttpCookie> Cookies = java.net.HttpCookie.parse(strCookies.substring(1, strCookies.length()-1));
        lwssoValue = Cookies.stream().filter(a -> a.getName().equals(LWSSO_COOKIE_KEY)).findFirst().get().getValue();*/
        for (String strCookie : cookieHeaderValue) {
            List<HttpCookie> cookies;
            try {
                // Sadly the server seems to send back empty cookies for some reason
                cookies = HttpCookie.parse(strCookie);
            } catch (Exception ex) {
                logger.error("Failed to parse SET_COOKIE header, issue with cookie: \"{}\", {}", strCookie, ex);
                continue;
            }
            Optional<HttpCookie> lwssoCookie = cookies.stream().filter(a -> a.getName().equals(LWSSO_COOKIE_KEY)).findFirst();
            if (lwssoCookie.isPresent()) {
                lwssoValue = lwssoCookie.get().getValue();
                renewed = true;
            } else {
                cookies.stream().filter(cookie -> cookie.getName().equals(OCTANE_USER_COOKIE_KEY)).findAny().ifPresent(cookie -> octaneUserValue = cookie.getValue());
            }
        }

        return renewed;
    }

    /**
     * Log jvm proxy system properties for debugging connection issues
     */
    private static void logProxySystemProperties() {
        if (logger.isDebugEnabled()) {
            String[] proxySysProperties = new String[]{"java.net.useSystemProxies", "http.proxyHost", "http.proxyPort", "https.proxyHost", "https.proxyPort"};
            Arrays.stream(proxySysProperties)
                    .forEach(sysProp -> logger.debug("{}: {}", sysProp, System.getProperty(sysProp)));
        }
    }

    /**
     * Log proxy for octane url domain using system wide {@link ProxySelector}
     *
     * @param urlDomain base url of octane server
     */
    private static void logSystemProxyForUrlDomain(String urlDomain) {
        if (logger.isDebugEnabled()) {
            try {
                List<Proxy> proxies = ProxySelector.getDefault().select(URI.create(urlDomain));
                logger.debug("System proxies for {}: {}", urlDomain, proxies.toString());
            } catch (SecurityException ex) {
                logger.debug("SecurityException when trying to access system wide proxy selector: ", ex);
            }
        }
    }

}
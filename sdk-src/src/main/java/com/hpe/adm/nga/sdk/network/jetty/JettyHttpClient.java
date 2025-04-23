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
package com.hpe.adm.nga.sdk.network.jetty;

import com.google.api.client.http.GenericUrl;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.BasicAuthentication;
import com.hpe.adm.nga.sdk.authentication.JSONAuthentication;
import com.hpe.adm.nga.sdk.authentication.SessionIdAuthentication;
import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.exception.OctanePartialException;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.HttpContent;
import org.eclipse.jetty.client.HttpContentResponse;
import org.eclipse.jetty.client.HttpResponseException;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.util.ByteBufferContentProvider;
import org.eclipse.jetty.client.util.FutureResponseListener;
import org.eclipse.jetty.client.util.MultiPartContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JettyHttpClient implements OctaneHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(JettyHttpClient.class.getName());

    private static final String LOGGER_REQUEST_FORMAT = "Request: {} - {} - {}";
    private static final String LOGGER_RESPONSE_FORMAT = "Response: {} - {} - {}";

    private static final String SET_COOKIE = "set-cookie";
    private static final String HTTP_MULTIPART_BOUNDARY_VALUE = "---------------------------92348603315617859231724135434";
    private static final String HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE = "entity";


    private static final String ERROR_CODE_TOKEN_EXPIRED = "VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT";
    private static final String ERROR_CODE_GLOBAL_TOKEN_EXPIRED = "VALIDATION_TOKEN_EXPIRED_GLOBAL_TIME_OUT";

    private static final int HTTP_REQUEST_RETRY_COUNT = 1;


    private boolean areNewCookiesReceived;
    protected RequestFactory requestFactory;
    protected String lwssoValue = "";
    protected String octaneUserValue;
    protected final String urlDomain;
    protected Authentication lastUsedAuthentication;
    protected Long lastSuccessfulAuthTimestamp;
    private final Map<OctaneHttpRequest, OctaneHttpResponse> cachedRequestToResponse = new HashMap<>();
    private final Map<OctaneHttpRequest, String> requestToEtagMap = new HashMap<>();

    private final ThreadLocal<Long> requestStartTime = new ThreadLocal<>();
    private final Phaser requestPhaser = new Phaser(1);


    public JettyHttpClient(final String urlDomain, final Authentication authentication) {
        this.urlDomain = urlDomain;
        this.lastUsedAuthentication = authentication;

        logSystemProperties();
        org.eclipse.jetty.util.log.Log.setLog(new StdErrLog());

        HttpClient client = new HttpClient(new HttpClientTransportOverHTTP2(new HTTP2Client()), new SslContextFactory.Client());

        addAuthentication(client);
        client.setIdleTimeout(6000);

        try {
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        requestFactory = new RequestFactory(this, client);

    }

    public JettyHttpClient(final String urlDomain, final Authentication authentication, Octane.OctaneCustomSettings settings) throws RuntimeException {
        this.urlDomain = urlDomain;
        this.lastUsedAuthentication = authentication;

        logSystemProperties();
        org.eclipse.jetty.util.log.Log.setLog(new StdErrLog());

        HttpClientTransport httpTransport = (HttpClientTransport) settings.get(Octane.OctaneCustomSettings.Setting.SHARED_HTTP_TRANSPORT);
        boolean trustAllCerts = (boolean) settings.get(Octane.OctaneCustomSettings.Setting.TRUST_ALL_CERTS);
        HttpClient client = httpTransport != null ?
                new HttpClient(httpTransport, new SslContextFactory.Client(trustAllCerts)) :
                new HttpClient(new HttpClientTransportOverHTTP2(new HTTP2Client()), new SslContextFactory.Client(trustAllCerts));

        addAuthentication(client);
        client.setConnectTimeout((int) settings.get(Octane.OctaneCustomSettings.Setting.CONNECTION_TIMEOUT));
        client.setIdleTimeout((int) settings.get(Octane.OctaneCustomSettings.Setting.READ_TIMEOUT));

        try {
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        requestFactory = new RequestFactory(this, client);
    }

    private void addAuthentication(HttpClient client) {

        if (lastUsedAuthentication != null) {
            if (lastUsedAuthentication.isBasicAuthentication()) {
                final BasicAuthentication basicAuthentication = (BasicAuthentication) lastUsedAuthentication;
                client.getAuthenticationStore().addAuthentication(new org.eclipse.jetty.client.util.BasicAuthentication(URI.create(urlDomain),
                        org.eclipse.jetty.client.api.Authentication.ANY_REALM,
                        basicAuthentication.getAuthenticationId(),
                        basicAuthentication.getAuthenticationSecret()));
            }
        }
    }

    @Override
    public boolean authenticate() {
        if (lastUsedAuthentication == null) {
            return false;
        }
        if (lastUsedAuthentication.isBasicAuthentication()) {
            return true;
        }
        if (lastUsedAuthentication.isSessionIdAuthentication()) {
            lwssoValue = ((SessionIdAuthentication) lastUsedAuthentication).getSessionID();
            return true;
        }
        //reset so it's not sent to auth request, server might return 304
        lwssoValue = null;
        octaneUserValue = null;

        final ByteBufferContentProvider content = new ByteBufferContentProvider("application/json",
                ByteBuffer.wrap(((JSONAuthentication) lastUsedAuthentication).getAuthenticationString().getBytes(StandardCharsets.UTF_8)));
        Request httpRequest = requestFactory.buildPostRequest(URI.create(urlDomain + OAUTH_AUTH_URL), content);

        // Authenticate request should never set the api mode header.
        // Newer versions of the Octane server will not accept a private access level HPE_CLIENT_TYPE on the authentication request.
        // Using this kind of header for future requests will still work.
        lastUsedAuthentication.getAPIMode().ifPresent(apiMode ->
                httpRequest.getHeaders().remove(apiMode.getHeaderKey())
        );
        ContentResponse response = executeRequest(httpRequest);
        if (HttpStatus.isSuccess(response.getStatus())) {
            lastSuccessfulAuthTimestamp = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }

    }

    @Override
    public synchronized void signOut() {
        Request httpRequest = null;
        try {
            httpRequest = requestFactory.buildPostRequest(URI.create(urlDomain + OAUTH_SIGNOUT_URL), null);
            ContentResponse response = executeRequest(httpRequest);

            if (HttpStatus.isSuccess(response.getStatus())) {
                updateLWSSOCookieValue(response);
                lastUsedAuthentication = null;
            }
        } catch (Exception e) {
            throw wrapException(e, httpRequest);
        }

    }

    /**
     * Convert the abstract {@link OctaneHttpRequest}Request object to a specific {@link Request} for the google http client
     *
     * @param octaneHttpRequest input {@link OctaneHttpRequest}
     * @return {@link Request}
     */
    protected Request convertOctaneRequestToGoogleHttpRequest(OctaneHttpRequest octaneHttpRequest) {
        final Request httpRequest;
        try {
            switch (octaneHttpRequest.getOctaneRequestMethod()) {
                case GET: {
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    httpRequest = requestFactory.buildGetRequest(domain.toURI());
                    if (((OctaneHttpRequest.GetOctaneHttpRequest) octaneHttpRequest).getAcceptType() != null)
                        httpRequest.getHeaders().add(HttpHeader.ACCEPT, ((OctaneHttpRequest.GetOctaneHttpRequest) octaneHttpRequest).getAcceptType());
                    final String eTagHeader = requestToEtagMap.get(octaneHttpRequest);
                    if (eTagHeader != null) {
                        httpRequest.getHeaders().add(HttpHeader.ETAG, eTagHeader);
                    }
                    break;
                }
                case POST: {
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    OctaneHttpRequest.PostOctaneHttpRequest postOctaneHttpRequest = (OctaneHttpRequest.PostOctaneHttpRequest) octaneHttpRequest;
                    httpRequest = requestFactory.buildPostRequest(domain.toURI(),
                            new ByteBufferContentProvider(postOctaneHttpRequest.getContentType(),
                                    ByteBuffer.wrap(postOctaneHttpRequest.getContent().getBytes(StandardCharsets.UTF_8))));
                    if (postOctaneHttpRequest.getAcceptType() != null)
                        httpRequest.getHeaders().add(HttpHeader.ACCEPT, postOctaneHttpRequest.getAcceptType());
                    break;
                }
                case POST_BINARY: {
                    httpRequest = buildBinaryPostRequest((OctaneHttpRequest.PostBinaryOctaneHttpRequest) octaneHttpRequest);
                    break;
                }
                case POST_BINARY_MULTIPART: {
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    OctaneHttpRequest.PostBinaryBulkOctaneHttpRequest postBinaryBulkOctaneHttpRequest = (OctaneHttpRequest.PostBinaryBulkOctaneHttpRequest) octaneHttpRequest;
                    httpRequest = requestFactory.buildPostRequest(domain.toURI(),
                            generateBinaryBulkPostRequest(postBinaryBulkOctaneHttpRequest));
                    if (postBinaryBulkOctaneHttpRequest.getAcceptType() != null)
                        httpRequest.getHeaders().add(HttpHeader.ACCEPT, postBinaryBulkOctaneHttpRequest.getAcceptType());
                    break;
                }
                case PUT: {
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    OctaneHttpRequest.PutOctaneHttpRequest putHttpOctaneHttpRequest = (OctaneHttpRequest.PutOctaneHttpRequest) octaneHttpRequest;
                    httpRequest = requestFactory.buildPutRequest(domain.toURI(),
                            new ByteBufferContentProvider(putHttpOctaneHttpRequest.getContentType(),
                                    ByteBuffer.wrap(putHttpOctaneHttpRequest.getContent().getBytes(StandardCharsets.UTF_8))));
                    if (putHttpOctaneHttpRequest.getAcceptType() != null)
                        httpRequest.getHeaders().add(HttpHeader.ACCEPT, putHttpOctaneHttpRequest.getAcceptType());
                    break;
                }
                case DELETE: {
                    GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
                    httpRequest = requestFactory.buildDeleteRequest(domain.toURI());
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Request method not known!");
                }
            }

            // Process any custom set headers
            octaneHttpRequest.getHeaders()
                    .forEach(header -> httpRequest.getHeaders().add(header.getHeaderKey(), header.getHeaderValue()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return httpRequest;
    }

    /**
     * Convert google implementation of {@link ContentResponse} to an implementation abstract {@link OctaneHttpResponse}
     *
     * @param httpResponse implementation specific {@link ContentResponse}
     * @return {@link OctaneHttpResponse} created from the impl response object
     */
    protected OctaneHttpResponse convertHttpResponseToOctaneHttpResponse(ContentResponse httpResponse) {
        // According to the {@link https://tools.ietf.org/html/rfc2616#section-3.7.1} spec the correct encoding should be returned.
        // Currently Octane does not return UTF-8 for the REST API even though that is the encoding.  Manually changing here to fix some encoding issues
        // {@See https://github.com/MicroFocus/ALMOctaneJavaRESTSDK/issues/79}
        final Charset charset = (Objects.nonNull(httpResponse.getHeaders().get(HttpHeader.CONTENT_TYPE)) &&
                (httpResponse.getHeaders().get(HttpHeader.CONTENT_TYPE).equals("application/json")) ||
                httpResponse.getHeaders().get(HttpHeader.CONTENT_TYPE).equals("text.csv")) ?
                StandardCharsets.UTF_8 : StandardCharsets.ISO_8859_1;
        return new OctaneHttpResponse(httpResponse.getStatus(), new ByteArrayInputStream(httpResponse.getContent()), charset);
    }

    @Override
    public OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest) {
        return execute(octaneHttpRequest, HTTP_REQUEST_RETRY_COUNT);
    }

    /**
     * This method can be used internally to retry the request in case of auth token timeout
     * Careful, this method calls itself recursively to retry the request
     *
     * @param octaneHttpRequest abstract request, has to be converted into a specific implementation of http request
     * @param retryCount        number of times the method should retry the request if it encounters an HttpResponseException
     * @return OctaneHttpResponse
     */
    private OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest, int retryCount) {

        try {
            final ContentResponse httpResponse;
            try {
                // wait auth or set cookies and register new request
                registerNewRequest();
                final Request httpRequest = convertOctaneRequestToGoogleHttpRequest(octaneHttpRequest);
                httpResponse = executeRequest(httpRequest);
            } finally {
                // unregister current request
                requestPhaser.arriveAndDeregister();
            }
            final OctaneHttpResponse octaneHttpResponse = convertHttpResponseToOctaneHttpResponse(httpResponse);
            final String eTag = httpResponse.getHeaders().get(HttpHeader.ETAG);
            if (eTag != null) {
                requestToEtagMap.put(octaneHttpRequest, eTag);
                cachedRequestToResponse.put(octaneHttpRequest, octaneHttpResponse);
            }
            return octaneHttpResponse;

        } catch (RuntimeException exception) {

            //Return cached response
            if (exception.getCause() instanceof HttpResponseException) {
                HttpResponseException httpResponseException = (HttpResponseException) exception.getCause();
                final int statusCode = httpResponseException.getResponse().getStatus();
                if (statusCode == HttpStatus.NOT_MODIFIED_304) {
                    return cachedRequestToResponse.get(octaneHttpRequest);
                }
            }

            //Handle session timeout exception
            if (retryCount > 0 && exception instanceof OctaneException) {
                OctaneException octaneException = (OctaneException) exception;
                StringFieldModel errorCodeFieldModel = (StringFieldModel) octaneException.getError().getValue("errorCode");
                LongFieldModel httpStatusCode = (LongFieldModel) octaneException.getError().getValue(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME);


                //Handle session timeout
                if (errorCodeFieldModel != null && httpStatusCode.getValue() == 401 &&
                        (ERROR_CODE_TOKEN_EXPIRED.equals(errorCodeFieldModel.getValue()) || ERROR_CODE_GLOBAL_TOKEN_EXPIRED.equals(errorCodeFieldModel.getValue())) &&
                        lastUsedAuthentication != null) {

                    // The same http client should not attempt re-auth from multiple threads
                    synchronized (this) {
                        // If another thread already handled session timeout, skip the re-auth and just retry the request
                        if (lastSuccessfulAuthTimestamp < requestStartTime.get()) {
                            // wait until all current requests are finished before authenticate
                            requestPhaser.arriveAndAwaitAdvance();
                            logger.debug("Auth token expired, trying to re-authenticate");
                            try {
                                authenticate();
                            } catch (OctaneException ex) {
                                logger.debug("Exception while retrying authentication: {}", ex.getMessage());
                            }
                        } else {
                            logger.debug("Auth token expired, but re-authentication was handled by another thread, will not re-authenticate");
                        }
                        logger.debug("Retrying request, retries left: {}", retryCount);
                    }
                    return execute(octaneHttpRequest, --retryCount);
                }
            }

            throw exception;
        }
    }

    private ContentResponse executeRequest(final Request httpRequest) {
        logger.debug(LOGGER_REQUEST_FORMAT, httpRequest.getMethod(), httpRequest.getURI().toString(), httpRequest.getHeaders().stream().collect(Collectors.toList()));

        final ContentProvider content = httpRequest.getContent();

        // Make sure you don't log any http content send to the login rest api, since you don't want credentials in the logs
        if (content != null && logger.isDebugEnabled() && !httpRequest.getURI().toString().contains(OAUTH_AUTH_URL)) {
            logHttpContent(new HttpContent(content));
        }

        ContentResponse response;
        try {
            requestStartTime.set(System.currentTimeMillis());
            if (httpRequest.getPath().contains("metadata")) {
                FutureResponseListener listener = new FutureResponseListener(httpRequest, 10 * 1024 * 1024);
                httpRequest.send(listener);
                response = listener.get(2, TimeUnit.SECONDS);
            } else {
                response = httpRequest.send();
            }
            if (response.getStatus() / 10 == 40)
                throw new Exception(response.getReason(), new HttpResponseException(response.getReason(), response));
        } catch (Exception e) {
            throw wrapException(e, httpRequest);
        }

        logger.debug(LOGGER_RESPONSE_FORMAT, response.getStatus(), response.getReason(), response.getHeaders().stream().collect(Collectors.toList()));
        return response;
    }

    private synchronized void registerNewRequest() {
        if (areNewCookiesReceived) {
            requestPhaser.arriveAndAwaitAdvance();
            areNewCookiesReceived = false;
        }
        requestPhaser.register();
    }

    private class RequestFactory {

        private boolean hasCustom = false;
        private HttpClient client;
        private JettyHttpClient jetty;

        public RequestFactory(JettyHttpClient jetty, HttpClient client) {
            this.client = client;
            this.jetty = jetty;
        }

        public Request buildRequest(Request request, ContentProvider contentProvider) {

            request.onResponseSuccess(response -> jetty.updateLWSSOCookieValue(response));


            if (jetty.lwssoValue != null && !jetty.lwssoValue.isEmpty())
                request.cookie(new HttpCookie(jetty.LWSSO_COOKIE_KEY, jetty.lwssoValue));
            if (jetty.octaneUserValue != null && !jetty.octaneUserValue.isEmpty()) {
                request.cookie(new HttpCookie(jetty.OCTANE_USER_COOKIE_KEY, jetty.octaneUserValue));
            }
            if (jetty.lastUsedAuthentication != null) {
                lastUsedAuthentication.getAPIMode().ifPresent(apiMode -> request.getHeaders().add(apiMode.getHeaderKey(), apiMode.getHeaderValue()));
            }
            request.content(contentProvider);

            return request;

        }

        public Request buildPostRequest(URI uri, ContentProvider contentProvider) {
            return buildRequest(client.newRequest(uri).method(HttpMethod.POST), contentProvider);
        }

        public Request buildPutRequest(URI uri, ContentProvider contentProvider) {
            return buildRequest(client.newRequest(uri).method(HttpMethod.PUT), contentProvider);
        }

        public Request buildGetRequest(URI uri) {
            return buildRequest(client.newRequest(uri).method(HttpMethod.GET), null);
        }

        public Request buildDeleteRequest(URI uri) {
            return buildRequest(client.newRequest(uri).method(HttpMethod.DELETE), null);
        }

        public Request buildPatchRequest(URI uri, ContentProvider contentProvider) {
            return buildRequest(client.newRequest(uri).method(HttpMethod.PATCH), contentProvider);
        }

        public Request buildHeadRequest(URI uri) {
            return buildRequest(client.newRequest(uri).method(HttpMethod.HEAD), null);
        }

    }


    private Request buildBinaryPostRequest(OctaneHttpRequest.PostBinaryOctaneHttpRequest octaneHttpRequest) throws IOException {

        GenericUrl domain = new GenericUrl(octaneHttpRequest.getRequestUrl());
        final Request httpRequest = requestFactory.buildPostRequest(domain.toURI(),
                generateMultiPartContent(octaneHttpRequest));
        if (octaneHttpRequest.getAcceptType() != null)
            httpRequest.getHeaders().add(HttpHeader.ACCEPT, octaneHttpRequest.getAcceptType());

        return httpRequest;
    }

    private MultiPartContentProvider generateBinaryBulkPostRequest(OctaneHttpRequest.PostBinaryBulkOctaneHttpRequest postBinaryBulkOctaneHttpRequest) {
        MultiPartContentProvider content = new MultiPartContentProvider(HTTP_MULTIPART_BOUNDARY_VALUE);

        postBinaryBulkOctaneHttpRequest.getBinaryFileInfo()
                .forEach(binaryFile -> addBinaryFileToMultiPart(content, postBinaryBulkOctaneHttpRequest.getBinaryContentType(), binaryFile));
        return content;
    }

    /**
     * Generates HTTP content based on input parameters and stream.
     *
     * @param octaneHttpRequest - JSON entity model.
     * @return - Generated HTTP content.
     */
    private MultiPartContentProvider generateMultiPartContent(OctaneHttpRequest.PostBinaryOctaneHttpRequest octaneHttpRequest) {

        MultiPartContentProvider content = new MultiPartContentProvider(HTTP_MULTIPART_BOUNDARY_VALUE);
        return addBinaryFileToMultiPart(content, octaneHttpRequest.getBinaryContentType(), Triple.of(octaneHttpRequest.getContent(), octaneHttpRequest.getBinaryInputStream(), octaneHttpRequest.getBinaryContentName()));
    }

    private MultiPartContentProvider addBinaryFileToMultiPart(MultiPartContentProvider content, String contentType, Triple<String, InputStream, String> binaryContent) {
        ByteBufferContentProvider byteArrayContent = new ByteBufferContentProvider("application/json", ByteBuffer.wrap(binaryContent.getLeft().getBytes(StandardCharsets.UTF_8)));
        HttpFields httpHeaders = new HttpFields();
        httpHeaders.add(HttpHeader.ACCEPT_ENCODING, "gzip");

        content.addFilePart(HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE, "blob", byteArrayContent, httpHeaders);
        // Add Stream
        try {
            byteArrayContent = new ByteBufferContentProvider(contentType, ByteBuffer.wrap(IOUtils.toByteArray(binaryContent.getMiddle())));
            HttpFields httpHeaders1 = new HttpFields();
            httpHeaders1.add(HttpHeader.ACCEPT_ENCODING, "gzip");
            content.addFilePart("content", binaryContent.getRight(), byteArrayContent, null);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Retrieve new cookie from set-cookie header
     *
     * @param response The response containing the set-cookie header or not
     * @return true if LWSSO cookie is renewed
     */
    private boolean updateLWSSOCookieValue(Response response) {
        HttpFields headers = response.getHeaders();
        boolean renewed = false;
        List<String> cookieHeaderValue = headers.getValuesList(SET_COOKIE);
        if (cookieHeaderValue.isEmpty()) {
            return false;
        }

        String url = response.getRequest().getURI().getRawPath();
        // if not auth and SET_COOKIE is received then stop all new requests until started requests are finished
        if (!url.contains(OAUTH_AUTH_URL)) {
            areNewCookiesReceived = true;
        }

        for (String strCookie : cookieHeaderValue) {
            List<java.net.HttpCookie> cookies;
            try {
                // Sadly the server seems to send back empty cookies for some reason
                cookies = java.net.HttpCookie.parse(strCookie);
            } catch (Exception ex) {
                logger.error("Failed to parse SET_COOKIE header, issue with cookie: \"{}\", {}", strCookie, ex);
                continue;
            }
            Optional<java.net.HttpCookie> lwssoCookie = cookies.stream().filter(a -> a.getName().equals(LWSSO_COOKIE_KEY)).findFirst();
            if (lwssoCookie.isPresent()) {
                lwssoValue = lwssoCookie.get().getValue();
                renewed = true;
            } else {
                cookies.stream().filter(cookie -> cookie.getName().equals(OCTANE_USER_COOKIE_KEY)).findAny().ifPresent(cookie -> octaneUserValue = cookie.getValue());
            }
        }

        return renewed;
    }


    public static int getHttpRequestRetryCount() {
        return HTTP_REQUEST_RETRY_COUNT;
    }

    private static RuntimeException wrapException(Exception exception, Request httpRequest) {
        if (exception.getCause() instanceof HttpResponseException) {

            HttpResponseException httpResponseException = (HttpResponseException) exception.getCause();
            logger.debug(LOGGER_RESPONSE_FORMAT, httpResponseException.getResponse().getStatus(), httpResponseException.getResponse().getReason(), httpResponseException.getResponse().getHeaders().stream().collect(Collectors.toList()));

            // It seems that Octane returns a message in 401 but this is swallowed by the HttpConnection as expected by the HTTP spec
            // So the only way to know if this should be re-authenticated is to see if there is a cookie in the request.  If so - we can fake the error and
            // ensure re-authentication
            if (httpResponseException.getResponse().getStatus() == 401) {
                try {
                    final String cookie = httpRequest.getCookies().stream()
                            .map(java.net.HttpCookie::toString)
                            .collect(Collectors.joining(";"));
                    ;
                    if (cookie != null) {
                        for (String splitCookie : cookie.split(";")) {
                            if (splitCookie.startsWith(LWSSO_COOKIE_KEY)) {
                                final LongFieldModel statusFieldModel = new LongFieldModel(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME, (long) httpResponseException.getResponse().getStatus());
                                final ErrorModel errorModel = new ErrorModel(Collections.singleton(statusFieldModel));
                                // assuming that we have a cookie and therefore can go for re-authentication...
                                errorModel.setValue(new StringFieldModel("errorCode", ERROR_CODE_TOKEN_EXPIRED));
                                return new OctaneException(errorModel);
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    // do nothing
                }
            }

            List<String> exceptionContentList = new ArrayList<>();
            HttpContentResponse response = (HttpContentResponse) httpResponseException.getResponse();
            exceptionContentList.add(response.getReason());
            exceptionContentList.add(response.getContentAsString());


            for (String exceptionContent : exceptionContentList) {
                try {
                    if (ModelParser.getInstance().hasErrorModels(exceptionContent)) {
                        Collection<ErrorModel> errorModels = ModelParser.getInstance().getErrorModels(exceptionContent);
                        Collection<EntityModel> entities = ModelParser.getInstance().getEntities(exceptionContent);
                        return new OctanePartialException(errorModels, entities);
                    } else if (ModelParser.getInstance().hasErrorModel(exceptionContent)) {
                        ErrorModel errorModel = ModelParser.getInstance().getErrorModelFromjson(exceptionContent);
                        errorModel.setValue(new LongFieldModel(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME, (long) httpResponseException.getResponse().getStatus()));
                        return new OctaneException(errorModel);
                    } else if (ModelParser.getInstance().hasServletError(exceptionContent)) {
                        ErrorModel errorModel = ModelParser.getInstance().getErrorModelFromServletJson(exceptionContent);
                        errorModel.setValue(new LongFieldModel(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME, (long) httpResponseException.getResponse().getStatus()));
                        return new OctaneException(errorModel);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        //In case nothing in exception is parsable
        return new RuntimeException(exception);
    }

    /**
     * Util method to debug log {@link HttpContent}
     *
     * @param content {@link HttpContent}
     */
    private static void logHttpContent(HttpContent content) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            content.advance();
            byteArrayOutputStream.write(content.getContent().array());
        } catch (IOException ex) {
            logger.error("Failed to log content of {} {}", content, ex);
        }
    }


    /**
     * Logs proxy system properties
     */
    private void logSystemProperties() {
        logProxySystemProperties();
        logSystemProxyForUrlDomain(urlDomain);
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

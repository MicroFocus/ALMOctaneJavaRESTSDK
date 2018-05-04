/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
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
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.exception.OctanePartialException;
import com.hpe.adm.nga.sdk.model.*;
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
import java.util.*;

/**
 * HTTP Client using Google's API
 * <p>This will be refactored in future releases to enable the use of different underlying APIs</p>
 */
public class GoogleHttpClient implements OctaneHttpClient {

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
    private static final String ERROR_CODE_TOKEN_EXPIRED = "VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT";

    private static final int HTTP_REQUEST_RETRY_COUNT = 1;

    protected HttpRequestFactory requestFactory;
    protected String lwssoValue = "";
    protected String octaneUserValue;
    protected final String urlDomain;
    protected Authentication lastUsedAuthentication;
    private final Map<OctaneHttpRequest, OctaneHttpResponse> cachedRequestToResponse = new HashMap<>();
    private final Map<OctaneHttpRequest, String> requestToEtagMap = new HashMap<>();

    /**
     * Request initializer called on every request made by the requestFactory
     */
    protected HttpRequestInitializer requestInitializer = request -> {
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

        if (lastUsedAuthentication != null) {
            String clientTypeHeader = lastUsedAuthentication.getClientHeader();
            if (clientTypeHeader != null && !clientTypeHeader.isEmpty()) {
                request.getHeaders().set(HPE_CLIENT_TYPE, clientTypeHeader);
            }
        }
        request.setReadTimeout(60000);
    };

    public GoogleHttpClient(final String urlDomain) {
        this.urlDomain = urlDomain;

        logProxySystemProperties();
        logSystemProxyForUrlDomain(urlDomain);

        HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        requestFactory = HTTP_TRANSPORT.createRequestFactory(requestInitializer);
    }

    /**
     * @return - Returns true if the authentication succeeded, false otherwise.
     */
    public boolean authenticate(Authentication authentication) {
        //reset so it's not sent to auth request, server might return 304
        lwssoValue = null;
        lastUsedAuthentication = authentication;
        try {
            final ByteArrayContent content = ByteArrayContent.fromString("application/json", authentication.getAuthenticationString());
            HttpRequest httpRequest = requestFactory.buildPostRequest(new GenericUrl(urlDomain + OAUTH_AUTH_URL), content);

            HttpResponse response = executeRequest(httpRequest);

            // Initialize Cookies keys
            return response.isSuccessStatusCode();
        } catch (RuntimeException e) {
            lastUsedAuthentication = null; //not reusable
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void signOut() {
        GenericUrl genericUrl = new GenericUrl(urlDomain + OAUTH_SIGNOUT_URL);
        try {
            HttpRequest httpRequest = requestFactory.buildPostRequest(genericUrl, null);
            HttpResponse response = executeRequest(httpRequest);

            if (response.isSuccessStatusCode()) {
                HttpHeaders hdr1 = response.getHeaders();
                updateLWSSOCookieValue(hdr1);
                lastUsedAuthentication = null;
            }
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

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
                        httpRequest.getHeaders().setIfNoneMatch(eTagHeader);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return httpRequest;
    }

    /**
     * Convert google implementation of {@link HttpResponse} to an implementation abstract {@link OctaneHttpResponse}
     * @param httpResponse implementation specific {@link HttpResponse}
     * @return {@link OctaneHttpResponse} created from the impl response object
     * @throws IOException if the response output stream stream cannot be read
     */
    protected OctaneHttpResponse convertHttpResponseToOctaneHttpResponse(HttpResponse httpResponse) {
        try {
            return new OctaneHttpResponse(httpResponse.getStatusCode(), httpResponse.getContent(), httpResponse.getContentCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        final HttpRequest httpRequest = convertOctaneRequestToGoogleHttpRequest(octaneHttpRequest);
        final HttpResponse httpResponse;

        try {
            httpResponse = executeRequest(httpRequest);

            final OctaneHttpResponse octaneHttpResponse = convertHttpResponseToOctaneHttpResponse(httpResponse);
            final String eTag = httpResponse.getHeaders().getETag();
            if (eTag != null) {
                requestToEtagMap.put(octaneHttpRequest, eTag);
                cachedRequestToResponse.put(octaneHttpRequest, octaneHttpResponse);
            }
            return octaneHttpResponse;

        } catch (RuntimeException exception) {

            //Return cached response
            if(exception.getCause() instanceof HttpResponseException) {
                HttpResponseException httpResponseException = (HttpResponseException) exception.getCause();
                final int statusCode = httpResponseException.getStatusCode();
                if (statusCode == HttpStatusCodes.STATUS_CODE_NOT_MODIFIED) {
                    return cachedRequestToResponse.get(octaneHttpRequest);
                }
            }

            //Handle session timeout exception
            if(retryCount > 0 && exception instanceof OctaneException) {
                OctaneException octaneException = (OctaneException) exception;
                StringFieldModel errorCodeFieldModel = (StringFieldModel) octaneException.getError().getValue("errorCode");

                //Handle session timeout
                if (errorCodeFieldModel != null && ERROR_CODE_TOKEN_EXPIRED.equals(errorCodeFieldModel.getValue()) && lastUsedAuthentication != null) {
                    logger.debug("Auth token expired, trying to re-authenticate");
                    try {
                        authenticate(lastUsedAuthentication);
                    } catch (OctaneException ex) {
                        logger.debug("Exception while retrying authentication: {}", ex.getMessage());
                    }
                    logger.debug("Retrying request, retries left: {}", retryCount);
                    return execute(octaneHttpRequest, --retryCount);
                }
            }

            throw exception;
        }
    }

    private HttpResponse executeRequest(final HttpRequest httpRequest) {
        logger.debug(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), httpRequest.getUrl().toString(), httpRequest.getHeaders().toString());

        final HttpContent content = httpRequest.getContent();

        // Make sure you don't log any http content send to the login rest api, since you don't want credentials in the logs
        if (content != null && logger.isDebugEnabled() && !httpRequest.getUrl().toString().contains(OAUTH_AUTH_URL)) {
            logHttpContent(content);
        }

        HttpResponse response;
        try {
            response = httpRequest.execute();
        } catch (Exception e) {
            throw wrapException(e);
        }

        logger.debug(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage(), response.getHeaders().toString());
        return response;
    }

    private static RuntimeException wrapException(Exception exception) {
        if(exception instanceof HttpResponseException) {

            HttpResponseException httpResponseException = (HttpResponseException) exception;
            logger.debug(LOGGER_RESPONSE_FORMAT, httpResponseException.getStatusCode(), httpResponseException.getStatusMessage(), httpResponseException.getHeaders().toString());

            List<String> exceptionContentList = new ArrayList<>();
            exceptionContentList.add(httpResponseException.getStatusMessage());
            exceptionContentList.add(httpResponseException.getContent());

            for(String exceptionContent : exceptionContentList) {
                try {
                    if(ModelParser.getInstance().hasErrorModels(exceptionContent)) {
                        Collection<ErrorModel> errorModels = ModelParser.getInstance().getErrorModels(exceptionContent);
                        Collection<EntityModel> entities = ModelParser.getInstance().getEntities(exceptionContent);
                        return new OctanePartialException(errorModels, entities);
                    } else {
                        ErrorModel errorModel = ModelParser.getInstance().getErrorModelFromjson(exceptionContent);
                        errorModel.setValue(new LongFieldModel("http_status_code", (long) httpResponseException.getStatusCode()));
                        return new OctaneException(errorModel);
                    }
                } catch (Exception ignored) {}
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
            logger.debug("MultipartContent: " + content.getType());
            multipartContent.getParts().forEach(part -> {
                logger.debug("Part: encoding: " + part.getEncoding() + ", headers: " + part.getHeaders());
                logHttpContent(part.getContent());
            });
        } else if (content instanceof InputStreamContent) {
            logger.debug("InputStreamContent: type: " + content.getType());
        } else if (content instanceof FileContent) {
            logger.debug("FileContent: type: " + content.getType() + ", filepath: " + ((FileContent) content).getFile().getAbsolutePath());
        } else {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                content.writeTo(byteArrayOutputStream);
                logger.debug("Content: type: " + content.getType() + ", " + byteArrayOutputStream.toString());
            } catch (IOException ex) {
                logger.error("Failed to log content of " + content, ex);
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

        ByteArrayContent byteArrayContent = new ByteArrayContent("application/json", octaneHttpRequest.getContent().getBytes());
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
        List<String> strHPSSOCookieCsrf1 = headers.getHeaderStringValues(SET_COOKIE);
        if (strHPSSOCookieCsrf1.isEmpty()) {
            return false;
        }

        /* Following code failed to parse set-cookie to get LWSSO cookie due to cookie version, check RFC 2965
        String strCookies = strHPSSOCookieCsrf1.toString();
        List<HttpCookie> Cookies = java.net.HttpCookie.parse(strCookies.substring(1, strCookies.length()-1));
        lwssoValue = Cookies.stream().filter(a -> a.getName().equals(LWSSO_COOKIE_KEY)).findFirst().get().getValue();*/
        for (String strCookie : strHPSSOCookieCsrf1) {
            List<HttpCookie> cookies;
            try {
                // Sadly the server seems to send back empty cookies for some reason
                cookies = HttpCookie.parse(strCookie);
            } catch (Exception ex) {
                logger.error("Failed to parse HPSSOCookieCsrf: " + ex.getMessage());
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

    public static int getHttpRequestRetryCount() {
        return HTTP_REQUEST_RETRY_COUNT;
    }

    /**
     * Log jvm proxy system properties for debugging connection issues
     */
    private static void logProxySystemProperties(){
        String[]  proxySysProperties = new String[]{"java.net.useSystemProxies", "http.proxyHost", "http.proxyPort", "https.proxyHost", "https.proxyPort"};
        Arrays.stream(proxySysProperties)
                .forEach(sysProp -> logger.debug(sysProp + ": " + System.getProperty(sysProp)));
    }

    /**
     * Log proxy for octane url domain using system wide {@link ProxySelector}
     * @param urlDomain base url of octane server
     */
    private static void logSystemProxyForUrlDomain(String urlDomain){
        try {
            List<Proxy> proxies = ProxySelector.getDefault().select(URI.create(urlDomain));
            logger.debug("System proxies for " + urlDomain + ": " + proxies.toString());
        } catch (SecurityException ex) {
            logger.debug("SecurityException when trying to access system wide proxy selector: " + ex);
        }
    }

}
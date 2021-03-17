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
package com.hpe.adm.nga.sdk.extension.network.google;


import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.io.CharStreams;
import com.hpe.adm.nga.sdk.extension.network.RequestInterceptor;
import com.hpe.adm.nga.sdk.extension.network.ResponseInterceptor;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http client with support for adding {@link RequestInterceptor} and {@link RequestInterceptor} objects
 */
public class InterceptorGoogleHttpClient extends GoogleHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(InterceptorGoogleHttpClient.class.getName());

    private static final List<RequestInterceptor> requestInterceptors = new ArrayList<>(1);
    private static final List<ResponseInterceptor> responseInterceptors = new ArrayList<>(1);

    public InterceptorGoogleHttpClient(String urlDomain) {
        super(urlDomain);
    }

    public InterceptorGoogleHttpClient(String urlDomain, com.hpe.adm.nga.sdk.authentication.BasicAuthentication authentication) {
        super(urlDomain, authentication);
    }

    /**
     * Util method that sets the http proxy
     *
     * @param httpProxy to use for all requests
     */
    public void setHttpProxy(Proxy httpProxy) {
        HttpTransport HTTP_TRANSPORT = new NetHttpTransport.Builder().setProxy(httpProxy).build();
        requestFactory = HTTP_TRANSPORT.createRequestFactory(requestInitializer);
    }

    @Override
    protected HttpRequest convertOctaneRequestToGoogleHttpRequest(OctaneHttpRequest octaneHttpRequest) {
        HttpRequest request = super.convertOctaneRequestToGoogleHttpRequest(octaneHttpRequest);
        requestInterceptors.forEach(requestInterceptor -> applyRequestInterceptor(requestInterceptor, request));
        return request;
    }

    @Override
    protected OctaneHttpResponse convertHttpResponseToOctaneHttpResponse(HttpResponse httpResponse) {
        responseInterceptors.forEach(responseInterceptor -> applyResponseInterceptor(responseInterceptor, httpResponse));
        return super.convertHttpResponseToOctaneHttpResponse(httpResponse);
    }

    /**
     * Apply request interceptor to the {@link HttpRequest} object
     *
     * @param requestInterceptor interceptor implementation
     * @param httpRequest        target {@link HttpRequest}
     */
    private static void applyRequestInterceptor(RequestInterceptor requestInterceptor, HttpRequest httpRequest) {
        logger.debug("Applying responseInterceptor {} of {}", responseInterceptors.indexOf(requestInterceptor) + 1, responseInterceptors.size());

        //URL
        String newUrl = requestInterceptor.url(httpRequest.getUrl().toString());
        httpRequest.setUrl(new GenericUrl(newUrl));

        //CONTENT
        if (httpRequest.getContent() != null && !(httpRequest.getContent() instanceof EmptyContent)) {
            if (httpRequest.getContent() instanceof ByteArrayContent) {
                ByteArrayContent byteArrayContent = (ByteArrayContent) httpRequest.getContent();
                try {
                    String byteArrayContentString = CharStreams.toString(new InputStreamReader(byteArrayContent.getInputStream(), StandardCharsets.UTF_8));
                    String newContent = requestInterceptor.content(byteArrayContentString);
                    httpRequest.setContent(new ByteArrayContent(byteArrayContent.getType(), newContent.getBytes()));
                } catch (IOException e) {
                    logger.error("Failed to call request interceptor content method, error while reading ByteArrayContent", e);
                }
            }
        }

        //HEADERS
        final Map<String, Object> oldHeaders = new HashMap<>();
        httpRequest.getHeaders().forEach(oldHeaders::put);
        final Map<String, Object> newHeaders = requestInterceptor.headers(oldHeaders);
        httpRequest.getHeaders().clear();
        newHeaders.forEach((key, value) -> httpRequest.getHeaders().set(key, value));
    }

    /**
     * Apply response interceptor to the {@link HttpResponse} object
     *
     * @param responseInterceptor interceptor implementation
     * @param httpResponse        target {@link HttpResponse}
     */
    private static void applyResponseInterceptor(ResponseInterceptor responseInterceptor, HttpResponse httpResponse) {
        logger.debug("Applying responseInterceptor {} of {}", responseInterceptors.indexOf(responseInterceptor) + 1, responseInterceptors.size());

        //HEADERS
        final Map<String, Object> oldHeaders = new HashMap<>();
        httpResponse.getHeaders().forEach(oldHeaders::put);
        final Map<String, Object> newHeaders = responseInterceptor.headers(oldHeaders);
        httpResponse.getHeaders().clear();
        newHeaders.forEach((key, value) -> httpResponse.getHeaders().set(key, value));
    }

    /**
     * Add a request interceptor to the http client
     *
     * @param requestInterceptor implementation of {@link RequestInterceptor}
     * @return true if it was added, false otherwise
     */
    public static boolean addRequestInterceptor(RequestInterceptor requestInterceptor) {
        return requestInterceptors.add(requestInterceptor);
    }

    /**
     * Remove a request interceptor to the http client
     *
     * @param requestInterceptor implementation of {@link RequestInterceptor}
     * @return true if it was removed, false otherwise
     */
    public static boolean removeRequestInterceptor(RequestInterceptor requestInterceptor) {
        return requestInterceptors.remove(requestInterceptor);
    }

    /**
     * Add a response interceptor to the http client
     *
     * @param responseInterceptor implementation of {@link ResponseInterceptor}
     * @return true if it was added, false otherwise
     */
    public static boolean addResponseInterceptor(ResponseInterceptor responseInterceptor) {
        return responseInterceptors.add(responseInterceptor);
    }

    /**
     * Remove a response interceptor to the http client
     *
     * @param responseInterceptor implementation of {@link ResponseInterceptor}
     * @return true if it was removed, false otherwise
     */
    public static boolean removeResponseInterceptor(ResponseInterceptor responseInterceptor) {
        return responseInterceptors.add(responseInterceptor);
    }

}

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

package com.hpe.adm.nga.sdk.extension.network.google;


import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.io.CharStreams;
import com.hpe.adm.nga.sdk.extension.network.RequestInterceptor;
import com.hpe.adm.nga.sdk.extension.network.ResponseInterceptor;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterceptorGoogleHttpClient extends GoogleHttpClient {

    private static final Logger logger = LogManager.getLogger(InterceptorGoogleHttpClient.class.getName());

    private static List<RequestInterceptor> requestInterceptors = new ArrayList<>(1);
    private static List<ResponseInterceptor> responseInterceptors = new ArrayList<>(1);

    public InterceptorGoogleHttpClient(String urlDomain) {
        super(urlDomain);
    }

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
    protected OctaneHttpResponse convertHttpResponseToOctaneHttpResponse(HttpResponse httpResponse) throws IOException {
        responseInterceptors.forEach(responseInterceptor -> applyResponseInterceptor(responseInterceptor, httpResponse));
        return super.convertHttpResponseToOctaneHttpResponse(httpResponse);
    }

    private static void applyRequestInterceptor(RequestInterceptor requestInterceptor, HttpRequest httpRequest){
        logger.debug("Applying responseInterceptor " + (responseInterceptors.indexOf(requestInterceptor) + 1) + " of " + responseInterceptors.size());

        //URL
        String newUrl = requestInterceptor.url(httpRequest.getUrl().toString());
        httpRequest.setUrl(new GenericUrl(newUrl));

        //CONTENT
        if(httpRequest.getContent() != null && !(httpRequest.getContent() instanceof EmptyContent)){
            if(httpRequest.getContent() instanceof ByteArrayContent){
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
        httpRequest.getHeaders().forEach( (key, value) -> oldHeaders.put(key, value));
        final Map<String, Object> newHeaders = requestInterceptor.headers(oldHeaders);
        httpRequest.getHeaders().clear();
        newHeaders.forEach((key, value) -> httpRequest.getHeaders().set(key, value));
    }

    private static void applyResponseInterceptor(ResponseInterceptor responseInterceptor, HttpResponse httpResponse){
        logger.debug("Applying responseInterceptor " + (responseInterceptors.indexOf(responseInterceptor) + 1) + " of " + responseInterceptors.size());

        //HEADERS
        final Map<String, Object> oldHeaders = new HashMap<>();
        httpResponse.getHeaders().forEach( (key, value) -> oldHeaders.put(key, value));
        final Map<String, Object> newHeaders = responseInterceptor.headers(oldHeaders);
        httpResponse.getHeaders().clear();
        newHeaders.forEach((key, value) -> httpResponse.getHeaders().set(key, value));
    }

    public static boolean addRequestInterceptor(RequestInterceptor requestInterceptor) {
        return requestInterceptors.add(requestInterceptor);
    }
    public static boolean removeRequestInterceptor(RequestInterceptor requestInterceptor) {
        return requestInterceptors.remove(requestInterceptor);
    }
    public static boolean addResponseInterceptor(ResponseInterceptor responseInterceptor) {
        return responseInterceptors.add(responseInterceptor);
    }
    public static boolean removeResponseInterceptor(ResponseInterceptor responseInterceptor) {
        return responseInterceptors.add(responseInterceptor);
    }

}
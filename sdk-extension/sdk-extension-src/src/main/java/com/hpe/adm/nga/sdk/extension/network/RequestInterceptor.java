/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package com.hpe.adm.nga.sdk.extension.network;

import java.util.Map;

/**
 * Interceptor for http requests
 * Methods are called before request is passed to the {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
 */
public interface RequestInterceptor {

    /**
     * Get and change the url before the request is executed
     * @param url original url
     * @return new url that is passed {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
     */
    default String url(String url){
        return url;
    }

    /**
     * Get and change the content before the request is executed
     * @param content original content of the request
     * @return new content that is passed {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
     */
    default String content(String content){ return content; }

    /**
     * Get and change the headers before the request is executed
     * @param headers headers of the original request
     * @return new headers that are passed {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
     */
    default Map<String, Object> headers(Map<String, Object> headers){
        return headers;
    }
}
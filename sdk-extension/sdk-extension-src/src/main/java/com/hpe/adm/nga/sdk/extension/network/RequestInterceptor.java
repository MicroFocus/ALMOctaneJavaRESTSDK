/**
 * Copyright 2016-2023 Open Text.
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

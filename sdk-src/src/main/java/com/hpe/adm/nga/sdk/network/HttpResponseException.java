/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package com.hpe.adm.nga.sdk.network;

/**
 *
 * Exception thrown when an error status code is detected in an HTTP response.
 *
 * Created by leufl on 7/11/2016.
 */

public class HttpResponseException extends com.google.api.client.http.HttpResponseException {

    /**
     * Constructor that constructs a detail message from the given HTTP response that includes the status code, status message and HTTP response content.
     * Callers of this constructor should call OctaneHttpResponse.disconnect() after HttpResponseException is instantiated.
     * @param response - HTTP response
     */
    public HttpResponseException(com.google.api.client.http.HttpResponse response) {
        super(response);
    }
}

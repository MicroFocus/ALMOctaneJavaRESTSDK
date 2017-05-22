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

import java.io.InputStream;

/**
 *
 * HTTP response.
 * Callers should call disconnect() when the HTTP response object is no longer needed. However, disconnect() does not have to be called if the response stream is properly closed.
 *
 * Created by leufl on 2/11/2016.
 */
public class OctaneHttpResponse {

    private final int statusCode;
    private final String content;
    private final InputStream inputStream;

    public OctaneHttpResponse(int statusCode, String content, InputStream inputStream) {
        this.statusCode = statusCode;
        this.content = content;
        this.inputStream = inputStream;
    }

    /**
     * @return - Returns whether received a successful HTTP status code
     */
    public boolean isSuccessStatusCode(){
        return statusCode >= 200 && statusCode < 300;
    }
//
//    /**
//     * @return - Returns the HTTP status code or 0 for none.
//     */
//    int getStatusCode();
//
//    /**
//     * @return - Returns the HTTP status message or null for none.
//     */
//    String getStatusMessage();
//
//    /**
//     * @return - Returns the HTTP response headers.
//     */
//    Map getHeaders();

    /**
     * Parses the content of the HTTP response from getContent() and reads it into a string.
     * Since this method returns "" for no content, a simpler check for no content is to check if getContent() is null.
     * All content is read from the input content stream rather than being limited by the Content-Length. For the character set, it follows the specification by parsing the "charset" parameter of the Content-Type header or by default "ISO-8859-1" if the parameter is missing.
     * @return - parsed string or "" for no content
     */
    public String getContent(){
        return content;
    }

    /**
     * Returns the content of the HTTP response.
     * The result is cached, so subsequent calls will be fast.
     * Callers should call InputStream.close() after the returned InputStream is no longer needed.
     * disconnect() does not have to be called if the content is closed.
     * @return - input stream content of the HTTP response or null for none
     */
    public InputStream getInputStream() {
        return inputStream;
    }
}

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
package com.hpe.adm.nga.sdk.network.google;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;

public class GoogleHttpClientProfiler extends GoogleHttpClient{

    private int authenticateCount;
    private int signOutCount;
    private int convertOctaneRequestToGoogleHttpRequestCount;
    private int convertHttpResponseToOctaneHttpResponseCount;
    private int executeCount;

    public GoogleHttpClientProfiler(String urlDomain) {
        super(urlDomain);
        reset();
    }

    public void reset() {
        authenticateCount = 0;
        signOutCount = 0;
        convertOctaneRequestToGoogleHttpRequestCount = 0;
        convertHttpResponseToOctaneHttpResponseCount = 0;
        executeCount = 0;
    }

    @Override
    public synchronized boolean authenticate(Authentication authentication) {
        authenticateCount++;
        System.out.println("@@PID "+ Thread.currentThread().getId());
        return super.authenticate(authentication);
    }

    @Override
    public synchronized void signOut() {
        signOutCount++;
        super.signOut();
    }

    @Override
    protected HttpRequest convertOctaneRequestToGoogleHttpRequest(OctaneHttpRequest octaneHttpRequest) {
        convertOctaneRequestToGoogleHttpRequestCount++;
        return super.convertOctaneRequestToGoogleHttpRequest(octaneHttpRequest);
    }

    @Override
    protected OctaneHttpResponse convertHttpResponseToOctaneHttpResponse(HttpResponse httpResponse) {
        convertHttpResponseToOctaneHttpResponseCount++;
        return super.convertHttpResponseToOctaneHttpResponse(httpResponse);
    }

    @Override
    public OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest) {
        executeCount++;
        return super.execute(octaneHttpRequest);
    }

    public int getAuthenticateCount() {
        return authenticateCount;
    }

    public int getSignOutCount() {
        return signOutCount;
    }

    public int getConvertOctaneRequestToGoogleHttpRequestCount() {
        return convertOctaneRequestToGoogleHttpRequestCount;
    }

    public int getConvertHttpResponseToOctaneHttpResponseCount() {
        return convertHttpResponseToOctaneHttpResponseCount;
    }

    public int getExecuteCount() {
        return executeCount;
    }

    public String getAll()
    {
        return "\t authenticateCount " + authenticateCount +
                "\n\t signOutCount " + signOutCount +
                "\n\t convertOctaneRequestToGoogleHttpRequestCount " + convertOctaneRequestToGoogleHttpRequestCount +
                "\n\t convertHttpResponseToOctaneHttpResponseCount " + convertHttpResponseToOctaneHttpResponseCount +
                "\n\t executeCount " + executeCount;
    }
}

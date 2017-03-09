/*
 *
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
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
 */
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.AuthenticationProvider;

/**
 *
 * HTTP Client
 *
 * Created by leufl on 2/11/2016.
 */
public interface OctaneHttpClient {

    //Constants
    String OAUTH_AUTH_URL = "/authentication/sign_in";
    String OAUTH_SIGNOUT_URL = "/authentication/sign_out";
    String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
    String HPE_CLIENT_TYPE = "HPECLIENTTYPE";

    /**
     * Sets the {@link AuthenticationProvider} object the HTTP client will use each time it has to perform authentication with the server
     * Be careful with your {@link AuthenticationProvider} instance lifecycle,
     * as the HTTP client might try to call {@link AuthenticationProvider#getAuthentication()} method
     * when needed
     * @param authenticationProvider an implementation of {@link AuthenticationProvider}
     */
    void setAuthenticationProvider(AuthenticationProvider authenticationProvider);

    /**
     * Attempts to use the {@link AuthenticationProvider} class to authenticate with the Octane server
     * @throws com.hpe.adm.nga.sdk.exception.OctaneException if the authentication provider was not set
     * @return true if authentication successful, false otherwise
     */
    boolean authenticate();

    /**
     * Signs out and removes cookies, the authentication provider is not affected
     */
    void signOut();

    OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest);
}

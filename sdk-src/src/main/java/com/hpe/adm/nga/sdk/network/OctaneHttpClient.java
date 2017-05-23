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
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.authentication.Authentication;

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
     * Authenticate with the Octane server using an implementation of the {@link Authentication} class
     * @param authentication implementation of {@link Authentication}
     * @return true if the authentication was successful, false otherwise
     */
    boolean authenticate(Authentication authentication);

    /**
     * Signs out and removes cookies
     */
    void signOut();

    OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest);
}

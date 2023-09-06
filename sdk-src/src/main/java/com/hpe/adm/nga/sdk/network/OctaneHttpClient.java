/*
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
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.authentication.JSONAuthentication;

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
    String OCTANE_USER_COOKIE_KEY = "OCTANE_USER";

    /**
     * Authenticate with the Octane server using an implementation of the {@link JSONAuthentication} class.
     * If basic authentication is being used then the authentication is an inherent part of execute and therefore
     * authentication does not need to be called explicitly.  True will be returned in this case
     *
     * @return true if the authentication was successful, false otherwise
     */
    boolean authenticate();

    /**
     * Signs out and removes cookies
     */
    void signOut();

    OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest);
}

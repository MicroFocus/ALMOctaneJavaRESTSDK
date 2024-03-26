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

package com.hpe.adm.nga.sdk.authentication;

import com.hpe.adm.nga.sdk.APIMode;

/**
 * An abstract class to use the authentication with a mode set for implementing JSON Authentication
 * This can be overidden if the authentication details are kept obtained in a different manner
 */
abstract public class JSONAuthentication extends Authentication {

    private static final String JSON_STRING = "{\"%s\":\"%s\",\"%s\":\"%s\"}";

    JSONAuthentication(final APIMode apiMode) {
        super(apiMode, false, false);
    }

    /**
     * Returns the authentication string for most authentication purposes.
     *
     * @return The correct JSON string
     */
    public final String getAuthenticationString() {
        return String.format(JSON_STRING, getAuthenticationIdKey(), getAuthenticationId(), getAuthenticationSecretKey(), getAuthenticationSecret());
    }

    /**
     * The key that is used for the id (client or user)
     *
     * @return client_id or username
     */
    abstract protected String getAuthenticationIdKey();

    /**
     * The key that is used for the secret (client or user)
     *
     * @return client_secret or password
     */
    abstract protected String getAuthenticationSecretKey();

    /**
     * The id that is used for the authentication
     *
     * @return client id or username
     */
    abstract protected String getAuthenticationId();

    /**
     * The secret that is used for the authentication
     *
     * @return client secret or password
     */
    abstract protected String getAuthenticationSecret();
}

/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.authentication;

import com.hpe.adm.nga.sdk.APIMode;

/**
 * Represents basic authentication
 */
public abstract class BasicAuthentication extends Authentication {

    /**
     * Represents basic authentication
     *
     * @param apiMode The mode to use if necessary
     */
    BasicAuthentication(final APIMode apiMode) {
        super(apiMode, true);
    }

    /**
     * The id that is used for the authentication
     *
     * @return client id or username
     */
    abstract public String getAuthenticationId();

    /**
     * The secret that is used for the authentication
     *
     * @return client secret or password
     */
    abstract public String getAuthenticationSecret();
}

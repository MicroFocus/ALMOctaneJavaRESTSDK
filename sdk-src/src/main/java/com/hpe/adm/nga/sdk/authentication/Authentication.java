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

import java.util.Optional;

/**
 * Interface of Authentication , hold contract functions. <br>
 * Provided implementations: {@link SimpleUserAuthentication}, {@link SimpleClientAuthentication},
 * {@link SimpleBasicAuthentication} <br>
 * Note that the default implementations keep the credentials in memory. <br>
 */
public abstract class Authentication {

    private final APIMode apiMode;
    private final boolean isBasicAuthentication;

    /**
     * The mode to use or null if none is needed
     *
     * @param apiMode The mode
     */
    Authentication(final APIMode apiMode, final boolean isBasicAuthentication) {
        this.apiMode = apiMode;
        this.isBasicAuthentication = isBasicAuthentication;
    }

    /**
     * Returns the API Mode header that is added to all calls to the REST API.  This usually refers to whether this should use
     * the technical preview or not (however other non-documented modes are possible) See the REST API documentation for
     * further information
     * This will only be used if it is non-empty
     *
     * @return The API mode header.  If none is sent then this should be null
     */
    public final Optional<APIMode> getAPIMode() {
        return Optional.ofNullable(apiMode);
    }

    /**
     * Returns whether this authentication type supports basic authentication
     *
     * @return Supports basic authentication
     */
    public final boolean isBasicAuthentication() {
        return isBasicAuthentication;
    }
}



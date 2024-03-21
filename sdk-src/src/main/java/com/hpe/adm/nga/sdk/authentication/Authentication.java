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
    private final boolean isSessionIdAuthentication;

    /**
     * The mode to use or null if none is needed
     *
     * @param apiMode The mode
     */
    Authentication(final APIMode apiMode, final boolean isBasicAuthentication, final boolean isSessionIdAuthentication) {
        this.apiMode = apiMode;
        this.isBasicAuthentication = isBasicAuthentication;
        this.isSessionIdAuthentication = isSessionIdAuthentication;
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

    /**
     * Returns whether this authentication type is based on provided LWSSOCOOKIE
     *
     * @return is based on LWSSO
     */
    public final boolean isSessionIdAuthentication() {
        return isSessionIdAuthentication;
    }
}



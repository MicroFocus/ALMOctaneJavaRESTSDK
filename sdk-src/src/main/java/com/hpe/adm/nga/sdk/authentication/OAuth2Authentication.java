/*
 * Copyright 2016-2025 Open Text.
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
 * Authentication using the OAuth2 token exchange mechanism.
 */
public class OAuth2Authentication extends Authentication {
    private final String clientId;
    private final String clientSecret;

    private String accessToken;
    private boolean isAuthenticated;

    /**
     * @param apiMode      the mode to use or null if none is needed
     * @param accessToken  the access token from the IDP
     * @param clientId     the client id for performing the token exchange operation in Octane
     * @param clientSecret the client secret for performing the token exchange operation in Octane
     */
    public OAuth2Authentication(final String accessToken, final String clientId, final String clientSecret,
                                final APIMode apiMode) {
        super(apiMode, AuthenticationType.OAUTH2);
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.isAuthenticated = false;
    }

    /**
     * @param accessToken the access token from the IDP
     * @param clientId     the client id for performing the token exchange operation in Octane
     * @param clientSecret the client secret for performing the token exchange operation in Octane
     */
    public OAuth2Authentication(final String accessToken, final String clientId, final String clientSecret) {
        this(accessToken, clientId, clientSecret, null);
    }

    /**
     * Get the access token received from the IDP, if the token exchange hasn't been performed yet.
     * Otherwise, the exchanged token is returned.
     *
     * @return a string containing the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Get the client id for performing the token exchange operation in Octane.
     * This token is used to perform the basic authentication.
     *
     * @return a string containing the client id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Get the client secret for performing the token exchange operation in Octane.
     * This token is used to perform the basic authentication.
     *
     * @return a string containing the client secret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Returns true if the authentication has been performed. This means that the
     * access token has been received from Octane and the exchange has been performed.
     *
     * @return true if the authentication has been performed
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * Exchange the IdP access token for an Octane access token.
     *
     * @param accessToken the access token received from Octane
     */
    public void exchangeAccessToken(final String accessToken) {
        this.accessToken = accessToken;
        this.isAuthenticated = true;
    }
}

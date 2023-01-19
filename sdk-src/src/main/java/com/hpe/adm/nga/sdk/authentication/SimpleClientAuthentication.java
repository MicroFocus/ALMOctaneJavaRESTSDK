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
 * Default class to enable api key authentications
 * Created by brucesp on 23/05/2016.
 */
public class SimpleClientAuthentication extends ClientAuthentication {

    private final String clientId;
    private final String clientSecret;

    /*
     * @param clientId The api key
     * @param clientSecret The api secret
     * @param apiMode API Mode - can be nullable
     */
    public SimpleClientAuthentication(final String clientId, final String clientSecret, final APIMode apiMode) {
        super(apiMode);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /*
     * @param clientId The api key
     * @param clientSecret The api secret
     */
    public SimpleClientAuthentication(final String clientId, final String clientSecret) {
        this(clientId, clientSecret, null);
    }

    protected String getAuthenticationId() {
        return clientId;
    }

    protected String getAuthenticationSecret() {
        return clientSecret;
    }
}

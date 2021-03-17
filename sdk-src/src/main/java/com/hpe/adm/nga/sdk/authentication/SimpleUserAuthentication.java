/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
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
 * Default class to enable user authentications
 * Created by brucesp on 23/05/2016.
 */
public class SimpleUserAuthentication extends UserAuthentication implements SupportsAutomaticReAuthentication {

    private final String userName;
    private final String password;

    /**
     * @param userName The user
     * @param password The password
     * @param apiMode  API Mode - can be nullable
     */
    public SimpleUserAuthentication(final String userName, final String password, final APIMode apiMode) {
        super(apiMode);
        this.userName = userName;
        this.password = password;
    }

    /**
     * @param userName The user
     * @param password The password
     */
    public SimpleUserAuthentication(final String userName, final String password) {
        this(userName, password, null);
    }

    protected String getAuthenticationId() {
        return userName;
    }

    protected String getAuthenticationSecret() {
        return password;
    }
}

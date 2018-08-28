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

package com.microfocus.adm.nga.sdk.authentication;

/**
 *
 * Created by brucesp on 23/05/2016.
 */
public class SimpleUserAuthentication extends UserAuthentication {

    private final String userName;
    private final String password;

    public SimpleUserAuthentication(final String userName, final String password, final String clientTypeHeader) {
        super(clientTypeHeader);
        this.userName = userName;
        this.password = password;
    }

    public SimpleUserAuthentication(final String userName, final String password) {
        this(userName, password, null);
    }

    protected String getUserName() {
        return userName;
    }

    protected String getPassword() {
        return password;
    }
}

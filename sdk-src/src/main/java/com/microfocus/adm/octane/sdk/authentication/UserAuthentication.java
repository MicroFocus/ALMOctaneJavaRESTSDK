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
package com.microfocus.adm.octane.sdk.authentication;

/**
 *
 * Created by brucesp on 19-Dec-16.
 */
abstract class UserAuthentication implements Authentication {

    private static final String JSON_STRING = "{\"user\":\"%s\",\"password\":\"%s\"}";

    private final String clientTypeHeader;

    UserAuthentication(final String clientTypeHeader) {
        this.clientTypeHeader = clientTypeHeader;
    }

    @Override
    public final String getClientHeader() {
        return clientTypeHeader;
    }

    @Override
    public final String getAuthenticationString() {
        return String.format(JSON_STRING, getUserName(), getPassword());
    }

    abstract protected String getUserName();
    abstract protected String getPassword();
}

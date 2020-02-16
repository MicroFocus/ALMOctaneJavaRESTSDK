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
package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;

/**
 * Created by brucesp on 06/06/2016.
 */
public class AuthenticationUtils {

    private static final String HPE_REST_API_TECH_PREVIEW = "HPE_REST_API_TECH_PREVIEW";

    public static Authentication getAuthentication(boolean usePrivateApi) {
        final String header = usePrivateApi ? HPE_REST_API_TECH_PREVIEW : null;
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String authenticationType = configuration.getString("sdk.authenticationType");
        if (authenticationType == null || authenticationType.isEmpty() || authenticationType.equals("userpass")) {
            return new SimpleUserAuthentication(configuration.getString("sdk.username"), configuration.getString("sdk.password"), header);
        } else if (authenticationType.equals("client")) {
            return new SimpleClientAuthentication(configuration.getString("sdk.clientId"), configuration.getString("sdk.clientSecret"), header);
        } else {
            throw new IllegalArgumentException("Authentication not set!");
        }
    }

    public static Authentication getAuthentication() {
        return getAuthentication(true);
    }
}

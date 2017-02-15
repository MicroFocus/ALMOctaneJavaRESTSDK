/*
 *
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;

/**
 * Creates a simple context
 *
 * Created by brucesp on 03-Jan-17.
 */
public class CreateContextExample {

    public void createContext() {
        // two types of authentication
        // 1) API Ket
        Authentication clientAuthentication = new SimpleClientAuthentication("client_id", "client_secret");

        // 2) User/pass
        Authentication userPassAuthentication = new SimpleUserAuthentication("user", "password");

        // get instance of Octane Builder
        final Octane.Builder octaneBuilder = new Octane.Builder(clientAuthentication);

        // now we can add the server
        octaneBuilder.Server("http://server.com:3232");
        // the sharedspace
        octaneBuilder.sharedSpace(323213231);
        // the workspace
        octaneBuilder.workSpace(32313);

        // finally we build the context and get an Octane instance:

        Octane octane = octaneBuilder.build();
    }

}

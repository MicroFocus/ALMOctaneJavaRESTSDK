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
package com.microfocus.adm.almoctane.sdk.examples;

import com.microfocus.adm.almoctane.sdk.Octane;
import com.microfocus.adm.almoctane.sdk.authentication.Authentication;
import com.microfocus.adm.almoctane.sdk.authentication.SimpleClientAuthentication;

/**
 * Creates a simple context
 *
 * Created by brucesp on 03-Jan-17.
 */
public class CreateContextExample {

    public static void main (String [] args){
        new CreateContextExample().createContext();
    }

    public void createContext() {
        // two types of authentication
        // 1) API Key
        Authentication clientAuthentication = new SimpleClientAuthentication("clientId", "clientSecret");

        // 2) User/pass
        //Authentication userPassAuthentication = new SimpleUserAuthentication("user", "password");

        // get instance of Octane Builder
        final Octane.Builder octaneBuilder = new Octane.Builder(clientAuthentication);

        // now we can add the server
        octaneBuilder.Server("https://awesome-octane.saas.microfocus.com");
        // the sharedspace
        octaneBuilder.sharedSpace(1001);
        // the workspace
        octaneBuilder.workSpace(1002);

        // finally we build the context and get an Octane instance:

        Octane octane = octaneBuilder.build();

       // octane.entityList("defects").get().limit(2).execute();
        octane.entityList("defects").get().execute();
    }

}

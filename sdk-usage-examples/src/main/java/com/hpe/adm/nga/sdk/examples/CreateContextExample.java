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
package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.OctaneWrapper;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;

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
        SimpleClientAuthentication clientAuthentication = new SimpleClientAuthentication("clientId", "clientSecret");

        // 2) User/pass
        //Authentication userPassAuthentication = new SimpleUserAuthentication("user", "password");

        // get instance of OctaneWrapper Builder
        final OctaneWrapper.Builder octaneWrapperBuilder = new OctaneWrapper.Builder();
        // add authentication and get instance of the correct wrapper
        final OctaneWrapper.ExplicitAuthenticationOctaneWrapperBuilder explicitAuthenticationOctaneWrapperBuilder = octaneWrapperBuilder.authentication(clientAuthentication);

        // now we can add the server
        explicitAuthenticationOctaneWrapperBuilder.Server("http://myd-vm10632.hpeswlab.net:8081");
        // now we can the instance of Octane builder
        final Octane.Builder octaneBuilder = explicitAuthenticationOctaneWrapperBuilder.build().octane();
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

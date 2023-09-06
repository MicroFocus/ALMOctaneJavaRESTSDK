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
package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
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
        Authentication clientAuthentication = new SimpleClientAuthentication("clientId", "clientSecret");

        // 2) User/pass
        //Authentication userPassAuthentication = new SimpleUserAuthentication("user", "password");

        // get instance of Octane Builder
        final Octane.Builder octaneBuilder = new Octane.Builder(clientAuthentication);

        // now we can add the server
        octaneBuilder.Server("http://myd-vm10632.hpeswlab.net:8081");
        // the sharedspace
        octaneBuilder.sharedSpace(1001);
        // the workspace
        octaneBuilder.workSpace(1002);
        //sets the client to HTTP2 client, by default is set to HTTP1.1
        //octaneBuilder.isHttp2(true);

        // finally we build the context and get an Octane instance:

        Octane octane = octaneBuilder.build();

       // octane.entityList("defects").get().limit(2).execute();
        octane.entityList("defects").get().execute();
    }

}

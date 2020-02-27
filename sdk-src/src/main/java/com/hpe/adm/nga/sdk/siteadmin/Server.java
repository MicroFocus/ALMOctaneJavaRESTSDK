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
package com.hpe.adm.nga.sdk.siteadmin;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.siteadmin.version.GetServerVersion;

/**
 * Creates an instance that represents the {@code server} API
 */
public class Server {

    // private members
    private final OctaneHttpClient octaneHttpClient;
    private final String urlDomain;

    /**
     * Creates a new Server object
     *
     * @param octaneHttpClient - Http Request Factory
     * @param siteAdminDomain  - siteAdmin Domain Name
     */
    public Server(OctaneHttpClient octaneHttpClient, String siteAdminDomain) {
        urlDomain = siteAdminDomain + "server/";
        this.octaneHttpClient = octaneHttpClient;
    }

    /**
     * Gets the API for server version
     *
     * @return the correct API
     */
    public GetServerVersion getServerVersion() {
        return new GetServerVersion(octaneHttpClient, urlDomain);
    }

}

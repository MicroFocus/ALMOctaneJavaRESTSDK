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
package com.hpe.adm.nga.sdk.siteadmin.version;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This returns an instance to get the version from the server
 * Corresponds to <a href="server/admin/server/version"></a>
 */
public class GetServerVersion {
    private final Logger logger = LoggerFactory.getLogger(GetServerVersion.class.getName());

    private final OctaneRequest octaneRequest;
    private final OctaneHttpClient octaneHttpClient;

    /**
     * Get the server version instance
     *
     * @param octaneHttpClient The client to use
     * @param urlDomain        The source url domain
     */
    public GetServerVersion(final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        this.octaneHttpClient = octaneHttpClient;
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain + "version");
    }

    /**
     * Gets the version as an object from the server
     *
     * @return The Version object
     */
    public Version execute() {
        final OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
        final OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        if (response.isSuccessStatusCode()) {
            final String json = response.getContent();
            logger.debug("Response_Json: {}", json);

            final JSONTokener tokener = new JSONTokener(json);
            final JSONObject jsonObj = new JSONObject(tokener);
            return new Version(jsonObj.getString("version"),
                    jsonObj.getString("build_date"),
                    jsonObj.getString("build_revision"),
                    jsonObj.getString("build_number"),
                    jsonObj.getString("display_version"));
        } else {
            throw new RuntimeException("Unexpected Version reponse!");
        }
    }
}

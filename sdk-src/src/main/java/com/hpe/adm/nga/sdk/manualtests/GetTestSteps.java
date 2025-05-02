/*
 * Copyright 2016-2025 Open Text.
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
package com.hpe.adm.nga.sdk.manualtests;

import com.hpe.adm.nga.sdk.manualtests.script.GetTestScriptModel;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class which represents test steps (script) from the API.  This returns the {@link GetTestScriptModel} which
 * can then be used to get the script details
 */
public class GetTestSteps {

    private final Logger logger = LoggerFactory.getLogger(GetTestSteps.class.getName());
    private final OctaneHttpClient octaneHttpClient;
    private final String scriptUrl;

    /**
     * Constructor
     *
     * @param octaneHttpClient the client to use
     * @param scriptUrl        The URL of the script including the test ID
     */
    GetTestSteps(OctaneHttpClient octaneHttpClient, String scriptUrl) {
        this.octaneHttpClient = octaneHttpClient;
        this.scriptUrl = scriptUrl;
    }

    /**
     * Calls the API and gets the returned JSON.  An error will be thrown if there was an issue
     *
     * @return The model that can then be queried
     */
    public GetTestScriptModel execute() {
        OctaneHttpRequest.GetOctaneHttpRequest getOctaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(scriptUrl);
        OctaneHttpResponse response = octaneHttpClient.execute(getOctaneHttpRequest);

        String json = "";
        if (response.isSuccessStatusCode()) {
            json = response.getContent();
            logger.debug("Response_Json: {}", json);
        }

        final JSONTokener tokener = new JSONTokener(json);
        final JSONObject jsonObj = new JSONObject(tokener);

        final EntityModel entityModel = ModelParser.getInstance().getEntityModel(jsonObj);
        return new GetTestScriptModel(entityModel);
    }
}

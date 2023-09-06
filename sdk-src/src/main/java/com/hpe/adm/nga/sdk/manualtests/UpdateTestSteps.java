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
package com.hpe.adm.nga.sdk.manualtests;

import com.hpe.adm.nga.sdk.manualtests.script.UpdateTestScriptModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;

/**
 * The class that can be used to update or create the test script for the given test
 */
public class UpdateTestSteps {
    private final OctaneHttpClient octaneHttpClient;
    private final String scriptUrl;
    private UpdateTestScriptModel updateTestScriptModel;

    /**
     * Constructor
     *
     * @param octaneHttpClient the client to use
     * @param scriptUrl        The URL of the script including the test ID
     */
    UpdateTestSteps(OctaneHttpClient octaneHttpClient, String scriptUrl) {
        this.octaneHttpClient = octaneHttpClient;
        this.scriptUrl = scriptUrl;
    }

    /**
     * An instance of the model to update
     *
     * @param updateTestScriptModel The instance of the model
     * @return This instance - to be used for chaining
     */
    public UpdateTestSteps testSteps(final UpdateTestScriptModel updateTestScriptModel) {
        this.updateTestScriptModel = updateTestScriptModel;
        return this;
    }

    /**
     * Carries out the execution of the creation or update.
     *
     * @return Whether the update/creation was successful
     */
    public boolean execute() {
        final String modelAsString = ModelParser.getInstance().getEntityJSONObject(updateTestScriptModel.getWrappedEntityModel()).toString();
        final OctaneHttpRequest.PutOctaneHttpRequest putOctaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(scriptUrl, OctaneHttpRequest.JSON_CONTENT_TYPE, modelAsString);
        return octaneHttpClient.execute(putOctaneHttpRequest).isSuccessStatusCode();
    }
}

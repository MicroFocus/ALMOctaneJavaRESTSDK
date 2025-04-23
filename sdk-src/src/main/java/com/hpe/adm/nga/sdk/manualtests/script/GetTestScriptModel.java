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
package com.hpe.adm.nga.sdk.manualtests.script;

import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.TestStepParser;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A model that represents a script that is received by the server.  According to the Octane documentation the Get and the Update
 * JSONs are different and therefore the correct model should be used according to the case
 */
public class GetTestScriptModel extends TypedEntityModel {

    /**
     * Constructor
     *
     * @param wrappedEntityModel The underlying model created from the JSON
     */
    public GetTestScriptModel(final EntityModel wrappedEntityModel) {
        super(wrappedEntityModel);
    }

    /**
     * Label: Creation Time
     * Description: The date and time the script was created, according to the ISO-8601 date format.
     *
     * @return The creation time of this script
     */
    @SuppressWarnings("unchecked")
    public java.time.ZonedDateTime getCreationTime() {
        final FieldModel<java.time.ZonedDateTime> creation_time = wrappedEntityModel.getValue("creation_time");
        return creation_time == null ? null : creation_time.getValue();
    }

    /**
     * Label: Last modified
     * Description: The date and time the script was last modified, according to the ISO-8601 date format.
     *
     * @return Last Modified date
     */
    @SuppressWarnings("unchecked")
    public java.time.ZonedDateTime getLastModified() {
        final FieldModel<java.time.ZonedDateTime> last_modified = wrappedEntityModel.getValue("last_modified");
        return last_modified == null ? null : last_modified.getValue();
    }

    /**
     * Returns the test steps from the server as objects.  This will only work for manual tests
     * Do NOT use for gherkin tests - they should be edited using the string and with a Gherkin parser
     *
     * @return List
     */
    public List<AbstractTestStep> getTestStepsAsObjects() {
        return TestStepParser.parseTestSteps(getTestStepsAsString());
    }

    /**
     * Returns the test steps from the server as the native string
     *
     * @return native string
     */
    public String getTestStepsAsString() {
        final StringFieldModel scriptFieldModel = (StringFieldModel) wrappedEntityModel.getValue("script");
        return scriptFieldModel.getValue();
    }
}

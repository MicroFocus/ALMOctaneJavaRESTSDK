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
package com.hpe.adm.nga.sdk.manualtests.script;

import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A model that represents a script that can be updated.  According to the Octane documentation the Get and the Update
 * JSONs are different and therefore the correct model should be used according to the case
 */
public final class UpdateTestScriptModel extends TypedEntityModel {

    /**
     * Represents the revision type (see Octane test script documentation)
     */
    public enum TestScriptRevisionType {
        MINOR("Minor"), MAJOR("Major");

        private final String type;

        TestScriptRevisionType(String type) {
            this.type = type;
        }
    }

    /**
     * Sets the test steps to update represented as objects
     * Do NOT use for gherkin tests - they should be uploaded as a simple string with the Gherkin script
     *
     * @param testSteps The list of objects
     * @return The model for chaining
     */
    public UpdateTestScriptModel setTestSteps(List<AbstractTestStep> testSteps) {
        if (testSteps != null) {
            final StringBuilder stepBuilder = new StringBuilder();
            testSteps.forEach(testStep ->
                    stepBuilder.append(testStep.getTestStepString())
            );
            setTestSteps(stepBuilder.toString());
        }

        return this;
    }

    /**
     * Sets the test steps to update represented as a simple JSON string (needs to confirm to the format as seen in the
     * documentation)
     *
     * @param testSteps The string of the JSON
     * @return The model for chaining
     */
    public final UpdateTestScriptModel setTestSteps(String testSteps) {
        wrappedEntityModel.setValue(new StringFieldModel("script", testSteps));
        return this;
    }

    /**
     * Sets the comment for the script
     *
     * @param comment The comment
     * @return The model for chaining
     */
    public final UpdateTestScriptModel setComment(String comment) {
        wrappedEntityModel.setValue(new StringFieldModel("comment", comment));
        return this;
    }

    /**
     * Sets the revision type for the script
     *
     * @param testScriptRevisionType The revision type
     * @return The model for chaining
     */
    public final UpdateTestScriptModel setRevisionType(final TestScriptRevisionType testScriptRevisionType) {
        wrappedEntityModel.setValue(new StringFieldModel("revision_type", testScriptRevisionType.type));
        return this;
    }
}

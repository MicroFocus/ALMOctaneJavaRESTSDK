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
package com.hpe.adm.nga.sdk.manualtests.teststeps;

import org.json.JSONObject;

/**
 * Represents a test step which can be either normal, call or validation
 */
public abstract class AbstractTestStep {

    private final String testStep;
    private final boolean escapeTestStep;

    /**
     * Constructor
     *
     * @param testStep the string of the step
     */
    AbstractTestStep(final String testStep) {
        this(testStep, true);
    }

    /**
     * Constructor
     *
     * @param testStep       the string of the step
     * @param escapeTestStep Whether the step should be escaped
     */
    AbstractTestStep(final String testStep, final boolean escapeTestStep) {
        this.testStep = testStep;
        this.escapeTestStep = escapeTestStep;
    }

    /**
     * Returns the step as a string including the prefix
     *
     * @return the script
     */
    public final String getTestStepString() {
        return getTestStepPrefix() + escapeMetaCharacters(testStep) + "\n";
    }

    /**
     * Returns the prefix of the step
     *
     * @return The prefix
     */
    abstract String getTestStepPrefix();

    /**
     * The step without the prefix
     *
     * @return The step
     */
    public final String getTestStep() {
        return testStep;
    }

    private String escapeMetaCharacters(String inputString) {
        if (!escapeTestStep) {
            return inputString;
        }
        final String quote = JSONObject.quote(inputString);

        return quote.substring(1, quote.length() - 1).replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
    }

}

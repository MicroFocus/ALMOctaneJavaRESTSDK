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

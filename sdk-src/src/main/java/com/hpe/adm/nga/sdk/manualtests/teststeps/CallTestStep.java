/**
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
package com.hpe.adm.nga.sdk.manualtests.teststeps;

/**
 * A test step representing calling another test (see Octane documentation)
 * Note: The called ID is not checked by the SDK - the REST API will fail if there is a problem
 */
public class CallTestStep extends AbstractTestStep {
    private final String testId;
    private final String callStepString;

    /**
     * The prefix for the call step
     */
    public static final String PREFIX = "- @";

    /**
     * Callstep has two parts - the ID of the called test and an optional step
     *
     * @param testId   the id to call
     * @param testStep the optional step
     */
    public CallTestStep(final String testId, final String testStep) {
        super(testId.concat(" ").concat(testStep), false);
        this.testId = testId;
        this.callStepString = testStep;
    }

    @Override
    String getTestStepPrefix() {
        return PREFIX;
    }

    /**
     * The test ID
     *
     * @return id
     */
    public String getTestId() {
        return testId;
    }

    /**
     * The string of the optional call step
     *
     * @return the string after the test id
     */
    public String getCallStepString() {
        return callStepString;
    }
}

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

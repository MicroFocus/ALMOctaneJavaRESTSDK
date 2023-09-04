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

import java.util.ArrayList;
import java.util.List;

/**
 * Used to parse the step to see what type of test it is and to create the correct object instance
 */
public class TestStepParser {

    /**
     * Create a list of objects representing the steps
     *
     * @param testScript The script to parse
     * @return The list of objects
     */
    public static List<AbstractTestStep> parseTestSteps(String testScript) {
        final String[] steps = testScript.split("\n");
        final List<AbstractTestStep> abstractTestSteps = new ArrayList<>(steps.length);
        for (final String step : steps) {
            if (step.startsWith(ValidationTestStep.PREFIX)) {
                abstractTestSteps.add(new ValidationTestStep(step.substring(ValidationTestStep.PREFIX.length())));
            } else if (step.startsWith(CallTestStep.PREFIX)) {
                abstractTestSteps.add(
                        new CallTestStep(
                                step.substring(CallTestStep.PREFIX.length(), step.indexOf(" ", CallTestStep.PREFIX.length())),
                                step.substring(step.indexOf(" ", CallTestStep.PREFIX.length()) + 1))
                );
            } else if (step.startsWith(TestStep.PREFIX)) {
                abstractTestSteps.add(new TestStep(step.substring(TestStep.PREFIX.length())));
            }
        }

        return abstractTestSteps;
    }

}

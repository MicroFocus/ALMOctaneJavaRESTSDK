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
package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.TestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.ValidationTestStep;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;

import java.util.*;

/**
 * Demonstrates how to manipulate tests using the SDK
 */
@SuppressWarnings("ALL")
public class TestExample {

    private final Octane octane = null;
    private final EntityList entityList = octane.entityList("manual_tests");

    public void createManualTest() {
        final FieldModel<String> nameField = new StringFieldModel("name", "newTest");
        // phase is a required field
        final FieldModel<EntityModel> phaseField =
                new ReferenceFieldModel("phase",
                        new EntityModel(new HashSet<>(Arrays.asList(
                                new StringFieldModel("type", "phase"),
                                // value will change depending on your instance
                                new StringFieldModel("id", "9001")
                        ))));

        final Set<FieldModel> testEntityFields = new HashSet<>(Arrays.asList(nameField, phaseField));
        final EntityModel testEntityModel = new EntityModel(testEntityFields);

        // see EntityExample for more details here
        final String createdTestId = entityList.create()
                .entities(new ArrayList<>(Collections.singletonList(testEntityModel)))
                .execute()
                // should be one test or else there is a problem
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not create test!"))
                .getId();

        /*
        If you have generated entities then the above can be replaced by:
          final TestManualEntityModel testManualEntityModel = new TestManualEntityModel("newTest", Phases.TestManualPhase.NEW);
        final String createdTestId =  octane
                .entityList(TestManualEntityList.class)
                .create()
                .entities(Collections.singleton(testManualEntityModel))
                .execute()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not create test!"))
                .getId();
         */

        // see TestTestSteps test for more usage examples

        final List<AbstractTestStep> testStepList = new ArrayList<>();
        testStepList.add(new TestStep("Step 1"));
        testStepList.add(new ValidationTestStep("Validating Step 1"));
    }
}

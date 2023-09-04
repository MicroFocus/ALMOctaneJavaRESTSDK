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
package com.hpe.adm.nga.sdk.tests.tests;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.manualtests.TestStepList;
import com.hpe.adm.nga.sdk.manualtests.UpdateTestSteps;
import com.hpe.adm.nga.sdk.manualtests.script.GetTestScriptModel;
import com.hpe.adm.nga.sdk.manualtests.script.UpdateTestScriptModel;
import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.CallTestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.TestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.ValidationTestStep;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TestTestSteps extends TestBase {

    @Test
    public void testTestStepCreation() {
        final String createdTestId = createTest();
        // this will fail if incorrect
        final EntityList.TestEntities test_manual = (EntityList.TestEntities) octane.entityList("manual_tests").at(createdTestId);
        final TestStepList testSteps = test_manual.getTestSteps();
        updateTestSteps(testSteps);

        final GetTestScriptModel getTestScriptModel = testSteps.get().execute();
        final String asString = getTestScriptModel.getTestStepsAsString();
        Assert.assertEquals("Returned test script is not correct!", "- Step 1\n- ?Validating Step 1\n- @2002 link to test\n", asString);

        final List<AbstractTestStep> testStepsAsObjects = getTestScriptModel.getTestStepsAsObjects();
        Assert.assertNotNull("test script as objects is null", testStepsAsObjects);
        Assert.assertEquals("test script as objects is not length 3", 3, testStepsAsObjects.size());

        final TestStep testStep = (TestStep) testStepsAsObjects.get(0);
        Assert.assertEquals("TestStep object string is incorrect", "Step 1", testStep.getTestStep());

        final ValidationTestStep validationTestStep = (ValidationTestStep) testStepsAsObjects.get(1);
        Assert.assertEquals("ValidationTestStep object string is incorrect", "Validating Step 1", validationTestStep.getTestStep());

        final CallTestStep callTestStep = (CallTestStep) testStepsAsObjects.get(2);
        Assert.assertEquals("CallTestStep object string is incorrect", "link to test", callTestStep.getCallStepString());
        Assert.assertEquals("CallTestStep object test id is incorrect", "2002", callTestStep.getTestId());
    }

    private void updateTestSteps(TestStepList testSteps) {
        final UpdateTestSteps update = testSteps.update();

        final List<AbstractTestStep> testStepList = new ArrayList<>();
        testStepList.add(new TestStep("Step 1"));
        testStepList.add(new ValidationTestStep("Validating Step 1"));
        testStepList.add(new CallTestStep("2002", "link to test"));

        // check test steps are created
        Assert.assertTrue("Script not created correctly!", update.testSteps(new UpdateTestScriptModel().setTestSteps(testStepList)).execute());
    }

    @Test
    public void testIncorrectNonTypedTestStepCreation() {
        final EntityList.Entities not_test_manual = octane.entityList("not_test_manual").at("1001");
        Assert.assertFalse("Entities is not the right type!", not_test_manual instanceof EntityList.TestEntities);
    }

    private String createTest() {
        final EntityList entityList = octane.entityList("manual_tests");
        Collection<EntityModel> phases = octane.entityList("phases").get().execute();
        EntityModel phase = phases
                .stream()
                .filter(entityModel -> entityModel.getValue("id").getValue().equals("phase.test_manual.new"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not get phase for test!"));

        final FieldModel<String> nameField = new StringFieldModel("name", "newTest");
        // phase is a required field
        final FieldModel<EntityModel> phaseField =
                new ReferenceFieldModel("phase", phase);

        final Set<FieldModel> testEntityFields = new HashSet<>(Arrays.asList(nameField, phaseField));
        final EntityModel testEntityModel = new EntityModel(testEntityFields);

        // see EntityExample for more details here
        return entityList.create()
                .entities(new ArrayList<>(Collections.singletonList(testEntityModel)))
                .execute()
                // should be one test or else there is a problem
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not create test!"))
                .getId();
    }
}

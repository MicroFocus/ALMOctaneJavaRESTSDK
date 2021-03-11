/*
 * © Copyright 2016-2021 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.tests.tests;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.manualtests.TestStepList;
import com.hpe.adm.nga.sdk.manualtests.UpdateTestSteps;
import com.hpe.adm.nga.sdk.manualtests.script.GetTestScriptModel;
import com.hpe.adm.nga.sdk.manualtests.script.UpdateTestScriptModel;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TestGherkinScript extends TestBase {

    @Test
    public void testTestStepCreation() {
        final String createdTestId = createTest();
        // this will fail if incorrect
        final EntityList.TestEntities test_manual = (EntityList.TestEntities) octane.entityList("gherkin_tests").at(createdTestId);
        final TestStepList testSteps = test_manual.getTestSteps();
        updateTestSteps(testSteps);

        final GetTestScriptModel getTestScriptModel = testSteps.get().execute();
        final String asString = getTestScriptModel.getTestStepsAsString();
        Assert.assertTrue("Returned test script is not correct!", asString.startsWith("#Auto generated Octane revision tag\n@TID")
                && asString.endsWith("\nFeature:\n"));
    }

    private void updateTestSteps(TestStepList testSteps) {
        final UpdateTestSteps update = testSteps.update();
        // check test steps are created
        Assert.assertTrue("Script not created correctly!", update.testSteps(new UpdateTestScriptModel().setTestSteps("#Auto generated Octane revision tag\n@TID2010REV0.2.0\nFeature:\n")).execute());
    }

    @Test
    public void testIncorrectNonTypedTestStepCreation() {
        final EntityList.Entities not_test_manual = octane.entityList("not_gherkin").at("1001");
        Assert.assertFalse("Entities is not the right type!", not_test_manual instanceof EntityList.TestEntities);
    }

    private String createTest() {
        final EntityList entityList = octane.entityList("gherkin_tests");
        Collection<EntityModel> phases = octane.entityList("phases").get().execute();
        EntityModel phase = phases
                .stream()
                .filter(entityModel -> entityModel.getValue("id").getValue().equals("phase.gherkin_test.new"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not get phase for test!"));

        final FieldModel<String> nameField = new StringFieldModel("name", "newGherkinTest");
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

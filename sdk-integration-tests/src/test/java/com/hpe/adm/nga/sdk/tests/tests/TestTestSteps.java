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
        Assert.assertTrue("Returned test script is not correct!", asString.equals("- Step 1\n- ?Validating Step 1\n- @2002 link to test\n"));

        final List<AbstractTestStep> testStepsAsObjects = getTestScriptModel.getTestStepsAsObjects();
        Assert.assertNotNull("test script as objects is null", testStepsAsObjects);
        Assert.assertTrue("test script as objects is not length 3", testStepsAsObjects.size() == 3);

        final TestStep testStep = (TestStep) testStepsAsObjects.get(0);
        Assert.assertTrue("TestStep object string is incorrect", testStep.getTestStep().equals("Step 1"));

        final ValidationTestStep validationTestStep = (ValidationTestStep) testStepsAsObjects.get(1);
        Assert.assertTrue("ValidationTestStep object string is incorrect", validationTestStep.getTestStep().equals("Validating Step 1"));

        final CallTestStep callTestStep = (CallTestStep) testStepsAsObjects.get(2);
        Assert.assertTrue("CallTestStep object string is incorrect", callTestStep.getCallStepString().equals("link to test"));
        Assert.assertTrue("CallTestStep object test id is incorrect", callTestStep.getTestId().equals("2002"));
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

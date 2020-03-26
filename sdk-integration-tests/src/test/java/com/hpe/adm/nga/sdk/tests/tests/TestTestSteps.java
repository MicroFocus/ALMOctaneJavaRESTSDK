package com.hpe.adm.nga.sdk.tests.tests;

import com.hpe.adm.nga.sdk.entities.TestEntityList;
import com.hpe.adm.nga.sdk.entities.TestManualEntityList;
import com.hpe.adm.nga.sdk.enums.Phases;
import com.hpe.adm.nga.sdk.manualtests.TestStepList;
import com.hpe.adm.nga.sdk.manualtests.UpdateTestSteps;
import com.hpe.adm.nga.sdk.manualtests.script.GetTestScriptModel;
import com.hpe.adm.nga.sdk.manualtests.script.UpdateTestScriptModel;
import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.CallTestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.TestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.ValidationTestStep;
import com.hpe.adm.nga.sdk.model.TestManualEntityModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestTestSteps extends TestBase {

    @Test
    public void testTestStepCreation() {
        final String createdTestId = "2005";//createTest();
        final TestEntityList testEntityList = octane.entityList(TestEntityList.class);
        final TestEntityList.TestEntities at = testEntityList.at(createdTestId);
        final TestStepList testSteps = at.getTestSteps();
        final UpdateTestSteps update = testSteps.update();

        final List<AbstractTestStep> testStepList = new ArrayList<>();
        testStepList.add(new TestStep("Step 1"));
        testStepList.add(new ValidationTestStep("Validating Step 1"));
        testStepList.add(new CallTestStep("2002", "link to test"));

        // check test steps are created
        Assert.assertTrue("Script not created correctly!", update.testSteps(new UpdateTestScriptModel().setTestSteps(testStepList)).execute());

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

    private String createTest() {
        final TestManualEntityModel testManualEntityModel = new TestManualEntityModel("test1", Phases.TestManualPhase.NEW);
        return octane
                .entityList(TestManualEntityList.class)
                .create()
                .entities(Collections.singleton(testManualEntityModel))
                .execute()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not create test!"))
                .getId();
    }
}

package com.hpe.adm.nga.sdk.manualtests.teststeps;

public class TestStep extends AbstractTestStep {
    public TestStep(final String testStep) {
        super(testStep);
    }

    @Override
    String getTestStepPrefix() {
        return "- ";
    }
}

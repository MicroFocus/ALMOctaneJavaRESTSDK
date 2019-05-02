package com.hpe.adm.nga.sdk.manualtests.teststeps;

public class ValidationTestStep extends AbstractTestStep {
    public ValidationTestStep(final String validationTestStep) {
        super(validationTestStep);
    }

    @Override
    String getTestStepPrefix() {
        return "- ?";
    }
}

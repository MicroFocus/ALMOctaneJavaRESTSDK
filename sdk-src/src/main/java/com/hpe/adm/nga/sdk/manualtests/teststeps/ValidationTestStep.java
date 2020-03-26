package com.hpe.adm.nga.sdk.manualtests.teststeps;

public class ValidationTestStep extends AbstractTestStep {
    public static final String PREFIX = "- ?";

    public ValidationTestStep(final String validationTestStep) {
        super(validationTestStep);
    }

    @Override
    String getTestStepPrefix() {
        return PREFIX;
    }
}

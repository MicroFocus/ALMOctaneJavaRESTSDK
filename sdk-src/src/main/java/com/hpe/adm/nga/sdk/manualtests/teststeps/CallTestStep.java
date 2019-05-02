package com.hpe.adm.nga.sdk.manualtests.teststeps;

public class CallTestStep extends AbstractTestStep {
    public CallTestStep(final String testId) {
        super(testId, false);
    }

    @Override
    String getTestStepPrefix() {
        return "@";
    }
}

package com.hpe.adm.nga.sdk.manualtests.teststeps;

public class CallTestStep extends AbstractTestStep {
    private final String testId;
    private final String callStepString;

    public static final String PREFIX = "- @";

    public CallTestStep(final String testId, final String testStep) {
        super(testId.concat(" ").concat(testStep), false);
        this.testId = testId;
        this.callStepString = testStep;
    }

    @Override
    String getTestStepPrefix() {
        return PREFIX;
    }

    public String getTestId() {
        return testId;
    }

    public String getCallStepString() {
        return callStepString;
    }
}

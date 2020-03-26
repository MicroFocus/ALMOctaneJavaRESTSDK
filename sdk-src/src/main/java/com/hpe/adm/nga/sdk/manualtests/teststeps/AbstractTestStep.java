package com.hpe.adm.nga.sdk.manualtests.teststeps;

import org.json.JSONObject;

public abstract class AbstractTestStep {

    private final String testStep;
    private final boolean escapeTestStep;

    AbstractTestStep(final String testStep) {
        this(testStep, true);
    }

    AbstractTestStep(final String testStep, final boolean escapeTestStep) {
        this.testStep = testStep;
        this.escapeTestStep = escapeTestStep;
    }

    public final String getTestStepString() {
        return getTestStepPrefix() + escapeMetaCharacters(testStep) + "\n";
    }

    abstract String getTestStepPrefix();

    public final String getTestStep() {
        return testStep;
    }

    private String escapeMetaCharacters(String inputString) {
        if (!escapeTestStep) {
            return inputString;
        }
        final String quote = JSONObject.quote(inputString);

        return quote.substring(1, quote.length() - 1).replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
    }

}

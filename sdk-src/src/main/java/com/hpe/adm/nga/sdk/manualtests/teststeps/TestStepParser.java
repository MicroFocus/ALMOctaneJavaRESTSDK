package com.hpe.adm.nga.sdk.manualtests.teststeps;

import java.util.ArrayList;
import java.util.List;

public class TestStepParser {

    public static List<AbstractTestStep> parseTestSteps(String testScript) {
        final String[] steps = testScript.split("\n");
        final List<AbstractTestStep> abstractTestSteps = new ArrayList<>(steps.length);
        for (final String step : steps) {
            if (step.startsWith(ValidationTestStep.PREFIX)) {
                abstractTestSteps.add(new ValidationTestStep(step.substring(ValidationTestStep.PREFIX.length())));
            } else if (step.startsWith(CallTestStep.PREFIX)) {
                abstractTestSteps.add(
                        new CallTestStep(
                                step.substring(CallTestStep.PREFIX.length(), step.indexOf(" ", CallTestStep.PREFIX.length())),
                                step.substring(step.indexOf(" ", CallTestStep.PREFIX.length()) + 1))
                );
            } else if (step.startsWith(TestStep.PREFIX)) {
                abstractTestSteps.add(new TestStep(step.substring(TestStep.PREFIX.length())));
            }
        }

        return abstractTestSteps;
    }

}

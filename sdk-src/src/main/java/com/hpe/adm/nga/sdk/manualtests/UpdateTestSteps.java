package com.hpe.adm.nga.sdk.manualtests;

import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;

import java.util.List;

public class UpdateTestSteps {
    private final OctaneHttpClient octaneHttpClient;
    private final String scriptUrl;
    private List<AbstractTestStep> testSteps;

    UpdateTestSteps(OctaneHttpClient octaneHttpClient, String scriptUrl) {
        this.octaneHttpClient = octaneHttpClient;
        this.scriptUrl = scriptUrl;
    }

    public UpdateTestSteps testSteps(List<AbstractTestStep> testSteps) {
        this.testSteps = testSteps;

        return this;
    }

    public boolean execute() {
        final StringBuilder stepBuilder = new StringBuilder();
        testSteps.forEach(testStep ->
                stepBuilder.append(testStep.getTestStepString())
        );

        final String scriptFormat = String.format("{\"script\":\"%s\",\"comment\":\"\",\"revision_type\":\"Minor\"}", stepBuilder.toString());

        final OctaneHttpRequest.PutOctaneHttpRequest putOctaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(scriptUrl, OctaneHttpRequest.JSON_CONTENT_TYPE, scriptFormat);

        return octaneHttpClient.execute(putOctaneHttpRequest).isSuccessStatusCode();
    }
}

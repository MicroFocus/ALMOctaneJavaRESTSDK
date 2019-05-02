package com.hpe.adm.nga.sdk.manualtests;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

public class TestStepList {

    private final OctaneHttpClient octaneHttpClient;
    private final String scriptUrl;

    public TestStepList(OctaneHttpClient octaneHttpClient, String baseDomain, final String testId) {
        this.octaneHttpClient = octaneHttpClient;
        this.scriptUrl = baseDomain + "tests/" + testId + "/script";
    }

    public GetTestSteps get() {
        return new GetTestSteps(octaneHttpClient, scriptUrl);
    }

    public UpdateTestSteps update() {
        return new UpdateTestSteps(octaneHttpClient, scriptUrl);
    }

}

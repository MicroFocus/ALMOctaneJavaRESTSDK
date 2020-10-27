/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.manualtests;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * An object that enables getting or updating (or creating) test steps using the SDK
 *
 * @see <a href="https://admhelp.microfocus.com/octane/en/15.0.40/Online/Content/API/Create_Manual_Test.htm#mt-item-3">Create Manual Test Steps</a>
 */
public class TestStepList {

    private final OctaneHttpClient octaneHttpClient;
    private final String scriptUrl;

    /**
     * The constructor.  The test id HAS to refer to a manual_test otherwise the API call will fail
     *
     * @param octaneHttpClient The client to use
     * @param baseDomain       The base domain from which the URL will be constructed
     * @param testId           Has to be a manual test ID otherwise the API will fail
     */
    public TestStepList(OctaneHttpClient octaneHttpClient, String baseDomain, final String testId) {
        this.octaneHttpClient = octaneHttpClient;
        this.scriptUrl = baseDomain + "tests/" + testId + "/script";
    }

    /**
     * Returns an instance of {@link GetTestSteps} in order to see test steps
     *
     * @return the instance
     */
    public GetTestSteps get() {
        return new GetTestSteps(octaneHttpClient, scriptUrl);
    }

    /**
     * Returns an instance of {@link UpdateTestSteps} in order to update or create test steps
     *
     * @return the instance
     */
    public UpdateTestSteps update() {
        return new UpdateTestSteps(octaneHttpClient, scriptUrl);
    }
}

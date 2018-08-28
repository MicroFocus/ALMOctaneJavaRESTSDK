/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microfocus.adm.almoctane.sdk.tests.authentication;

import com.microfocus.adm.almoctane.sdk.exception.OctaneException;
import com.microfocus.adm.almoctane.sdk.model.EntityModel;
import com.microfocus.adm.almoctane.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 *
 * Created by leufl on 14/11/2016.
 */
public class TestAuthentication extends TestBase {

    @Test
    public void testSignOut() throws Exception {
        octane.signOut();
        try {
            Collection<EntityModel> defectModel = octane.entityList("stories").get().execute();
            Assert.fail("Sign out failed.");
        } catch (OctaneException e) {}
    }
}

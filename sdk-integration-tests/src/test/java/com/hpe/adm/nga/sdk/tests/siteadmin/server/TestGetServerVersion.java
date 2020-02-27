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
package com.hpe.adm.nga.sdk.tests.siteadmin.server;

import com.hpe.adm.nga.sdk.siteadmin.version.Version;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Used to test the {@link com.hpe.adm.nga.sdk.siteadmin.version.GetServerVersion} API
 */
public class TestGetServerVersion extends TestBase {

    /**
     * Tests getting the server version
     */
    @Test
    public void testGetServerVersion() {
        final Version serverVersion = siteAdmin.getServer().getServerVersion().execute();
        Assert.assertNotNull(serverVersion);
        Assert.assertFalse(serverVersion.getVersion().isEmpty());
    }
}

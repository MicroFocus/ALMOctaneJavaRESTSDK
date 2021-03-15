/*
 * © Copyright 2016-2021 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.tests.admin;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.utils.AuthenticationUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.HttpUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestWorkSpaceAdmin {

    static {
        // for local execution
        if (System.getProperty("should.set.proxy") == null) {
            System.setProperty("should.set.proxy", "true");
        }
    }

    private static String url;
    private static Authentication authentication;
    private static String sharedSpaceId;
    private static String workspaceId;

    @BeforeClass
    public static void init() {
        HttpUtils.SetSystemKeepAlive(false);
        HttpUtils.SetSystemProxy();

        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        url = configuration.getString("sdk.url");
        authentication = AuthenticationUtils.getAuthentication();
        sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        workspaceId = configuration.getString("sdk.workspaceId");
    }

    @Test
    public void testGetWorkSpaceAdmin() {
        Octane octane = ContextUtils.getContextWorkspace(url, authentication, sharedSpaceId, String.valueOf(Octane.NO_WORKSPACE_ID));

        final OctaneCollection<EntityModel> entityModels = octane.entityList(Octane.NO_ENTITY).get().execute();
        Assert.assertTrue(entityModels.size() > 0);
        Assert.assertEquals("workspace", entityModels.iterator().next().getValue("type").getValue());
    }

    @Test
    public void testGetWorkSpaceUsers() {
        Octane octane = ContextUtils.getContextWorkspace(url, authentication, sharedSpaceId, workspaceId);
        final OctaneCollection<EntityModel> entityModels = octane.entityList("workspace_users").get().execute();
        Assert.assertTrue(entityModels.size() > 0);
        Assert.assertEquals("workspace_user", entityModels.iterator().next().getValue("type").getValue());
    }
}

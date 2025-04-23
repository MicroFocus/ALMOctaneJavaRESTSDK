/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
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

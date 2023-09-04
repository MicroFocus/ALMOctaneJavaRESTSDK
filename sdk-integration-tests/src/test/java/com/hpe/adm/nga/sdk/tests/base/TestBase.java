/**
 * Copyright 2016-2023 Open Text.
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
package com.hpe.adm.nga.sdk.tests.base;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.SiteAdmin;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.utils.AuthenticationUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.HttpUtils;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * Created by Guy Guetta on 12/04/2016.
 */
public class TestBase {
    protected static Octane octane;
    protected static SiteAdmin siteAdmin;
    protected static String entityName = "";
    private static String entityTypeOld = "";
    protected static EntityList entityList;
    protected static Metadata metadata;

    static {
        // for local execution
        if (System.getProperty("should.set.proxy") == null) {
            System.setProperty("should.set.proxy", "true");
        }
    }

    @BeforeClass
    public static void init() {
        HttpUtils.SetSystemKeepAlive(false);
        HttpUtils.SetSystemProxy();

        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authentication authentication = AuthenticationUtils.getAuthentication();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        octane = ContextUtils.getContextWorkspace(url, authentication, sharedSpaceId, workspaceId);
        siteAdmin = ContextUtils.getContextSiteAdmin(url, authentication);
        metadata = octane.metadata();
    }

    @Before
    public void before() {
        if (!entityName.equals(entityTypeOld)) {
            entityList = octane.entityList(entityName);
            entityTypeOld = entityName;
        }
    }
}

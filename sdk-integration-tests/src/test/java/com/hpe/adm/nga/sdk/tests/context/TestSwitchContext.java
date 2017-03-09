/*
 *
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.hpe.adm.nga.sdk.tests.context;


import com.hpe.adm.nga.sdk.AuthenticationProvider;
import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.XmlAuthenticationProvider;
import org.junit.Test;

import java.util.Collection;

/**
 *
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class TestSwitchContext {

    protected static AuthenticationProvider authenticationProvider = new XmlAuthenticationProvider();

//    @Test
    public void contextSiteAdmin() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");

        Octane octane = ContextUtils.getContextSiteAdmin(url, authenticationProvider);

        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("shared_spaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextSharedSpace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");

        Octane octane = ContextUtils.getContextSharedSpace(url, authenticationProvider, sharedSpaceId);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("workspaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextWorkspace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        Octane octane = ContextUtils.getContextWorkspace(url, authenticationProvider, sharedSpaceId, workspaceId);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("list_nodes");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

}

package com.hpe.adm.nga.sdk.tests.context;


import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.utils.AuthenticationUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import org.junit.Test;

import java.util.Collection;

/**
 *
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class TestSwitchContext {

    //    @Test
    public void contextSiteAdmin() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authentication authentication = AuthenticationUtils.getAuthentication();

        Octane octane = ContextUtils.getContextSiteAdmin(url, authentication);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("shared_spaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextSharedSpace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authentication authentication = AuthenticationUtils.getAuthentication();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");

        Octane octane = ContextUtils.getContextSharedSpace(url, authentication, sharedSpaceId);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("workspaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextWorkspace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authentication authentication = AuthenticationUtils.getAuthentication();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        Octane octane = ContextUtils.getContextWorkspace(url, authentication, sharedSpaceId, workspaceId);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("list_nodes");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

}
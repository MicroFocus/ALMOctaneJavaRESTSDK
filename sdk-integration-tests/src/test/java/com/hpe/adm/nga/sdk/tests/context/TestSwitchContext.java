package com.hpe.adm.nga.sdk.tests.context;


import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.utils.AuthorisationUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import org.junit.Test;

import java.util.Collection;

/**
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class TestSwitchContext {

//    @Test
    public void contextSiteAdmin() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();

        Octane octane = ContextUtils.getContextSiteAdmin(url, authorisation);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("shared_spaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextSharedSpace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");

        Octane octane = ContextUtils.getContextSharedSpace(url, authorisation, sharedSpaceId);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("workspaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextWorkspace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        Octane octane = ContextUtils.getContextWorkspace(url, authorisation, sharedSpaceId, workspaceId);
        Metadata metadata = octane.metadata();

        EntityList entities = octane.entityList("list_nodes");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

}

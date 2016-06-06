package com.hpe.adm.nga.sdk.tests.context;


import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGA;
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
public class SwitchContext {

//    @Test
    public void contextSiteAdmin() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();

        NGA nga = ContextUtils.getContextSiteAdmin(url, authorisation);
        Metadata metadata = nga.metadata();

        EntityList entities = nga.entityList("shared_spaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextSharedSpace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");

        NGA nga = ContextUtils.getContextSharedSpace(url, authorisation, sharedSpaceId);
        Metadata metadata = nga.metadata();

        EntityList entities = nga.entityList("workspaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextWorkspace() throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        NGA nga = ContextUtils.getContextWorkspace(url, authorisation, sharedSpaceId, workspaceId);
        Metadata metadata = nga.metadata();

        EntityList entities = nga.entityList("list_nodes");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

}

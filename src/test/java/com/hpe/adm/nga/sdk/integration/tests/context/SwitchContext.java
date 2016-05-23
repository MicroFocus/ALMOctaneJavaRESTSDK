package test.java.com.hpe.adm.nga.sdk.integration.tests.context;


import main.java.com.hpe.adm.nga.sdk.EntityList;
import main.java.com.hpe.adm.nga.sdk.NGA;
import main.java.com.hpe.adm.nga.sdk.metadata.Metadata;
import main.java.com.hpe.adm.nga.sdk.model.EntityModel;

import test.java.com.hpe.adm.nga.sdk.integration.utils.ContextUtils;

import org.junit.Test;

import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class SwitchContext {

    @Test
    public void contextSiteAdmin() throws Exception {
        String url = ResourceBundle.getBundle("configuration").getString("url");
        String username = ResourceBundle.getBundle("configuration").getString("username");
        String password = ResourceBundle.getBundle("configuration").getString("password");

        NGA nga = ContextUtils.getContextSiteAdmin(url, username, password);
        Metadata metadata = nga.metadata();

        EntityList entities = nga.entityList("shared_spaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextSharedSpace() throws Exception {
        String url = ResourceBundle.getBundle("configuration").getString("url");
        String username = ResourceBundle.getBundle("configuration").getString("username");
        String password = ResourceBundle.getBundle("configuration").getString("password");
        String sharedSpaceId = ResourceBundle.getBundle("configuration").getString("sharedSpaceId");

        NGA nga = ContextUtils.getContextSharedSpace(url, username, password, sharedSpaceId);
        Metadata metadata = nga.metadata();

        EntityList entities = nga.entityList("workspaces");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

    @Test
    public void contextWorkspace() throws Exception {
        String url = ResourceBundle.getBundle("configuration").getString("url");
        String username = ResourceBundle.getBundle("configuration").getString("username");
        String password = ResourceBundle.getBundle("configuration").getString("password");
        String sharedSpaceId = ResourceBundle.getBundle("configuration").getString("sharedSpaceId");
        String workspaceId = ResourceBundle.getBundle("configuration").getString("workspaceId");

        NGA nga = ContextUtils.getContextWorkspace(url, username, password, sharedSpaceId, workspaceId);
        Metadata metadata = nga.metadata();

        EntityList entities = nga.entityList("list_nodes");
        Collection<EntityModel> colEntityList = entities.get().execute();
    }

}

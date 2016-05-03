package com.hpe.adm.nga.sdk.tests.base;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.HttpUtils;
import com.sun.javafx.runtime.SystemProperties;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ResourceBundle;

/**
 * Created by Guy Guetta on 12/04/2016.
 */
public class TestBase {
    protected static NGA nga;
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

        String url = ResourceBundle.getBundle("configuration").getString("url");
        String username = ResourceBundle.getBundle("configuration").getString("username");
        String password = ResourceBundle.getBundle("configuration").getString("password");
        String sharedSpaceId = ResourceBundle.getBundle("configuration").getString("sharedSpaceId");
        String workspaceId = ResourceBundle.getBundle("configuration").getString("workspaceId");

        nga = ContextUtils.getContextWorkspace(url, username, password, sharedSpaceId, workspaceId);
        metadata = nga.metadata();
    }

    @Before
    public void before() {
        if (!entityName.equals(entityTypeOld)) {
            entityList = nga.entityList(entityName);
            entityTypeOld = entityName;
        }
    }

}

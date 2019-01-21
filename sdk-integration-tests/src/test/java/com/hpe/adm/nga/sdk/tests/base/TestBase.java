package com.hpe.adm.nga.sdk.tests.base;

import com.hpe.adm.nga.sdk.Octane;
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
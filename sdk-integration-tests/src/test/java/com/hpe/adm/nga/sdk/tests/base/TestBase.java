package com.hpe.adm.nga.sdk.tests.base;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.utils.AuthorisationUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.HttpUtils;
import org.junit.Before;
import org.junit.BeforeClass;

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

        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        nga = ContextUtils.getContextWorkspace(url, authorisation, sharedSpaceId, workspaceId);
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

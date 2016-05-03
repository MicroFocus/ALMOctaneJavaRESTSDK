package com.hpe.adm.nga.sdk.tests.base;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.utils.HttpUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * Created by Guy Guetta on 12/04/2016.
 */
public class TestBase {

    final static String MY_APP_ID = "rest@hpe.com";
    final static String MY_APP_SECRET = "Welcome1";

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
        try {
            HttpUtils.SetSystemKeepAlive(false);
            HttpUtils.SetSystemProxy();

            nga = new NGA.Builder(
                    new BasicAuthorisation() {
                        @Override
                        public String getUsername() {

                            return MY_APP_ID;
                        }

                        @Override
                        public String getPassword() {

                            return MY_APP_SECRET;
                        }

                    }
            ).Server("http://localhost.emea.hpqcorp.net:8080").sharedSpace(2001).workSpace(1002).build();
            metadata = nga.metadata();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Before
    public void before() {
        if (!entityName.equals(entityTypeOld)) {
            entityList = nga.entityList(entityName);
            entityTypeOld = entityName;
        }
    }

}

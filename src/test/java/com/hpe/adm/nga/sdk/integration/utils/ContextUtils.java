package test.java.com.hpe.adm.nga.sdk.integration.utils;

import main.java.com.hpe.adm.nga.sdk.NGA;
import main.java.com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import org.json.JSONException;
import sun.security.krb5.RealmException;

import java.io.IOException;

/**
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class ContextUtils {

    public static NGA getContextSiteAdmin(String url, String userName, String password) {
        return getContext(url, userName, password, "", "");
    }

    public static NGA getContextSharedSpace(String url, String userName, String password, String sharedSpaceId) {
        return getContext(url, userName, password, sharedSpaceId, "");
    }

    public static NGA getContextWorkspace(String url, String userName, String password, String sharedSpaceId, String workspaceId) {
        return getContext(url, userName, password, sharedSpaceId, workspaceId);
    }

    private static NGA getContext(String url, String userName, String password, String sharedSpaceId, String workspaceId) {
        NGA nga = null;
        try {
            NGA.Builder builder = new NGA.Builder(
                    new BasicAuthorisation() {
                        @Override
                        public String getUsername() {
                            return userName;
                        }

                        @Override
                        public String getPassword() {
                            return password;
                        }
                    }
            ).Server(url);

            if (!sharedSpaceId.isEmpty()) {
                builder = builder.sharedSpace(Long.valueOf(sharedSpaceId));
            }

            if (!workspaceId.isEmpty()) {
                builder = builder.workSpace(Long.valueOf(workspaceId));
            }

            nga = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return nga;
    }
}

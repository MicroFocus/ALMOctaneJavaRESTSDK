package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authorisation.Authorisation;

/**
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class ContextUtils {

    public static Octane getContextSiteAdmin(String url, Authorisation authorisation) {
        return getContext(url, authorisation, "", "");
    }

    public static Octane getContextSharedSpace(String url, Authorisation authorisation, String sharedSpaceId) {
        return getContext(url, authorisation, sharedSpaceId, "");
    }

    public static Octane getContextWorkspace(String url, Authorisation authorisation, String sharedSpaceId, String workspaceId) {
        return getContext(url, authorisation, sharedSpaceId, workspaceId);
    }

    private static Octane getContext(String url, Authorisation authorisation, String sharedSpaceId, String workspaceId) {
        Octane octane = null;
        try {
//            Octane.Builder builder = new Octane.Builder(new UserAuthorisation(userName, password)).Server(url);
            Octane.Builder builder = new Octane.Builder(authorisation).Server(url);

            if (!sharedSpaceId.isEmpty()) {
                builder = builder.sharedSpace(Long.valueOf(sharedSpaceId));
            }

            if (!workspaceId.isEmpty()) {
                builder = builder.workSpace(Long.valueOf(workspaceId));
            }

            octane = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return octane;
    }
}

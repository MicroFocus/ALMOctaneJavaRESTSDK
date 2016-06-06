package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.authorisation.Authorisation;

/**
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class ContextUtils {

    public static NGA getContextSiteAdmin(String url, Authorisation authorisation) {
        return getContext(url, authorisation, "", "");
    }

    public static NGA getContextSharedSpace(String url, Authorisation authorisation, String sharedSpaceId) {
        return getContext(url, authorisation, sharedSpaceId, "");
    }

    public static NGA getContextWorkspace(String url, Authorisation authorisation, String sharedSpaceId, String workspaceId) {
        return getContext(url, authorisation, sharedSpaceId, workspaceId);
    }

    private static NGA getContext(String url, Authorisation authorisation, String sharedSpaceId, String workspaceId) {
        NGA nga = null;
        try {
//            NGA.Builder builder = new NGA.Builder(new UserAuthorisation(userName, password)).Server(url);
            NGA.Builder builder = new NGA.Builder(authorisation).Server(url);

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

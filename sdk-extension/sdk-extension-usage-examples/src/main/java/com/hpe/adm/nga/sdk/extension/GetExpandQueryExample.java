package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.extension.entities.ExtendedGetEntities;
import com.hpe.adm.nga.sdk.model.EntityModel;

import java.util.*;

public class GetExpandQueryExample {

    public static void main(String[] args) {

        OctaneExtensionUtil.enable();

        Authentication authentication = new SimpleUserAuthentication(
                OctaneConnectionConstants.username,
                OctaneConnectionConstants.password
        );

        Octane octane =
                new Octane.Builder(authentication)
                        .Server(OctaneConnectionConstants.urlDomain)
                        .sharedSpace(OctaneConnectionConstants.sharedspaceId)
                        .workSpace(OctaneConnectionConstants.workspaceId).build();


        Map<String, Set<String>> expandMap = new HashMap<>();
        expandMap.put("fields", new HashSet<>());
        expandMap.get("fields").add("author");

        final Collection<EntityModel> defects =
                ((ExtendedGetEntities) octane.entityList("defects").get())
                        .expand(expandMap)
                        .execute();

        Util.printEntities(defects);
    }
}
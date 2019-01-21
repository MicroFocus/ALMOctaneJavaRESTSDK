package com.hpe.adm.nga.sdk.extension.stringquery;


import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.extension.OctaneConnectionConstants;
import com.hpe.adm.nga.sdk.extension.StringQuery;
import com.hpe.adm.nga.sdk.extension.Util;
import com.hpe.adm.nga.sdk.model.EntityModel;

import java.util.Collection;

public class StringQueryExample {

    public static void main(String[] args) {

        Authentication authentication
                = new SimpleUserAuthentication(OctaneConnectionConstants.username, OctaneConnectionConstants.password);

        Octane octane =
                new Octane.Builder(authentication)
                        .Server(OctaneConnectionConstants.urlDomain)
                        .sharedSpace(OctaneConnectionConstants.sharedspaceId)
                        .workSpace(OctaneConnectionConstants.workspaceId)
                        .build();

        //Example: query to return stories or defects
        StringQuery stringQuery = StringQuery.fromString("subtype EQ 'story' || subtype EQ 'defect'");

        //Fetch defects as an example a print them to the console
        Collection<EntityModel> entities = octane
                .entityList("work_items")
                .get()
                .query(stringQuery)
                .execute();

        Util.printEntities(entities);
    }

}
/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.extension.stringquery;


import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.OctaneWrapper;
import com.hpe.adm.nga.sdk.authentication.ExplicitAuthentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.extension.OctaneConnectionConstants;
import com.hpe.adm.nga.sdk.extension.StringQuery;
import com.hpe.adm.nga.sdk.extension.Util;
import com.hpe.adm.nga.sdk.model.EntityModel;

import java.util.Collection;

public class StringQueryExample {

    public static void main(String[] args) {

        ExplicitAuthentication authentication
                = new SimpleUserAuthentication(OctaneConnectionConstants.username, OctaneConnectionConstants.password);

        Octane octane =
                new OctaneWrapper.Builder().authentication(authentication)
                        .Server(OctaneConnectionConstants.urlDomain)
                        .build()
                        .octane()
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
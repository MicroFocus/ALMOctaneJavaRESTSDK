/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        expandMap.put("fields", new HashSet<String>());
        expandMap.get("fields").add("author");

        final Collection<EntityModel> defects =
                ((ExtendedGetEntities) octane.entityList("defects").get())
                        .expand(expandMap)
                        .execute();

        System.out.println(defects.size());
    }
}
/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.extension.entities;

import com.hpe.adm.nga.sdk.entities.get.GetEntities;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extension for {@link GetEntities}
 */
public class ExtendedGetEntities extends GetEntities {

    protected ExtendedGetEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super(octaneHttpClient, urlDomain);
    }

    /**
     * Allows setting the expand query param of the request, controls what fields are returned for each relation field of base entity
     * @param relationExpandFields map of relation field name to fields of the related entity type
     * @return GetEntities Object with new expand parameter
     */
    public GetEntities expand(Map<String, Set<String>> relationExpandFields) {
        String expandQueryString =
                relationExpandFields.keySet().stream().map(relationFieldName -> {

                    String relationFieldExpandValue = "";
                    if(relationExpandFields.get(relationFieldName).size() > 0){
                        relationFieldExpandValue =
                                relationExpandFields.get(relationFieldName)
                                        .stream()
                                        .collect(Collectors.joining(","));

                        relationFieldExpandValue = "fields=" + relationFieldExpandValue;
                    }

                    return relationFieldName + "{" + relationFieldExpandValue +"}";
                }).collect(Collectors.joining(","));

        octaneRequest.getOctaneUrl().setParam("expand", expandQueryString);

        return this;
    }

}

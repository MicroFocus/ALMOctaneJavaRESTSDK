/**
 * Copyright 2016-2023 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
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

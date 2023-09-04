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
package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * A helper for updating entities
 */
final class UpdateHelper {

    private static final UpdateHelper INSTANCE = new UpdateHelper();

    private UpdateHelper() {
    }

    static UpdateHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. UpdateEntities Request execution with json data 2. Parse response to
     * a new EntityModel object
     *
     * @param entityModel the entitymodel
     * @param octaneRequest the octane request
     */
    EntityModel updateEntityModel(EntityModel entityModel, OctaneRequest octaneRequest) {
        JSONObject objBase = ModelParser.getInstance().getEntityJSONObject(entityModel, true);
        String jsonEntityModel = objBase.toString();

        OctaneHttpRequest octaneHttpRequest =
                new OctaneHttpRequest.PutOctaneHttpRequest(octaneRequest.getFinalRequestUrl(),
                        OctaneHttpRequest.JSON_CONTENT_TYPE,
                        jsonEntityModel)
                        .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
        octaneHttpRequest.setHeaders(octaneRequest.getHeaders());

        return octaneRequest.getEntityResponse(octaneHttpRequest);
    }

    /**
     * 1. Request UpdateEntities Execution
     * 2. Parse response to a new Collection object
     *
     * @param entityModels the entitymodel
     * @param octaneRequest the octane request
     */
    OctaneCollection<EntityModel> updateEntityModels(Collection<EntityModel> entityModels, OctaneRequest octaneRequest)  {
        OctaneCollection<EntityModel> newEntityModels;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels, true);
        String jsonEntityModel = objBase.toString();

        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(
                octaneRequest.getFinalRequestUrl(),
                OctaneHttpRequest.JSON_CONTENT_TYPE, jsonEntityModel)
                .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
        octaneHttpRequest.setHeaders(octaneRequest.getHeaders());

        newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);

        //TODO: partial support exception handling

        return newEntityModels;
    }

}

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
package com.microfocus.adm.almoctane.sdk.entities.update;

import com.microfocus.adm.almoctane.sdk.entities.OctaneCollection;
import com.microfocus.adm.almoctane.sdk.model.EntityModel;
import com.microfocus.adm.almoctane.sdk.model.ModelParser;
import com.microfocus.adm.almoctane.sdk.network.OctaneHttpRequest;
import com.microfocus.adm.almoctane.sdk.network.OctaneRequest;
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
        newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);

        //TODO: partial support exception handling

        return newEntityModels;
    }

}
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
package com.hpe.adm.nga.sdk.entities.create;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * A helper for creating entities
 */
final class CreateHelper {

    private static final CreateHelper INSTANCE = new CreateHelper();

    private CreateHelper() {
    }

    static CreateHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. build Entity Json Object  2. Post
     * Request execution with json data 3. Parse response to a new
     * object
     * @param entityModels the collection of entitymodels
     * @param octaneRequest the octane request
     */
    OctaneCollection<EntityModel> createEntities(Collection<EntityModel> entityModels, OctaneRequest octaneRequest)  {

        OctaneCollection<EntityModel> newEntityModels;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels);
        String strJsonEntityModel = objBase.toString();

        OctaneHttpRequest octaneHttpRequest =
                new OctaneHttpRequest.PostOctaneHttpRequest(octaneRequest.getFinalRequestUrl(), OctaneHttpRequest.JSON_CONTENT_TYPE, strJsonEntityModel)
                        .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
        octaneHttpRequest.setHeaders(octaneRequest.getHeaders());
        newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);

        //TODO: partial support

        return newEntityModels;
    }
}

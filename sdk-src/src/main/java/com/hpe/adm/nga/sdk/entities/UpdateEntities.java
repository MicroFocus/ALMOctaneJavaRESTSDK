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

package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * This class hold the UpdateEntities objects and serve all functions concern to
 * REST put.
 */
public class UpdateEntities extends OctaneRequest {

    private Collection<EntityModel> entityModels = null;

    UpdateEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super (octaneHttpClient, urlDomain);
    }

    /**
     * 1. Request UpdateEntities Execution
     * 2. Parse response to a new Collection object
     */
    public OctaneCollection execute() throws RuntimeException {

        OctaneCollection newEntityModels = null;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels, true);
        String jsonEntityModel = objBase.toString();
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(
                    getFinalRequestUrl(),
                    OctaneHttpRequest.JSON_CONTENT_TYPE, jsonEntityModel)
                    .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = getEntitiesResponse(octaneHttpRequest);

        } catch (Exception e) {

            handleException(e, true);
        }

        return newEntityModels;

    }

    /**
     * UpdateEntities query parameters
     *
     * @param query - new query parameters
     * @return UpdateEntities object with new query parameters
     */
    public UpdateEntities query(Query query) {
        getOctaneUrl().setDqlQueryParam(query);
        return this;
    }

    /**
     * Set new entities collection
     *
     * @param entities The entities which will be updated
     * @return create Object with new entities collection
     */
    public UpdateEntities entities(Collection<EntityModel> entities) {
        entityModels = entities;
        return this;
    }

}

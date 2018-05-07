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

package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

import java.util.Collection;

/**
 * This class hold the UpdateEntities objects and serve all functions concern to
 * REST put.
 */
public class UpdateEntities {

    private Collection<EntityModel> entityModels = null;
    private final OctaneRequest octaneRequest;

    public UpdateEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * 1. Request UpdateEntities Execution
     * 2. Parse response to a new Collection object
     * @return a collection of entities models that have been updated
     */
    public OctaneCollection<EntityModel> execute()  {
        return UpdateHelper.getInstance().updateEntityModels(entityModels, octaneRequest);
    }

    /**
     * UpdateEntities query parameters
     *
     * @param query - new query parameters
     * @return UpdateEntities object with new query parameters
     */
    public UpdateEntities query(Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
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

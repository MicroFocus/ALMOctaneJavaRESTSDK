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

package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

/**
 * This class hold the GetEntities objects and serve all functions concern to REST
 * GetEntities.
 */
public class GetEntities {

    protected final OctaneRequest octaneRequest;

    public GetEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * 1. Request GetEntities Execution <br>
     * 2. Parse response to a new Collection object
     * @return a collection of entities models that have been retrieved
     */
    public OctaneCollection<EntityModel> execute()  {
        return GetHelper.getInstance().getEntityModels(octaneRequest);
    }

    /**
     * Add Fields parameters
     *
     * @param fields An array of fields that will be part of the HTTP Request
     * @return GetEntities Object with new Fields parameters
     */
    public GetEntities addFields(String... fields) {
        octaneRequest.getOctaneUrl().addFieldsParam(fields);
        return this;
    }

    /**
     * Add Limit parameter
     *
     * @param limit The entity limit
     * @return GetEntities Object with new limit parameter
     */
    public GetEntities limit(int limit) {
        octaneRequest.getOctaneUrl().setLimitParam(limit);
        return this;
    }

    /**
     * Add offset parameter
     *
     * @param offset The entity limit offset
     * @return GetEntities Object with new offset parameter
     */
    public GetEntities offset(int offset) {
        octaneRequest.getOctaneUrl().setOffsetParam(offset);
        return this;
    }

    /**
     * Add OrderBy parameters
     *
     * @param orderBy The string which determines how the entities should be ordered
     * @param asc     - true=ascending/false=descending
     * @return GetEntities Object with new OrderBy parameters
     */
    public GetEntities addOrderBy(String orderBy, boolean asc) {
        octaneRequest.getOctaneUrl().setOrderByParam(orderBy, asc);
        return this;
    }

    /**
     * @param query The query to use
     * @return The object
     */
    public GetEntities query(Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return this;
    }
}

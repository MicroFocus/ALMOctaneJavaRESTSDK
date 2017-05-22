/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

/**
 * This class hold the DeleteEntities objects and serve all functions concern to
 * REST delete.
 */
public class DeleteEntities extends OctaneRequest {

    DeleteEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super(octaneHttpClient, urlDomain);
    }

    /**
     * Execute a DeleteEntities request
     *
     * @return null
     */
    public Collection<EntityModel> execute() throws RuntimeException {

        Collection<EntityModel> deletedEntityModels = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(getFinalRequestUrl());
            deletedEntityModels = getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {

            handleException(e, false);
        }

        return deletedEntityModels;

    }

    /**
     * UpdateEntities DeleteEntities with new Query parameters
     *
     * @param query - new Query parameters
     * @return a DeleteEntities Object with new Query parameters
     */
    public DeleteEntities query(Query query) {
        getOctaneUrl().setDqlQueryParam(query);
        return this;
    }
}

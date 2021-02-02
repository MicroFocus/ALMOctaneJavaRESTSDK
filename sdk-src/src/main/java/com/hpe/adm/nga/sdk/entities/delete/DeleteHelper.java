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
package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * A helper for deleting entities
 */
final class DeleteHelper {

    private static final DeleteHelper INSTANCE = new DeleteHelper();

    private DeleteHelper() {
    }

    static DeleteHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     *
     * @param octaneRequest the octane request
     */
    EntityModel deleteEntityModel(OctaneRequest octaneRequest)  {
        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
        octaneHttpRequest.setHeaders(octaneRequest.getHeaders());
        return octaneRequest.getEntityResponse(octaneHttpRequest);
    }

    /**
     * Execute a DeleteEntities request
     *
     * @param octaneRequest the octane request
     * @return null
     */
    OctaneCollection<EntityModel> deleteEntityModels(OctaneRequest octaneRequest)  {
        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
        octaneHttpRequest.setHeaders(octaneRequest.getHeaders());
        return octaneRequest.getEntitiesResponse(octaneHttpRequest);
    }
}

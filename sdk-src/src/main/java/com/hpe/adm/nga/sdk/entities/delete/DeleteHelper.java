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
package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

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
    EntityModel deleteEntityModel(OctaneRequest octaneRequest) throws RuntimeException {
        EntityModel newEntityModel = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
            newEntityModel = octaneRequest.getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }
        return newEntityModel;
    }

    /**
     * Execute a DeleteEntities request
     *
     * @param octaneRequest the octane request
     * @return null
     */
    Collection<EntityModel> deleteEntityModels(OctaneRequest octaneRequest) throws RuntimeException {
        Collection<EntityModel> deletedEntityModels = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
            deletedEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }

        return deletedEntityModels;
    }
}

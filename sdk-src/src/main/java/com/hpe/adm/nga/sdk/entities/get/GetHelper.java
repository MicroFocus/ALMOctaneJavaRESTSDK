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

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

/**
 * A helper for getting entities
 */
final class GetHelper {

    private static final GetHelper INSTANCE = new GetHelper();

    private GetHelper() {
    }

    static GetHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. Request GetEntities Execution
     * 2. Parse response to a new Collection object
     */
    final Collection<EntityModel> getEntityModels(final OctaneRequest octaneRequest) throws RuntimeException {
        Collection<EntityModel> newEntityModels = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl()).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }
        return newEntityModels;
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     */
    final EntityModel getEntityModel(final OctaneRequest octaneRequest) throws RuntimeException {
        EntityModel newEntityModel = null;
        try {
            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl())
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModel = octaneRequest.getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }

        return newEntityModel;
    }
}

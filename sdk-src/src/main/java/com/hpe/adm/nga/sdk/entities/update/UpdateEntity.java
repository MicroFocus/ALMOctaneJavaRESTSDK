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

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * This class hold the UpdateEntities object of one entity
 */
public class UpdateEntity {

    private EntityModel entityModel;
    private final OctaneRequest octaneRequest;

    public UpdateEntity(OctaneHttpClient octaneHttpClient, String urlDomain, String entityId) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * 1. UpdateEntities Request execution with json data 2. Parse response to
     * a new EntityModel object
     * @return the updated entity model from the server
     */
    public EntityModel execute() {
        return UpdateHelper.getInstance().updateEntityModel(entityModel, octaneRequest);
    }

    /**
     * set a new entity for updating
     *
     * @param entityModel The entity model to be updated
     * @return an update object with new entity
     */
    public UpdateEntity entity(EntityModel entityModel) {
        this.entityModel = entityModel;
        return this;
    }
}

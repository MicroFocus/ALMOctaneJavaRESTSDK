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

import com.hpe.adm.nga.sdk.APIMode;
import com.hpe.adm.nga.sdk.entities.get.GetEntity;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * This class hold the DeleteEntities object of one entity
 */
public class DeleteEntity {
    private final OctaneRequest octaneRequest;

    public DeleteEntity(OctaneHttpClient octaneHttpClient, String urlDomain, String entityId) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * 1. DeleteEntity Request execution with json data 2. Parse response to a
     * @return new EntityModel object for the entity that's been deleted
     */
    public EntityModel execute()  {
        return DeleteHelper.getInstance().deleteEntityModel(octaneRequest);
    }

    /**
     * 1. DeleteEntity Request execution with json data , using a custom api mode
     * 2. Parse response to a new EntityModel object
     * @return EntityModel from the server
     */
    public EntityModel execute(APIMode apiMode)  {
        octaneRequest.addHeader(apiMode);
        EntityModel result = execute();
        octaneRequest.removeHeader(apiMode);
        return result;
    }

    /**
     * Append a new path element, for special cases
     * @param path The new path section to be added
     * @return this
     */
    public DeleteEntity addPath(String path) {
        octaneRequest.getOctaneUrl().getPaths().add(path);
        return this;
    }
}

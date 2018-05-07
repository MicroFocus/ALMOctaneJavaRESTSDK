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

package com.hpe.adm.nga.sdk.entities.create;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

/**
 * This class hold the CreateEntities objects and serve all functions concern to
 * REST Post.
 */
public class CreateEntities {

    private Collection<EntityModel> entityModels = null;
    private final OctaneRequest octaneRequest;

    public CreateEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * 1. build Entity Json Object  2. Post
     * Request execution with json data 3. Parse response to a new
     * object
     * @return a collection of entities models that have been created
     */
    public OctaneCollection<EntityModel> execute()  {
        return CreateHelper.getInstance().createEntities(entityModels, octaneRequest);
    }

    /**
     * Set new entities collection
     *
     * @param entities The entities which will be created
     * @return create Object with new entities collection
     */
    public CreateEntities entities(Collection<EntityModel> entities) {

        entityModels = entities;
        return this;
    }
}

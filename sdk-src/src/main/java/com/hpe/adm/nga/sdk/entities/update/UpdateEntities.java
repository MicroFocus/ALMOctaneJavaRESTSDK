/**
 * Copyright 2016-2023 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.APIMode;
import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.entities.get.GetEntities;
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
     * 1. Request UpdateEntities Execution <br> using a custom api mode value
     * 2. Parse response to a new Collection object
     * @return a collection of entities models that have been retrieved
     */
    public OctaneCollection<EntityModel> execute(APIMode apiMode)  {
        octaneRequest.addHeader(apiMode);
        OctaneCollection<EntityModel> result = execute();
        octaneRequest.removeHeader(apiMode);
        return result;
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

    /**
     * Append a new path element, for special cases
     * @param path The new path section to be added
     * @return this
     */
    public UpdateEntities addPath(String path) {
        octaneRequest.getOctaneUrl().getPaths().add(path);
        return this;
    }
}

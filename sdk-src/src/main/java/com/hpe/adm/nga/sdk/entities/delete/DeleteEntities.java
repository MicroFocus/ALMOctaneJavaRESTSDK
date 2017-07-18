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

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

/**
 * This class hold the DeleteEntities objects and serve all functions concern to
 * REST delete.
 */
public class DeleteEntities {
    private final OctaneRequest octaneRequest;

    public DeleteEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * Execute a DeleteEntities request
     *
     * @return collection of deleted entities
     */
    public OctaneCollection execute() throws RuntimeException {
        return DeleteHelper.getInstance().deleteEntityModels(octaneRequest);
    }

    /**
     * UpdateEntities DeleteEntities with new Query parameters
     *
     * @param query - new Query parameters
     * @return a DeleteEntities Object with new Query parameters
     */
    public DeleteEntities query(Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return this;
    }
}

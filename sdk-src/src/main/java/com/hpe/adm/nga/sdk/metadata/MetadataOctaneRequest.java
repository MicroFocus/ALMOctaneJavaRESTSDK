/*
 * Copyright 2016-2025 Open Text.
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
package com.hpe.adm.nga.sdk.metadata;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;

/**
 * Abstract request used for fetching metadata
 */
abstract class MetadataOctaneRequest {

    protected static final String JSON_DATA_FIELD_NAME = "data";
    protected static final String LOGGER_RESPONSE_JSON_FORMAT = "Response_Json: {}";
    protected final OctaneRequest octaneRequest;
    protected final OctaneHttpClient octaneHttpClient;

    protected MetadataOctaneRequest(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
        this.octaneHttpClient = octaneHttpClient;
    }

    final MetadataOctaneRequest addEntities(String queryFieldName, String... entities) {
        if (entities == null || entities.length == 0) {
            return this;
        } else {
            Query.QueryBuilder builder = null;
            for (String entity : entities) {
                if (builder == null) {
                    builder = Query.statement(queryFieldName, QueryMethod.EqualTo, entity);
                } else {
                    builder = builder.or(Query.statement(queryFieldName, QueryMethod.EqualTo, entity));
                }
            }
            octaneRequest.getOctaneUrl().setDqlQueryParam(builder.build());
            return this;
        }
    }

}

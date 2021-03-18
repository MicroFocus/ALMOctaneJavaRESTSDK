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
package com.hpe.adm.nga.sdk.metadata;

import com.hpe.adm.nga.sdk.APIMode;
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

    final MetadataOctaneRequest apiMode(final APIMode apiMode) {
        octaneRequest.addHeader(apiMode);
        return this;
    }

}
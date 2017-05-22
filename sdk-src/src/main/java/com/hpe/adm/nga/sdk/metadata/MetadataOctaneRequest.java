/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.hpe.adm.nga.sdk.metadata;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;

/**
 * Created by brucesp on 15-May-17.
 */
abstract class MetadataOctaneRequest extends OctaneRequest {

    protected static final String JSON_DATA_FIELD_NAME = "data";
    protected static final String LOGGER_RESPONSE_JSON_FORMAT = "Response_Json: %s";

    MetadataOctaneRequest(OctaneHttpClient octaneHttpClient, String urlDomain){
        super(octaneHttpClient, urlDomain);
    }

    final <T extends MetadataOctaneRequest> T addEntities(String queryFieldName, String... entities) {
        if (entities == null) {
            return (T) this;
        }
        if (entities.length == 0) {
            return (T) this;
        }

        Query.QueryBuilder builder = Query.statement(queryFieldName, QueryMethod.EqualTo, "sdksdksdk");
        for (String entity : entities){
            builder = builder.or(Query.statement(queryFieldName, QueryMethod.EqualTo, entity));
        }
        return (T) this;
    }
}

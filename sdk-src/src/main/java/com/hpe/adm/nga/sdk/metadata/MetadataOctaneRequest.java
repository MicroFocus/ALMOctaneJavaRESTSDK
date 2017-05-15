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

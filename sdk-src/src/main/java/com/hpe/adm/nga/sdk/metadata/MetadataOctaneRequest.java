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
    protected static final String LOGGER_RESPONSE_JSON_FORMAT = "Response_Json: %s";
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
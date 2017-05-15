package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

/**
 * This class hold the GetEntities objects and serve all functions concern to REST
 * GetEntities.
 */
public class GetEntities extends OctaneRequest {
    // Public
    GetEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super(octaneHttpClient, urlDomain);
    }

    /**
     * 1. Request GetEntities Execution
     * 2. Parse response to a new Collection object
     */
    public Collection<EntityModel> execute() throws RuntimeException {
        Collection<EntityModel> newEntityModels = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(getFinalRequestUrl()).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {
            handleException(e, false);
        }
        return newEntityModels;
    }

    /**
     * Add Fields parameters
     *
     * @param fields An array of fields that will be part of the HTTP Request
     * @return GetEntities Object with new Fields parameters
     */
    public GetEntities addFields(String... fields) {
        getOctaneUrl().addFieldsParam(fields);
        return this;
    }

    /**
     * Add Limit parameter
     *
     * @param limit The entity limit
     * @return GetEntities Object with new limit parameter
     */
    public GetEntities limit(int limit) {
        getOctaneUrl().setLimitParam(limit);
        return this;
    }

    /**
     * Add offset parameter
     *
     * @param offset The entity limit offset
     * @return GetEntities Object with new offset parameter
     */
    public GetEntities offset(int offset) {
        getOctaneUrl().setOffsetParam(offset);
        return this;
    }

    /**
     * Add OrderBy parameters
     *
     * @param orderBy The string which determines how the entities should be ordered
     * @param asc     - true=ascending/false=descending
     * @return GetEntities Object with new OrderBy parameters
     */
    public GetEntities addOrderBy(String orderBy, boolean asc) {
        getOctaneUrl().setOrderByParam(orderBy, asc);
        return this;
    }

    /**
     * @param query The query to use
     * @return The object
     */
    public GetEntities query(Query query) {
        getOctaneUrl().setDqlQueryParam(query);
        return this;
    }
}

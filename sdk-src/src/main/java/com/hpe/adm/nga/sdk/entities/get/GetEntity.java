package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * This class hold the GetEntities object of one entity
 */
public class GetEntity {

    private final OctaneRequest octaneRequest;

    public GetEntity(OctaneHttpClient octaneHttpClient, String urlDomain, String entityId) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     * @return EntityModel from the server
     */
    public EntityModel execute()  {
        return GetHelper.getInstance().getEntityModel(octaneRequest);
    }

    /**
     * Set Fields Parameters
     *
     * @param fields An array or comma separated list of fields to be retrieved
     * @return a new GetEntities object with new Fields Parameters
     */
    public GetEntity addFields(String... fields) {
        octaneRequest.getOctaneUrl().addFieldsParam(fields);
        return this;
    }
}

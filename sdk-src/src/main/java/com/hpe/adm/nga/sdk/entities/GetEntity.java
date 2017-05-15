package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * This class hold the GetEntities object of one entity
 */
public class GetEntity extends OctaneRequest {

    GetEntity(OctaneHttpClient octaneHttpClient, String urlDomain, int iEntityId) {
        super(octaneHttpClient, urlDomain, iEntityId);
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     */
    public EntityModel execute() throws RuntimeException {

        EntityModel newEntityModel = null;
        try {
            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.GetOctaneHttpRequest(getFinalRequestUrl())
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModel = getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {

            handleException(e, false);
        }

        return newEntityModel;

    }

    /**
     * Set Fields Parameters
     *
     * @param fields An array or comma separated list of fields to be retrieved
     * @return a new GetEntities object with new Fields Parameters
     */
    public GetEntity addFields(String... fields) {
        getOctaneUrl().addFieldsParam(fields);
        return this;
    }
}

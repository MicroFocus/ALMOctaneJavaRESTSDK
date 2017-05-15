package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * This class hold the DeleteEntities object of one entity
 */
public class DeleteEntity extends OctaneRequest {

    DeleteEntity(OctaneHttpClient octaneHttpClient, String urlDomain, int iEntityId) {
        super(octaneHttpClient, urlDomain, iEntityId);
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     */
    public EntityModel execute() throws RuntimeException {
        EntityModel newEntityModel = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(getFinalRequestUrl());
            newEntityModel = getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {

            handleException(e, false);
        }
        return newEntityModel;

    }
}

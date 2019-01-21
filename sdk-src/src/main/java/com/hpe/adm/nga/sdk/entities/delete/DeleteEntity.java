package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * This class hold the DeleteEntities object of one entity
 */
public class DeleteEntity {
    private final OctaneRequest octaneRequest;

    public DeleteEntity(OctaneHttpClient octaneHttpClient, String urlDomain, String entityId) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * @return new EntityModel object for the entity that's been deleted
     */
    public EntityModel execute()  {
        return DeleteHelper.getInstance().deleteEntityModel(octaneRequest);
    }
}

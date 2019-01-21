package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * This class hold the UpdateEntities object of one entity
 */
public class UpdateEntity {

    private EntityModel entityModel;
    private final OctaneRequest octaneRequest;

    public UpdateEntity(OctaneHttpClient octaneHttpClient, String urlDomain, String entityId) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * 1. UpdateEntities Request execution with json data 2. Parse response to
     * a new EntityModel object
     * @return the updated entity model from the server
     */
    public EntityModel execute() {
        return UpdateHelper.getInstance().updateEntityModel(entityModel, octaneRequest);
    }

    /**
     * set a new entity for updating
     *
     * @param entityModel The entity model to be updated
     * @return an update object with new entity
     */
    public UpdateEntity entity(EntityModel entityModel) {
        this.entityModel = entityModel;
        return this;
    }
}

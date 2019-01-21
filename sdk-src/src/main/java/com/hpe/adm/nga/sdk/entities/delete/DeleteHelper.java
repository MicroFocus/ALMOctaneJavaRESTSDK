package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * A helper for deleting entities
 */
final class DeleteHelper {

    private static final DeleteHelper INSTANCE = new DeleteHelper();

    private DeleteHelper() {
    }

    static DeleteHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     *
     * @param octaneRequest the octane request
     */
    EntityModel deleteEntityModel(OctaneRequest octaneRequest)  {
        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
        return octaneRequest.getEntityResponse(octaneHttpRequest);
    }

    /**
     * Execute a DeleteEntities request
     *
     * @param octaneRequest the octane request
     * @return null
     */
    OctaneCollection<EntityModel> deleteEntityModels(OctaneRequest octaneRequest)  {
        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
        return octaneRequest.getEntitiesResponse(octaneHttpRequest);
    }
}

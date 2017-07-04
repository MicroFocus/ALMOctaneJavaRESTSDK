package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

/**
 * Created by brucesp on 27-Jun-17.
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
     * @param octaneRequest
     */
    EntityModel deleteEntityModel(OctaneRequest octaneRequest) throws RuntimeException {
        EntityModel newEntityModel = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
            newEntityModel = octaneRequest.getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }
        return newEntityModel;
    }

    /**
     * Execute a DeleteEntities request
     *
     * @param octaneRequest
     * @return null
     */
    Collection<EntityModel> deleteEntityModels(OctaneRequest octaneRequest) throws RuntimeException {
        Collection<EntityModel> deletedEntityModels = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
            deletedEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }

        return deletedEntityModels;
    }
}

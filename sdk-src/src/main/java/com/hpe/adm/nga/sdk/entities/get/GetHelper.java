package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

/**
 * Created by brucesp on 27-Jun-17.
 */
final class GetHelper {

    private static final GetHelper INSTANCE = new GetHelper();

    private GetHelper() {
    }

    static GetHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. Request GetEntities Execution
     * 2. Parse response to a new Collection object
     */
    final Collection<EntityModel> getEntityModels(final OctaneRequest octaneRequest) throws RuntimeException {
        Collection<EntityModel> newEntityModels = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl()).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }
        return newEntityModels;
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     */
    final EntityModel getEntityModel(final OctaneRequest octaneRequest) throws RuntimeException {
        EntityModel newEntityModel = null;
        try {
            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl())
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModel = octaneRequest.getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }

        return newEntityModel;
    }
}

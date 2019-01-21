package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * A helper for getting entities
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
    final OctaneCollection<EntityModel> getEntityModels(final OctaneRequest octaneRequest)  {
        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl()).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
        return octaneRequest.getEntitiesResponse(octaneHttpRequest);
    }

    /**
     * 1. GetEntities Request execution with json data 2. Parse response to a
     * new EntityModel object
     */
    final EntityModel getEntityModel(final OctaneRequest octaneRequest)  {
        OctaneHttpRequest octaneHttpRequest =
                new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl())
                        .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
        return octaneRequest.getEntityResponse(octaneHttpRequest);
    }

}
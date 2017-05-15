package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;

/**
 * This class hold the DeleteEntities objects and serve all functions concern to
 * REST delete.
 */
public class DeleteEntities extends OctaneRequest {

    DeleteEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super(octaneHttpClient, urlDomain);
    }

    /**
     * Execute a DeleteEntities request
     *
     * @return null
     */
    public Collection<EntityModel> execute() throws RuntimeException {

        Collection<EntityModel> deletedEntityModels = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(getFinalRequestUrl());
            deletedEntityModels = getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {

            handleException(e, false);
        }

        return deletedEntityModels;

    }

    /**
     * UpdateEntities DeleteEntities with new Query parameters
     *
     * @param query - new Query parameters
     * @return a DeleteEntities Object with new Query parameters
     */
    public DeleteEntities query(Query query) {
        getOctaneUrl().setDqlQueryParam(query);
        return this;
    }
}

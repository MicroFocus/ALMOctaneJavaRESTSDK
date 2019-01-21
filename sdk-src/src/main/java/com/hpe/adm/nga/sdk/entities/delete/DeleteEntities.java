package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

/**
 * This class hold the DeleteEntities objects and serve all functions concern to
 * REST delete.
 */
public class DeleteEntities {
    private final OctaneRequest octaneRequest;

    public DeleteEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * Execute a DeleteEntities request
     *
     * @return collection of deleted entities
     */
    public OctaneCollection<EntityModel> execute()  {
        return DeleteHelper.getInstance().deleteEntityModels(octaneRequest);
    }

    /**
     * UpdateEntities DeleteEntities with new Query parameters
     *
     * @param query - new Query parameters
     * @return a DeleteEntities Object with new Query parameters
     */
    public DeleteEntities query(Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return this;
    }
}

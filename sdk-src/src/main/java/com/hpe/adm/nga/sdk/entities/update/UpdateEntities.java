package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

import java.util.Collection;

/**
 * This class hold the UpdateEntities objects and serve all functions concern to
 * REST put.
 */
public class UpdateEntities {

    private Collection<EntityModel> entityModels = null;
    private final OctaneRequest octaneRequest;

    public UpdateEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * 1. Request UpdateEntities Execution
     * 2. Parse response to a new Collection object
     * @return a collection of entities models that have been updated
     */
    public OctaneCollection<EntityModel> execute()  {
        return UpdateHelper.getInstance().updateEntityModels(entityModels, octaneRequest);
    }

    /**
     * UpdateEntities query parameters
     *
     * @param query - new query parameters
     * @return UpdateEntities object with new query parameters
     */
    public UpdateEntities query(Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return this;
    }

    /**
     * Set new entities collection
     *
     * @param entities The entities which will be updated
     * @return create Object with new entities collection
     */
    public UpdateEntities entities(Collection<EntityModel> entities) {
        entityModels = entities;
        return this;
    }

}

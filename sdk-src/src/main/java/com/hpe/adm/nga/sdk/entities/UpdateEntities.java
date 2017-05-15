package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * This class hold the UpdateEntities objects and serve all functions concern to
 * REST put.
 */
public class UpdateEntities extends OctaneRequest {

    private Collection<EntityModel> entityModels = null;

    UpdateEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super (octaneHttpClient, urlDomain);
    }

    /**
     * 1. Request UpdateEntities Execution
     * 2. Parse response to a new Collection object
     */
    public Collection<EntityModel> execute() throws RuntimeException {

        Collection<EntityModel> newEntityModels = null;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels);
        String jsonEntityModel = objBase.toString();
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(
                    getFinalRequestUrl(),
                    OctaneHttpRequest.JSON_CONTENT_TYPE, jsonEntityModel)
                    .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = getEntitiesResponse(octaneHttpRequest);

        } catch (Exception e) {

            handleException(e, true);
        }

        return newEntityModels;

    }

    /**
     * UpdateEntities query parameters
     *
     * @param query - new query parameters
     * @return UpdateEntities object with new query parameters
     */
    public UpdateEntities query(Query query) {
        getOctaneUrl().setDqlQueryParam(query);
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

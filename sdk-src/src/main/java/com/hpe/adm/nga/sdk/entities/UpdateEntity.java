package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

/**
 * This class hold the UpdateEntities object of one entity
 */
public class UpdateEntity extends OctaneRequest {

    private EntityModel entityModel;

    UpdateEntity(OctaneHttpClient octaneHttpClient, String urlDomain, int iEntityId) {
        super(octaneHttpClient, urlDomain, iEntityId);
    }

    /**
     * 1. UpdateEntities Request execution with json data 2. Parse response to
     * a new EntityModel object
     */
    public EntityModel execute() {

        EntityModel newEntityModel = null;
        JSONObject objBase = ModelParser.getInstance().getEntityJSONObject(entityModel);
        String jsonEntityModel = objBase.toString();

        try {
            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.PutOctaneHttpRequest(getFinalRequestUrl(),
                            OctaneHttpRequest.JSON_CONTENT_TYPE,
                            jsonEntityModel)
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);

            newEntityModel = getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {
            handleException(e, false);
        }

        return newEntityModel;
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

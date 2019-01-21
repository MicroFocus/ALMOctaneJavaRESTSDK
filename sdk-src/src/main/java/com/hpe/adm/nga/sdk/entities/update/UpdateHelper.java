package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * A helper for updating entities
 */
final class UpdateHelper {

    private static final UpdateHelper INSTANCE = new UpdateHelper();

    private UpdateHelper() {
    }

    static UpdateHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. UpdateEntities Request execution with json data 2. Parse response to
     * a new EntityModel object
     *
     * @param entityModel the entitymodel
     * @param octaneRequest the octane request
     */
    EntityModel updateEntityModel(EntityModel entityModel, OctaneRequest octaneRequest) {
        JSONObject objBase = ModelParser.getInstance().getEntityJSONObject(entityModel, true);
        String jsonEntityModel = objBase.toString();

        OctaneHttpRequest octaneHttpRequest =
                new OctaneHttpRequest.PutOctaneHttpRequest(octaneRequest.getFinalRequestUrl(),
                        OctaneHttpRequest.JSON_CONTENT_TYPE,
                        jsonEntityModel)
                        .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);

        return octaneRequest.getEntityResponse(octaneHttpRequest);
    }

    /**
     * 1. Request UpdateEntities Execution
     * 2. Parse response to a new Collection object
     *
     * @param entityModels the entitymodel
     * @param octaneRequest the octane request
     */
    OctaneCollection<EntityModel> updateEntityModels(Collection<EntityModel> entityModels, OctaneRequest octaneRequest)  {
        OctaneCollection<EntityModel> newEntityModels;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels, true);
        String jsonEntityModel = objBase.toString();

        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(
                octaneRequest.getFinalRequestUrl(),
                OctaneHttpRequest.JSON_CONTENT_TYPE, jsonEntityModel)
                .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
        newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);

        //TODO: partial support exception handling

        return newEntityModels;
    }

}
package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by brucesp on 27-Jun-17.
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
     * @param entityModel
     * @param octaneRequest
     */
    EntityModel updateEntityModel(EntityModel entityModel, OctaneRequest octaneRequest) {
        EntityModel newEntityModel = null;
        JSONObject objBase = ModelParser.getInstance().getEntityJSONObject(entityModel);
        String jsonEntityModel = objBase.toString();

        try {
            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.PutOctaneHttpRequest(octaneRequest.getFinalRequestUrl(),
                            OctaneHttpRequest.JSON_CONTENT_TYPE,
                            jsonEntityModel)
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);

            newEntityModel = octaneRequest.getEntityResponse(octaneHttpRequest);
        } catch (Exception e) {
            octaneRequest.handleException(e, false);
        }

        return newEntityModel;
    }

    /**
     * 1. Request UpdateEntities Execution
     * 2. Parse response to a new Collection object
     *
     * @param entityModels
     * @param octaneRequest
     */
    Collection<EntityModel> updateEntityModels(Collection<EntityModel> entityModels, OctaneRequest octaneRequest) throws RuntimeException {
        Collection<EntityModel> newEntityModels = null;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels);
        String jsonEntityModel = objBase.toString();
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(
                    octaneRequest.getFinalRequestUrl(),
                    OctaneHttpRequest.JSON_CONTENT_TYPE, jsonEntityModel)
                    .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);

        } catch (Exception e) {
            octaneRequest.handleException(e, true);
        }

        return newEntityModels;
    }

}

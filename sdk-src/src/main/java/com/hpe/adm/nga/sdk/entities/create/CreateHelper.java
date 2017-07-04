package com.hpe.adm.nga.sdk.entities.create;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by brucesp on 27-Jun-17.
 */
final class CreateHelper {

    private static final CreateHelper INSTANCE = new CreateHelper();

    private CreateHelper() {
    }

    static CreateHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 1. build Entity Json Object  2. Post
     * Request execution with json data 3. Parse response to a new
     * object
     * @param entityModels
     * @param octaneRequest
     */
    public Collection<EntityModel> createEntities(Collection<EntityModel> entityModels, OctaneRequest octaneRequest) throws RuntimeException {

        Collection<EntityModel> newEntityModels = null;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels);
        String strJsonEntityModel = objBase.toString();
        try {
            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.PostOctaneHttpRequest(octaneRequest.getFinalRequestUrl(), OctaneHttpRequest.JSON_CONTENT_TYPE, strJsonEntityModel)
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {

            octaneRequest.handleException(e, true);
        }

        return newEntityModels;
    }
}

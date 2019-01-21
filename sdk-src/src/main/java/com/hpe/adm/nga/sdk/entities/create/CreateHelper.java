package com.hpe.adm.nga.sdk.entities.create;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.util.Collection;

/**
 * A helper for creating entities
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
     * @param entityModels the collection of entitymodels
     * @param octaneRequest the octane request
     */
    OctaneCollection<EntityModel> createEntities(Collection<EntityModel> entityModels, OctaneRequest octaneRequest)  {

        OctaneCollection<EntityModel> newEntityModels;
        JSONObject objBase = ModelParser.getInstance().getEntitiesJSONObject(entityModels);
        String strJsonEntityModel = objBase.toString();

        OctaneHttpRequest octaneHttpRequest =
                new OctaneHttpRequest.PostOctaneHttpRequest(octaneRequest.getFinalRequestUrl(), OctaneHttpRequest.JSON_CONTENT_TYPE, strJsonEntityModel)
                        .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);

        newEntityModels = octaneRequest.getEntitiesResponse(octaneHttpRequest);

        //TODO: partial support

        return newEntityModels;
    }
}

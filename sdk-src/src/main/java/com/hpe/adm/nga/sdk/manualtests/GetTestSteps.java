package com.hpe.adm.nga.sdk.manualtests;

import com.hpe.adm.nga.sdk.manualtests.script.GetTestScriptModel;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetTestSteps {

    private final Logger logger = LoggerFactory.getLogger(GetTestSteps.class.getName());
    private final OctaneHttpClient octaneHttpClient;
    private final String scriptUrl;

    GetTestSteps(OctaneHttpClient octaneHttpClient, String scriptUrl) {
        this.octaneHttpClient = octaneHttpClient;
        this.scriptUrl = scriptUrl;
    }

    public GetTestScriptModel execute() {
        OctaneHttpRequest.GetOctaneHttpRequest getOctaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(scriptUrl);
        OctaneHttpResponse response = octaneHttpClient.execute(getOctaneHttpRequest);

        String json = "";
        if (response.isSuccessStatusCode()) {
            json = response.getContent();
            logger.debug(String.format("Response_Json: %s", json));
        }

        final JSONTokener tokener = new JSONTokener(json);
        final JSONObject jsonObj = new JSONObject(tokener);

        final EntityModel entityModel = ModelParser.getInstance().getEntityModel(jsonObj);
        return new GetTestScriptModel(entityModel);
    }
}

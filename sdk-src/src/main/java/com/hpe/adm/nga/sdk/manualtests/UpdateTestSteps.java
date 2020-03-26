package com.hpe.adm.nga.sdk.manualtests;

import com.hpe.adm.nga.sdk.manualtests.script.UpdateTestScriptModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;

public class UpdateTestSteps {
    private final OctaneHttpClient octaneHttpClient;
    private final String scriptUrl;
    private UpdateTestScriptModel updateTestScriptModel;

    UpdateTestSteps(OctaneHttpClient octaneHttpClient, String scriptUrl) {
        this.octaneHttpClient = octaneHttpClient;
        this.scriptUrl = scriptUrl;
    }

    public UpdateTestSteps testSteps(final UpdateTestScriptModel updateTestScriptModel) {
        this.updateTestScriptModel = updateTestScriptModel;
        return this;
    }

    public boolean execute() {
        final String modelAsString = ModelParser.getInstance().getEntityJSONObject(updateTestScriptModel.getWrappedEntityModel()).toString();
        final OctaneHttpRequest.PutOctaneHttpRequest putOctaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(scriptUrl, OctaneHttpRequest.JSON_CONTENT_TYPE, modelAsString);
        return octaneHttpClient.execute(putOctaneHttpRequest).isSuccessStatusCode();
    }
}

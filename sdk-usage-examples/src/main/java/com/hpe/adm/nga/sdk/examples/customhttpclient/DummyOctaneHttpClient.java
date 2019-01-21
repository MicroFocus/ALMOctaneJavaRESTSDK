package com.hpe.adm.nga.sdk.examples.customhttpclient;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Dummy implementation of {@link OctaneHttpClient} for the {@link DummyOctaneHttpClientExample}
 */
public class DummyOctaneHttpClient implements OctaneHttpClient {

    /**
     * Number of dummy defects to make
     */
    public static int dummyDefectCount = 10;

    public DummyOctaneHttpClient(String ignored){}

    @Override
    public boolean authenticate(Authentication authentication) {
        return true;
    }

    @Override
    public void signOut() {
    }

    @Override
    public OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest) {
        //Create some dummy entity models
        Collection<EntityModel> entities = new ArrayList<>();
        for (int i = 1; i <= dummyDefectCount; i++) {
            EntityModel entityModel = new EntityModel();
            entityModel.setValue(new StringFieldModel("id", i + ""));
            entityModel.setValue(new StringFieldModel("name", "defect no:" + i));
            entityModel.setValue(new StringFieldModel("type", "work_item"));
            entityModel.setValue(new StringFieldModel("subtype", "defect"));
            entities.add(entityModel);
        }

        //Convert them to JSON that matches server return type
        String returnJson = ModelParser.getInstance().getEntitiesJSONObject(entities).toString();

        //Return a dummy OctaneHttpResponse
        InputStream stream = new ByteArrayInputStream(returnJson.getBytes(StandardCharsets.UTF_8));
        return new OctaneHttpResponse(202, stream, StandardCharsets.UTF_8);
    }

}
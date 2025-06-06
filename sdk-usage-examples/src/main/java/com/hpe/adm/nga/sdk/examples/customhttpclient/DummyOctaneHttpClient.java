/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.examples.customhttpclient;

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
    public boolean authenticate() {
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

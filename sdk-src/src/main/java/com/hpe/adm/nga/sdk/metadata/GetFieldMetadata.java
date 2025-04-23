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
package com.hpe.adm.nga.sdk.metadata;

import com.google.gson.Gson;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * This class hold the field metadata object
 */
public final class GetFieldMetadata extends MetadataOctaneRequest {

    private final Logger logger = LoggerFactory.getLogger(GetFieldMetadata.class.getName());
    private static final String TYPE_NAME_FIELDS_NAME = "fields";
    private static final String QUERY_NAME_FIELD_NAME = "entity_name";

    /**
     * Creates a new Field object
     *
     * @param octaneHttpClient implementation of the {@link OctaneHttpClient}
     * @param urlDomain        base url to use with the http client
     */
    public GetFieldMetadata(OctaneHttpClient octaneHttpClient, String urlDomain) {

        super(octaneHttpClient, urlDomain + "/" + TYPE_NAME_FIELDS_NAME);
    }

    GetFieldMetadata addEntities(String... entities) {
        return (GetFieldMetadata) super.addEntities(QUERY_NAME_FIELD_NAME, entities);
    }

    /**
     * GetEntities Request execution of metadata's field info
     * Collection object
     * @return Collection of {@link FieldMetadata} objects from the server
     */
    public Collection<FieldMetadata> execute() {

        Collection<FieldMetadata> colEntitiesMetadata = null;
        String json = "";

        OctaneHttpRequest octaneHttpRequest =
                new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl()).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
        OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        if (response.isSuccessStatusCode()) {

            json = response.getContent();
            colEntitiesMetadata = getFieldMetadata(json);
        }

        logger.debug(LOGGER_RESPONSE_JSON_FORMAT, json);
        return colEntitiesMetadata;

    }

    /**
     * get a fields metadata collection based on a given json string
     *
     * @param json the json to parse
     * @return fields metadata collection based on a given json string
     */
    private Collection<FieldMetadata> getFieldMetadata(String json) {

        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_FIELD_NAME);

        // prepare entity collection
        Collection<FieldMetadata> fieldsMetadata = new ArrayList<>();
        IntStream.range(0, jsonDataArr.length()).forEach((i) -> fieldsMetadata.add(new Gson().fromJson(jsonDataArr.getJSONObject(i).toString(), FieldMetadata.class)));

        return fieldsMetadata;
    }
}

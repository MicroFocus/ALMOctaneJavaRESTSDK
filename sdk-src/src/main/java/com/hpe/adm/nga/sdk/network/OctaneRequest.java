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
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.APIMode;
import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * An abstract representation of a request
 */
public final class OctaneRequest {

    private final Logger logger = LoggerFactory.getLogger(OctaneRequest.class.getName());

    private final OctaneUrl octaneUrl;
    protected final OctaneHttpClient octaneHttpClient;
    private final Set<APIMode> httpHeaders = new HashSet<>();

    // constant
    private static final String LOGGER_RESPONSE_JSON_FORMAT = "Response_Json: {}";

    public OctaneRequest(final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        octaneUrl = new OctaneUrl(urlDomain);
        this.octaneHttpClient = octaneHttpClient;
    }

    public OctaneRequest(final OctaneHttpClient octaneHttpClient, final String urlDomain, final String entityId) {
        this(octaneHttpClient, urlDomain);
        octaneUrl.addPaths(entityId);
    }

    public void addHeader(APIMode header) {
        httpHeaders.add(header);
    }

    public void removeHeader(APIMode header) {
        httpHeaders.remove(header);
    }

    public Set<APIMode> getHeaders() {
        return Collections.unmodifiableSet(httpHeaders);
    }

    public final OctaneUrl getOctaneUrl() {
        return octaneUrl;
    }

    public final String getFinalRequestUrl() {
        return octaneUrl.toString();
    }

    /**
     * get entities result based on Http Request
     *
     * @param octaneHttpRequest - http request
     * @return entities ased on Http Request
     */
    public final OctaneCollection<EntityModel> getEntitiesResponse(OctaneHttpRequest octaneHttpRequest) {

        OctaneCollection<EntityModel> newEntityModels = null;

        OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        String json = response.getContent();
        logger.debug(LOGGER_RESPONSE_JSON_FORMAT, json);

        if (response.isSuccessStatusCode() && json != null && !json.isEmpty()) {
            newEntityModels = ModelParser.getInstance().getEntities(json);

        }

        return newEntityModels;
    }

    /**
     * get entity result based on Http Request
     *
     * @param octaneHttpRequest the request object
     * @return EntityModel
     */
    public EntityModel getEntityResponse(OctaneHttpRequest octaneHttpRequest) {

        EntityModel newEntityModel = null;

        OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        String json = response.getContent();
        logger.debug(LOGGER_RESPONSE_JSON_FORMAT, json);
        if (response.isSuccessStatusCode() && (json != null && !json.isEmpty())) {

            JSONTokener tokener = new JSONTokener(json);
            JSONObject jsonObj = new JSONObject(tokener);
            newEntityModel = ModelParser.getInstance().getEntityModel(jsonObj);
        }

        return newEntityModel;

    }

}

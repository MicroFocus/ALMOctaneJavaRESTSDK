/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Collection;

/**
 * This class hold the CreateEntities objects and serve all functions concern to REST
 * GetEntities.
 */
public class CreateAttachment extends OctaneRequest {

    private String contentType = "";
    private String contentName = "";
    private InputStream inputStream = null;
    private EntityModel entity = null;

    protected CreateAttachment(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super(octaneHttpClient, urlDomain);
    }

    /**
     * Setter of new entities to create and file to upload
     *
     * @param entity - new entity to create
     * @param stream - file stream
     * @param type   The content type
     * @param name   The content name
     * @return - An Object with new data
     */
    public CreateAttachment attachment(EntityModel entity, InputStream stream, String type, String name) {

        this.entity = entity;
        inputStream = stream;
        contentType = type;
        contentName = name;

        return this;
    }

    /**
     * Request Post Execution with Multipart content type
     * return a collection of entities models that have been created
     */
    public Collection<EntityModel> execute() throws RuntimeException {

        return executeMultipart(entity, inputStream, contentType, contentName);
    }

    /**
     * Post a multipart request - A request made of a json data and file upload:
     * 1. Construct multipart data
     * 2. get response
     *
     * @param entity      - new entity data to create
     * @param inputStream - file stream
     * @param contentName The name of the content
     * @param contentType The type of the content
     * @return - response - collection of entity models which have been created
     */
    private Collection<EntityModel> executeMultipart(EntityModel entity, InputStream inputStream, String contentType, String contentName) {

        Collection<EntityModel> newEntityModels = null;

        JSONObject data = ModelParser.getInstance().getEntityJSONObject(entity);
        try {
            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.PostBinaryOctaneHttpRequest(getFinalRequestUrl(), inputStream, data.toString(), contentName, contentType)
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            newEntityModels = getEntitiesResponse(octaneHttpRequest);
        } catch (Exception e) {
            handleException(e, false);
        }

        return newEntityModels;
    }
}

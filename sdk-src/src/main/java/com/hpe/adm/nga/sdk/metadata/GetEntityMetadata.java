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

package com.hpe.adm.nga.sdk.metadata;

import com.google.gson.Gson;
import com.hpe.adm.nga.sdk.metadata.features.*;
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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * This class hold the entity metadata object
 */
public final class GetEntityMetadata extends MetadataOctaneRequest {

    private static final String TYPE_NAME_ENTITIES_NAME = "entities";
    private static final String QUERY_NAME_FIELD_NAME = "name";
    private final Logger logger = LoggerFactory.getLogger(GetEntityMetadata.class.getName());

    private static final String JSON_NAME_FIELD_NAME = "name";
    private static final String JSON_LABEL_FIELD_NAME = "label";
    private static final String LOGGER_INVALID_FEATURE_FORMAT = ": not a valid feature";
    private static final String JSON_FEATURES_FIELD_NAME = "features";
    private static final String FEATURE_REST_NAME = "rest";
    private static final String FEATURE_MAILING_NAME = "mailing";
    private static final String FEATURE_HAS_ATTACHMENTS_NAME = "attachments";
    private static final String FEATURE_HAS_COMMENTS_NAME = "comments";
    private static final String FEATURE_BUSINESS_RULES_NAME = "business_rules";
    private static final String FEATURE_SUBTYPES_NAME = "subtypes";
    private static final String FEATURE_SUBTYPE_OF_NAME = "subtype_of";
    private static final String FEATURE_HIERARCHY_NAME = "hierarchy";
    private static final String FEATURE_UDF_ENTITY_NAME = "user_defined_fields";
    private static final String FEATURE_ORDERING_ENTITY_NAME = "ordering";
    private static final String FEATURE_GROUPING_ENTITY_NAME = "grouping";
    private static final String FEATURE_PHASES_ENTITY_NAME = "phases";
    private static final String FEATURE_AUDITING_ENTITY_NAME = "auditing";

    /**
     * Creates a new entity object
     * @param octaneHttpClient {@link OctaneHttpClient} impl to execute http requests
     * @param urlDomain base url of octane server
     */
    protected GetEntityMetadata(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super(octaneHttpClient, urlDomain + "/" + TYPE_NAME_ENTITIES_NAME);
    }

    public GetEntityMetadata addEntities(String... entities) {
        return (GetEntityMetadata) super.addEntities(QUERY_NAME_FIELD_NAME, entities);
    }

    /**
     * GetEntities Request execution of metadata's entity info
     * Collection object
     * @return Collection of {@link EntityMetadata}
     */
    public Collection<EntityMetadata> execute()  {

        Collection<EntityMetadata> entitiesMetadata = null;
        String json = "";

        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
        OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        if (response.isSuccessStatusCode()) {

            json = response.getContent();
            entitiesMetadata = getEntitiesMetadata(json);
        }

        logger.debug(String.format(LOGGER_RESPONSE_JSON_FORMAT, json));
        return entitiesMetadata;
    }

    /**
     * get a entities metadata collection based on a given json string
     *
     * @param json The json to parse into metadata
     * @return entity metadata collection based on a given json string
     */
    private Collection<EntityMetadata> getEntitiesMetadata(String json) {

        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_FIELD_NAME);

        // prepare entity collection
        Collection<EntityMetadata> entitiesMetadata = new ArrayList<>();
        IntStream.range(0, jsonDataArr.length()).forEach((i) -> entitiesMetadata.add(getEntityMetadata(jsonDataArr.getJSONObject(i))));

        return entitiesMetadata;
    }

    /**
     * get a new EntityMetadata object based on json object
     *
     * @param jsonEntityObj - Json object
     * @return new EntityMetadata object
     */
    private EntityMetadata getEntityMetadata(JSONObject jsonEntityObj) {

        Set<Feature> features = new HashSet<>();
        String name = jsonEntityObj.getString(JSON_NAME_FIELD_NAME);
        String label = jsonEntityObj.getString(JSON_LABEL_FIELD_NAME);
        //Boolean canModifyLabel = jsonEntityObj.getBoolean(JSON_CAN_MODIFY_LABEL_FIELD_NAME);
        JSONArray jsonFeatures = jsonEntityObj.getJSONArray(JSON_FEATURES_FIELD_NAME);
        IntStream.range(0, jsonFeatures.length()).forEach((i) -> {
            Feature featureObject = getFeatureObject(jsonFeatures.getJSONObject(i));
            if (featureObject != null) {
                features.add(featureObject);
            }
        });

        // TODO: Check this
        return new EntityMetadata(name, label, false, features);
    }

    /**
     * GetEntities Feature Object based on the feature json object
     *
     * @param jsonFeatureObj - Json Feature object
     * @return Feature Object
     */
    private Feature getFeatureObject(JSONObject jsonFeatureObj) {

        Feature feature = null;
        String featureName = jsonFeatureObj.getString(JSON_NAME_FIELD_NAME);

        switch (featureName) {
            case FEATURE_REST_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), RestFeature.class);
                break;
            case FEATURE_MAILING_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), MailingFeature.class);
                break;
            case FEATURE_HAS_ATTACHMENTS_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), AttachmentsFeature.class);
                break;
            case FEATURE_HAS_COMMENTS_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), CommentsFeature.class);
                break;
            case FEATURE_BUSINESS_RULES_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), BusinessRulesFeature.class);
                break;
            case FEATURE_SUBTYPES_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), SubTypesFeature.class);
                break;
            case FEATURE_SUBTYPE_OF_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), SubTypesOfFeature.class);
                break;
            case FEATURE_HIERARCHY_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), HierarchyFeature.class);
                break;
            case FEATURE_UDF_ENTITY_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), UdfFearture.class);
                break;
            case FEATURE_ORDERING_ENTITY_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), OrderingFeature.class);
                break;
            case FEATURE_GROUPING_ENTITY_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), GroupingFeature.class);
                break;
            case FEATURE_PHASES_ENTITY_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), PhasesFeature.class);
                break;
            case FEATURE_AUDITING_ENTITY_NAME:
                feature = new Gson().fromJson(jsonFeatureObj.toString(), AuditingFeature.class);
                break;
            default:
                logger.debug(featureName + LOGGER_INVALID_FEATURE_FORMAT);
                break;
        }

        return feature;
    }
}

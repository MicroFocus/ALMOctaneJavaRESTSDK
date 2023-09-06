/*
 * Copyright 2016-2023 Open Text.
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
package com.hpe.adm.nga.sdk.model;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.IntStream;

public final class ModelParser {
    private static final String JSON_DATA_NAME = "data";
    private static final String JSON_ERRORS_NAME = "errors";
    private static final String JSON_TOTAL_COUNT_NAME = "total_count";
    private static final String JSON_EXCEEDS_TOTAL_COUNT_NAME = "exceeds_total_count";
    private static final String REGEX_DATE_FORMAT = "\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}Z";
    private static final String LOGGER_INVALID_FIELD_SCHEME_FORMAT = "{} field scheme is invalid";

    private final Logger logger = LoggerFactory.getLogger(ModelParser.class.getName());

    private static ModelParser modelParser = new ModelParser();

    private ModelParser() {
    }

    public static ModelParser getInstance() {
        return modelParser;
    }

    /**
     * get a new json object based on a given EntityModel object
     *
     * @param entityModel the given entity model object
     * @return new json object based on a given EntityModel object
     */
    public final JSONObject getEntityJSONObject(EntityModel entityModel) {
        return getEntityJSONObject(entityModel, false);
    }

    /**
     * get a new json object based on a given EntityModel object
     *
     * @param entityModel the given entity model object
     * @param onlyDirty   Return only dirty fields (used for updates)
     * @return new json object based on a given EntityModel object
     */
    @SuppressWarnings("rawtypes")
    public final JSONObject getEntityJSONObject(EntityModel entityModel, boolean onlyDirty) {

        Collection<FieldModel> fieldModels = onlyDirty ? entityModel.getDirtyValues() : entityModel.getValues();
        JSONObject objField = new JSONObject();
        fieldModels.forEach((i) -> {
            Object value = getFieldValue(i);
            objField.put(i.getName(), Objects.isNull(value) ? JSONObject.NULL : value);
        });

        return objField;
    }

    /**
     * get a new json object based on a given EntityModel list
     *
     * @param entitiesModels - Collection of entities models
     * @return new json object conatin entities data
     */
    public final JSONObject getEntitiesJSONObject(Collection<EntityModel> entitiesModels) {
        return getEntitiesJSONObject(entitiesModels, false);
    }

    /**
     * get a new json object based on a given EntityModel list
     *
     * @param entitiesModels - Collection of entities models
     * @param onlyDirty      Converts only dirty fields (relevant for updating entity)
     * @return new json object conatin entities data
     */
    public final JSONObject getEntitiesJSONObject(Collection<EntityModel> entitiesModels, boolean onlyDirty) {

        JSONObject objBase = new JSONObject();
        JSONArray objEntities = new JSONArray();
        objBase.put(JSON_DATA_NAME, objEntities);
        objBase.put(JSON_TOTAL_COUNT_NAME, entitiesModels.size());
        objBase.put(JSON_EXCEEDS_TOTAL_COUNT_NAME, false);
        entitiesModels.forEach((i) -> objEntities.put(getEntityJSONObject(i, onlyDirty)));

        return objBase;
    }

    /**
     * GetEntities an object that represent a field value based on the Field Model
     *
     * @param fieldModel the source fieldModel
     * @return field value
     */
    @SuppressWarnings("rawtypes")
    private Object getFieldValue(FieldModel fieldModel) {

        Object fieldValue;

        if (fieldModel.getClass() == ReferenceFieldModel.class) {
            EntityModel fieldEntityModel = ((ReferenceFieldModel) fieldModel).getValue();
            fieldValue = JSONObject.NULL;

            if (fieldEntityModel != null) {
                fieldValue = getEntityJSONObject(fieldEntityModel, false);
            }

        } else if (fieldModel.getClass() == MultiReferenceFieldModel.class) {

            Collection<EntityModel> entities = ((MultiReferenceFieldModel) fieldModel).getValue();
            fieldValue = getEntitiesJSONObject(entities, false);

        } else {

            fieldValue = fieldModel.getValue();
        }

        return fieldValue;
    }

    /**
     * get a new EntityModel object based on json object
     *
     * @param jsonEntityObj - json object
     * @return new EntityModel object
     */
    @SuppressWarnings("rawtypes")
    public EntityModel getEntityModel(JSONObject jsonEntityObj) {

        Set<FieldModel> fieldModels = new HashSet<>();
        Iterator<?> keys = jsonEntityObj.keys();
        EntityModel entityModel;

        while (keys.hasNext()) {

            FieldModel fldModel;
            String strKey = (String) keys.next();
            Object aObj = jsonEntityObj.get(strKey);
            if (aObj == JSONObject.NULL) {
                fldModel = new EmptyFieldModel(strKey);
            } else if (aObj instanceof Long || aObj instanceof Integer) {
                fldModel = new LongFieldModel(strKey, Long.parseLong(aObj.toString()));
            } else if (aObj instanceof Double || aObj instanceof Float) {
                fldModel = new FloatFieldModel(strKey, Float.parseFloat(aObj.toString()));
            } else if (aObj instanceof Boolean) {
                fldModel = new BooleanFieldModel(strKey, Boolean.parseBoolean(aObj.toString()));
            } else if (aObj instanceof JSONArray) {
                fldModel = new ObjectFieldModel(strKey, aObj.toString());
            } else if (aObj instanceof JSONObject) {

                JSONObject fieldObject = jsonEntityObj.getJSONObject(strKey);

                if (!fieldObject.isNull(JSON_DATA_NAME)) {

                    Collection<EntityModel> entities = getEntities(aObj.toString());
                    fldModel = new MultiReferenceFieldModel(strKey, entities);
                } else if (!fieldObject.isNull("type") && !fieldObject.isNull("id")) {
                    EntityModel ref = getEntityModel(jsonEntityObj.getJSONObject(strKey));
                    fldModel = new ReferenceFieldModel(strKey, ref);
                } else {
                    fldModel = new ObjectFieldModel(strKey, aObj.toString());
                }

            } else if (aObj instanceof String) {

                boolean isMatch = aObj.toString().matches(REGEX_DATE_FORMAT);
                if (isMatch) {

                    final ZonedDateTime zonedDateTime = ZonedDateTime.parse(aObj.toString());
                    fldModel = new DateFieldModel(strKey, zonedDateTime);

                } else {
                    fldModel = new StringFieldModel(strKey, aObj.toString());
                }
            } else {
                logger.debug(LOGGER_INVALID_FIELD_SCHEME_FORMAT, strKey);
                continue; //do not put it inside the model object to avoid a null pointer exception
            }

            fieldModels.add(fldModel);
        }

        entityModel = new EntityModel(fieldModels, EntityModel.EntityState.CLEAN);
        return entityModel;
    }

    /**
     * get a entity model collection based on a given json string
     *
     * @param json The JSON to parse
     * @return entity model collection based on a given json string
     */
    public OctaneCollection<EntityModel> getEntities(String json) {
        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_NAME);

        final OctaneCollection<EntityModel> entityModels;
        if (jsonObj.has(JSON_EXCEEDS_TOTAL_COUNT_NAME) && jsonObj.has(JSON_TOTAL_COUNT_NAME)) {
            final boolean exceedsTotalAmount = jsonObj.getBoolean("exceeds_total_count");
            final int totalCount = jsonObj.getInt("total_count");
            entityModels = new OctaneCollectionImpl<>(totalCount, exceedsTotalAmount);
        } else {
            entityModels = new OctaneCollectionImpl<>();
        }
        IntStream.range(0, jsonDataArr.length()).forEach((i) -> entityModels.add(getEntityModel(jsonDataArr.getJSONObject(i))));

        return entityModels;
    }

    /**
     * Checks the message for a list of errors
     *
     * @param json The json to parse
     * @return Whether there is a list of errors
     */
    public boolean hasErrorModels(String json) {
        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        return jsonObj.has(JSON_ERRORS_NAME) && jsonObj.get(JSON_ERRORS_NAME) instanceof JSONArray;
    }

    /**
     * Checks the message for an error
     *
     * @param json The json to parse
     * @return Whether there is an error
     */
    public boolean hasErrorModel(String json) {
        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        return jsonObj.has("error_code") && jsonObj.get("error_code") instanceof String;
    }

    /**
     * Checks the message for a higher-level servlet error
     *
     * @param json The json to parse
     * @return Whether there is a higher level servlet error
     */
    public boolean hasServletError(String json) {
        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        return jsonObj.has("message") && jsonObj.get("message") instanceof String;
    }

    /**
     * GetEntities Error models based on a given error json string
     *
     * @param json - json string with error information
     * @return collection of error models
     */
    public Collection<ErrorModel> getErrorModels(String json) {

        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        JSONArray jsonErrArr = jsonObj.getJSONArray(JSON_ERRORS_NAME);
        Collection<ErrorModel> ErrModels = new ArrayList<>();
        IntStream.range(0, jsonErrArr.length()).forEach((i) -> ErrModels.add(getErrorModelFromjson(jsonErrArr.getJSONObject(i).toString())));

        return ErrModels;
    }

    /**
     * GetEntities Error model based on a given error json string
     *
     * @param json - json string with error information
     * @return error model
     */
    @SuppressWarnings("rawtypes")
    public ErrorModel getErrorModelFromjson(String json) {

        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonErrObj = new JSONObject(tokener);

        Set<FieldModel> fieldModels = new HashSet<>();
        Iterator<?> keys = jsonErrObj.keys();

        while (keys.hasNext()) {

            String strKey = (String) keys.next();
            Object aObj = jsonErrObj.get(strKey);

            FieldModel fldModel;

            if (aObj == JSONObject.NULL) {
                fldModel = new ReferenceErrorModel(strKey, null);
            } else if (aObj instanceof JSONObject) {
                EntityModel ref = getEntityModel(jsonErrObj.getJSONObject(strKey));
                fldModel = new ReferenceFieldModel(strKey, ref);
            } else {

                fldModel = new StringFieldModel(strKey, aObj.toString());
            }

            fieldModels.add(fldModel);

        }


        return new ErrorModel(fieldModels);
    }

    /**
     * Parses the servlet error and extracts the underlying message that was set by Octane that can then be used by the
     * exception
     *
     * @param json The json to parse
     * @return The errormodel with all extracted errors
     */
    public ErrorModel getErrorModelFromServletJson(String json) {
        final ErrorModel firstLevelModel = getErrorModelFromjson(json);
        final String messageInJson = firstLevelModel.getValue("message").getValue().toString();
        final ErrorModel errorModelFromjson = getErrorModelFromjson(messageInJson.replaceAll("&quot;", "\""));
        errorModelFromjson.getValues().forEach(firstLevelModel::setValue);

        return firstLevelModel;
    }
}

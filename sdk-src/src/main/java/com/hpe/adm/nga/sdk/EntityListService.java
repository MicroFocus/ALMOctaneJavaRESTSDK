/*
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.exception.OctanePartialException;
import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.network.HttpResponseException;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;


/**
 * This class represents the entity context and carries out the actual server requests.  It builds the correct URL as
 * appropriate
 */
public class EntityListService {

    // constant
    private static final String JSON_DATA_NAME = "data";
    private static final String JSON_ERRORS_NAME = "errors";
    private static final String JSON_TOTAL_COUNT_NAME = "total_count";
    private static final String JSON_EXCEEDS_TOTAL_COUNT_NAME = "exceeds_total_count";
    private static final String LIMIT_PARAM_FORMAT = "limit=%d&";
    private static final String OFFSET_PARAM_FORMAT = "offset=%d&";
    private static final String FIELDS_PARAM_FORMAT = "fields=%s";
    private static final String ORDER_BY_PARAM_FORMAT = "order_by=%s";
    private static final String QUERY_PARAM_FORMAT = "query=\"%s\"";
    private static final String LOGGER_RESPONSE_FORMAT = "Response: %d - %s - %s";
    private static final String LOGGER_RESPONSE_JSON_FORMAT = "Response_Json: %s";
    private static final long HTTPS_CONFLICT_STATUS_CODE = 409;
    private static final String LOGGER_INVALID_FIELD_SCHEME_FORMAT = " field scheme is invalid";
    private static final String REGEX_DATE_FORMAT = "\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}Z";
    private static final String DATE_TIME_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String LOGGER_INVALID_DATE_SCHEME_FORMAT = " date scheme is invalid";


    // private members
    private final String urlDomain;
    private final OctaneHttpClient octaneHttpClient;
    private final Logger logger = LogManager.getLogger(EntityListService.class.getName());

    // **** public Functions ***

    /**
     * Creates a new EntityListService object.  This represents an entity collection
     *
     * @param octaneHttpClient - Http Client
     * @param entityListDomain - Domain Name
     */
    public EntityListService(OctaneHttpClient octaneHttpClient, String entityListDomain) {

        urlDomain = entityListDomain;
        this.octaneHttpClient = octaneHttpClient;
    }

    /**
     * getter of an Entities object ( Entities object handle a unique entity
     * model )
     *
     * @param entityId - entity id
     * @return a new Entities object with specific id
     */
    public Entities at(int entityId) {
        return new Entities(entityId);
    }

    /**
     * getter of an Get object of EntityList ( EntityList object handle a
     * collection of entity models )
     *
     * @return a new Get object
     */
    public Get get() {

        return new Get();
    }

    /**
     * getter of an Update object of EntityList ( EntityList object handle a
     * collection of entity models )
     *
     * @return a new Update object
     */
    public Update update() {

        return new Update();
    }

    /**
     * getter of an Create object of EntityList ( EntityList object handle a
     * collection of entity models
     *
     * @return a new Create object
     */
    public Create create() {

        return new Create();
    }

    /**
     * getter of an Delete object of EntityList ( EntityList object handle a
     * collection of entity models
     *
     * @return a new Delete object
     */
    public Delete delete() {
        return new Delete();
    }

    /**
     * TBD - Remove after testing
     */
    public Collection<EntityModel> testGetEntityModels(String json) {


        JSONObject jsonObj = new JSONObject(json);
        JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_NAME);
        Collection<EntityModel> entityModels = new ArrayList<>();
        IntStream.range(0, jsonDataArr.length()).forEach((i) -> entityModels.add(getEntityModel(jsonDataArr.getJSONObject(i))));

        // TBD - remove after debugging
        /*Set<FieldModel> setFieldModel = null;
        for (int i = 0; i < jsonDataArr.length(); i++) {
			JSONObject jsonEntityObj = jsonDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jsonEntityObj);
			entityModels.add(entityModel);
		}*/

        return entityModels;
    }


    /**
     * A utility class for building a URIs with various components, based on the
     * given domain name and the global quarry parameters of entity list.
     *
     * @param urlDomain    - domain name
     * @param fieldsParams - the fields that have been requested
     * @param orderByParam - the fields that the entities will be ordered by
     * @param limitParam  - The number to limit by
     * @param offsetParam - The page number
     * @param queryParams - The query object to be used
     * @return url string ready to transmit
     */
    private String urlBuilder(String urlDomain, String fieldsParams, String orderByParam, long limitParam, long offsetParam, Query queryParams) {

        // Construct url paramters
        fieldsParams = (fieldsParams != null && !fieldsParams.isEmpty())
                ? String.format(FIELDS_PARAM_FORMAT, fieldsParams) : "";
        fieldsParams = (fieldsParams != null && !fieldsParams.isEmpty())
                ? fieldsParams.substring(0, fieldsParams.length() - 1) + "&" : "";
        String limitParamString = limitParam >= 0 ? String.format(LIMIT_PARAM_FORMAT, limitParam) : "";
        String offsetParamString = offsetParam >= 0 ? String.format(OFFSET_PARAM_FORMAT, offsetParam) : "";
        orderByParam = (orderByParam != null && !orderByParam.isEmpty())
                ? String.format(ORDER_BY_PARAM_FORMAT, orderByParam) : "";
        orderByParam = (orderByParam != null && !orderByParam.isEmpty())
                ? orderByParam.substring(0, orderByParam.length() - 1) + "&" : "";
        String queryParamsString = queryParams != null ? String.format(QUERY_PARAM_FORMAT, queryParams.getQueryString())
                : "";
        String params = fieldsParams + limitParamString + offsetParamString + orderByParam + queryParamsString;
        params = !params.isEmpty() && params.charAt(params.length() - 1) == '&'
                ? params.substring(0, params.length() - 1) : params;
        params = params.isEmpty() ? "" : "?" + params;

        return urlDomain + params;

    }

    /**
     * A utility class for building a URIs with various components, based on the
     * given domain name and the global quarry parameters of entity list.
     *
     * @param urlDomain    - domain name
     * @param queryParams- query parameters
     * @return url string ready to transmit
     */
    private String urlBuilder(String urlDomain, Query queryParams) {


        return urlBuilder(urlDomain, "", "", Long.MIN_VALUE, Long.MIN_VALUE, queryParams);

    }

    /**
     * A utility class for building a URIs with various components, based on the
     * given domain name and the global quarry parameters of entity list.
     *
     * @param urlDomain    - domain name
     * @param fieldsParams - field parameters
     * @return url string ready to transmit
     */
    private String urlBuilder(String urlDomain, String fieldsParams) {

        return urlBuilder(urlDomain, fieldsParams, "", Long.MIN_VALUE, Long.MIN_VALUE, null);
    }

    /**
     * A utility class for building a URIs with various components, based on the
     * given domain name and the global quarry parameters of entity list.
     *
     * @param urlDomain - domain name
     * @return url string ready to transmit
     */
    private String urlBuilder(String urlDomain) {
        return urlBuilder(urlDomain, (Query) null);

    }

    /**
     * get a entity model collection based on a given json string
     *
     * @param json The JSON to parse
     * @return entity model collection based on a given json string
     */
    private Collection<EntityModel> getEntities(String json) {

        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_NAME);
        Collection<EntityModel> entityModels = new ArrayList<>();
        IntStream.range(0, jsonDataArr.length()).forEach((i) -> entityModels.add(getEntityModel(jsonDataArr.getJSONObject(i))));


        // TBD - remove after debugging
        /*for (int i = 0; i < jsonDataArr.length(); i++) {
            JSONObject jsonEntityObj = jsonDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jsonEntityObj);
			entityModels.add(entityModel);
		}*/

        return entityModels;
    }

    /**
     * Get an object that represent a field value based on the Field Model
     *
     * @param fieldModel the source fieldModel
     * @return field value
     */
    private Object getFieldValue(FieldModel fieldModel) {

        Object fieldValue = null;

        if (fieldModel.getClass() == ReferenceFieldModel.class) {
            EntityModel fieldEntityModel = ((ReferenceFieldModel) fieldModel).getValue();
            fieldValue = JSONObject.NULL;

            if (fieldEntityModel != null) {
                fieldValue = getEntityJSONObject(fieldEntityModel);
            }

        } else if (fieldModel.getClass() == MultiReferenceFieldModel.class) {

            Collection<EntityModel> entities = ((MultiReferenceFieldModel) fieldModel).getValue();
            fieldValue = getEntitiesJSONObject(entities);

        } else if (fieldModel.getClass() == DateFieldModel.class) {

            DateFormat df = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
            try {
                fieldValue = df.format(fieldModel.getValue());
            } catch (Exception ex) {
                logger.debug(fieldModel.getValue().toString() + LOGGER_INVALID_DATE_SCHEME_FORMAT);
            }

        } else {

            fieldValue = fieldModel.getValue();
        }

        return fieldValue;
    }


    /**
     * get a new json object based on a given EntityModel object
     *
     * @param entityModel the given entity model object
     * @return new json object based on a given EntityModel object
     */
    private JSONObject getEntityJSONObject(EntityModel entityModel) {

        Set<FieldModel> fieldModels = entityModel.getValues();
        JSONObject objField = new JSONObject();
        fieldModels.forEach((i) -> objField.put(i.getName(), getFieldValue(i)));

        // TBD - Remove after debugging
        /*for (Iterator iterator2 = fieldModels.iterator(); iterator2.hasNext();) {
            FieldModel fieldModel = (FieldModel) iterator2.next();
			Object fieldValue = getFieldValue(fieldModel);
			objField.put(fieldModel.getName(), fieldValue);
		}*/

        return objField;
    }

    /**
     * get a new json object based on a given EntityModel list
     *
     * @param entitiesModels - Collection of entities models
     * @return new json object conatin entities data
     */
    private JSONObject getEntitiesJSONObject(Collection<EntityModel> entitiesModels) {

        JSONObject objBase = new JSONObject();
        JSONArray objEntities = new JSONArray();
        objBase.put(JSON_DATA_NAME, objEntities);
        objBase.put(JSON_TOTAL_COUNT_NAME, entitiesModels.size());
        objBase.put(JSON_EXCEEDS_TOTAL_COUNT_NAME, false);
        entitiesModels.forEach((i) -> objEntities.put(getEntityJSONObject(i)));

        // TBD- Remove after debugging
        /*for (Iterator iterator1 = entitiesModels.iterator(); iterator1.hasNext();) {
			EntityModel entityModel = (EntityModel) iterator1.next();
			Set<FieldModel> setFieldModel = entityModel.getValue();
			JSONObject objField = getEntityJSONObject(entityModel);
			objEntities.put(objField);
		}*/

        return objBase;

    }

    /**
     * get a new EntityModel object based on json object
     *
     * @param jsonEntityObj - json object
     * @return new EntityModel object
     */
    private EntityModel getEntityModel(JSONObject jsonEntityObj) {

        Set<FieldModel> fieldModels = new HashSet<>();
        Iterator<?> keys = jsonEntityObj.keys();
        EntityModel entityModel;

        while (keys.hasNext()) {

            FieldModel fldModel = null;
            String strKey = (String) keys.next();
            Object aObj = jsonEntityObj.get(strKey);
            if (aObj == JSONObject.NULL) {
                fldModel = new ReferenceFieldModel(strKey, null);
            } else if (aObj instanceof Long || aObj instanceof Integer) {
                fldModel = new LongFieldModel(strKey, Long.parseLong(aObj.toString()));
            } else if (aObj instanceof Float) {
                fldModel = new FloatFieldModel(strKey, Float.parseFloat(aObj.toString()));
            } else if (aObj instanceof Boolean) {
                fldModel = new BooleanFieldModel(strKey, Boolean.parseBoolean(aObj.toString()));
            } else if (aObj instanceof JSONObject) {

                JSONObject fieldObject = jsonEntityObj.getJSONObject(strKey);

                if (!fieldObject.isNull(JSON_DATA_NAME)) {

                    Collection<EntityModel> entities = getEntities(aObj.toString());
                    fldModel = new MultiReferenceFieldModel(strKey, entities);
                } else {
                    EntityModel ref = getEntityModel(jsonEntityObj.getJSONObject(strKey));
                    fldModel = new ReferenceFieldModel(strKey, ref);
                }

            } else if (aObj instanceof String) {

                boolean isMatch = aObj.toString().matches(REGEX_DATE_FORMAT);
                if (isMatch) {

                    DateFormat df = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
                    try {
                        Date result = df.parse(aObj.toString());
                        fldModel = new DateFieldModel(strKey, result);
                    } catch (Exception ex) {
                        logger.debug(aObj + LOGGER_INVALID_DATE_SCHEME_FORMAT);
                    }
                } else {
                    fldModel = new StringFieldModel(strKey, aObj.toString());
                }
            } else {
                logger.debug(strKey + LOGGER_INVALID_FIELD_SCHEME_FORMAT);
            }

            fieldModels.add(fldModel);
        }

        entityModel = new EntityModel(fieldModels);
        return entityModel;
    }


    /**
     * Get Error models based on a given error json string
     *
     * @param json - json string with error information
     * @return collection of error models
     */
    private Collection<ErrorModel> getErrorModels(String json) {

        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        JSONArray jsonErrArr = jsonObj.getJSONArray(JSON_ERRORS_NAME);
        Collection<ErrorModel> ErrModels = new ArrayList<>();
        IntStream.range(0, jsonErrArr.length()).forEach((i) -> ErrModels.add(getErrorModelFromjson(jsonErrArr.getJSONObject(i).toString())));

        // TBD- Remove after debug
        // prepare entity collection
		/*Collection<ErrorModel> ErrModels = new ArrayList<ErrorModel>();
		for (int i = 0; i < jsonErrArr.length(); i++) {
			JSONObject jsonErrObj = jsonErrArr.getJSONObject(i);
			ErrorModel errorModel = getErrorModel(jsonErrObj.toString());
			ErrModels.add(errorModel);
		}*/

        return ErrModels;
    }

    /**
     * Get Error model based on a given error json string
     *
     * @param json - json string with error information
     * @return error model
     */
    private ErrorModel getErrorModelFromjson(String json) {

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
            } else if (aObj instanceof JSONObject || aObj == JSONObject.NULL) {
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
     * get entities result based on Http Request
     *
     * @param octaneHttpRequest - http request
     * @return entities ased on Http Request
     * * @throws Exception
     */
    private Collection<EntityModel> getEntitiesResponse(OctaneHttpRequest octaneHttpRequest) throws Exception {

        Collection<EntityModel> newEntityModels = null;

        OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        String json = response.getContent();
        logger.debug(String.format(LOGGER_RESPONSE_JSON_FORMAT, json));

        if (response.isSuccessStatusCode() && json != null && !json.isEmpty()) {
            newEntityModels = getEntities(json);

        }

        return newEntityModels;
    }

    /**
     * get entity result based on Http Request
     *
     * @param octaneHttpRequest the request object
     * @return EntityModel
     */
    private EntityModel getEntityResponse(OctaneHttpRequest octaneHttpRequest) {

        EntityModel newEntityModel = null;

        OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        String json = response.getContent();
        logger.debug(String.format(LOGGER_RESPONSE_JSON_FORMAT, json));
        if (response.isSuccessStatusCode() && (json != null && !json.isEmpty())) {

            JSONTokener tokener = new JSONTokener(json);
            JSONObject jsonObj = new JSONObject(tokener);
            newEntityModel = getEntityModel(jsonObj);
        }

        return newEntityModel;

    }

    /**
     * Handle exceptions
     *
     * @param e              - exception
     * @param partialSupport - Is Partial ?
     */
    private void handleException(Exception e, boolean partialSupport) {

        if (e instanceof HttpResponseException) {

            HttpResponseException httpResponseException = (HttpResponseException) e;
            logger.debug(String.format(LOGGER_RESPONSE_FORMAT, httpResponseException.getStatusCode(), httpResponseException.getStatusMessage(), httpResponseException.getHeaders().toString()));
            if (partialSupport && httpResponseException.getStatusCode() == HTTPS_CONFLICT_STATUS_CODE) {
                Collection<EntityModel> entities = getEntities(httpResponseException.getContent());
                Collection<ErrorModel> errorModels = getErrorModels(httpResponseException.getContent());
                throw new OctanePartialException(errorModels, entities);
            } else {
                ErrorModel errorModel = getErrorModelFromjson(httpResponseException.getContent());
                throw new OctaneException(errorModel);
            }
        } else {
            boolean traverse = true;
            Throwable throwable = e;
            while (traverse) {
                Throwable nextThrowable = throwable.getCause();
                if (nextThrowable == null || nextThrowable == throwable) {
                    traverse = false;
                } else {
                    throwable = nextThrowable;
                }
            }
            ErrorModel errorModel = new ErrorModel(throwable.getMessage());
            throw new OctaneException(errorModel);
        }
    }

    // **** Classes ***

    /**
     * This class hold the Get objects and serve all functions concern to REST
     * Get.
     *
     * @author Moris Oz
     */
    public class Get extends OctaneRequest<Collection<EntityModel>> {

        private String fieldsParams = "";
        private String orderByParam = "";
        private long limitParam = Long.MIN_VALUE;
        private long offsetParam = Long.MIN_VALUE;
        private Query queryParams = null;

        // Public

        /**
         * 1. Request Get Execution
         * 2. Parse response to a new Collection
         * <EntityModel> object
         */
        @Override
        public Collection<EntityModel> execute() throws RuntimeException {


            Collection<EntityModel> newEntityModels = null;
            String url = urlBuilder(urlDomain, fieldsParams, orderByParam, limitParam, offsetParam, queryParams);
            try {
                OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(url).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
                newEntityModels = getEntitiesResponse(octaneHttpRequest);
            } catch (Exception e) {

                handleException(e, false);
            }

            return newEntityModels;
        }

        /**
         * Add Fields parameters
         *
         * @param fields An array of fields that will be part of the HTTP Request
         * @return Get Object with new Fields parameters
         */
        public Get addFields(String... fields) {

            fieldsParams += String.join(",", (CharSequence[]) fields) + ",";
            return this;
        }

        /**
         * Add Limit parameter
         *
         * @param limit
         * @return Get Object with new limit parameter
         */
        public Get limit(int limit) {

            limitParam = limit;
            return this;
        }

        /**
         * Add offset parameter
         *
         * @param offset
         * @return Get Object with new offset parameter
         */
        public Get offset(int offset) {
            offsetParam = offset;
            return this;
        }

        /**
         * Add OrderBy parameters
         *
         * @param orderBy
         * @param asc     - true=ascending/false=descending
         * @return Get Object with new OrderBy parameters
         */
        public Get addOrderBy(String orderBy, boolean asc) {

            String ascString = asc ? "" : "-";

            orderByParam += ascString + String.join(",", orderBy) + ",";
            return this;
        }

        /**
         * @param query
         * @return
         */
        public Get query(Query query) {

            queryParams = query;
            return this;
        }
    }

    /**
     * This class hold the Update objects and serve all functions concern to
     * REST put.
     *
     * @author moris oz
     */
    public class Update extends OctaneRequest<Collection<EntityModel>> {

        private Collection<EntityModel> entityModels = null;
        private Query queryParams = null;

        /**
         * 1. Request Update Execution
         * 2. Parse response to a new Collection
         * <p>
         * <EntityModel> object
         */
        @Override
        public Collection<EntityModel> execute() throws RuntimeException {

            Collection<EntityModel> newEntityModels = null;
            String url = urlBuilder(urlDomain, queryParams);
            JSONObject objBase = getEntitiesJSONObject(entityModels);
            String jsonEntityModel = objBase.toString();

            try {
                OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.PutOctaneHttpRequest(url,
                        OctaneHttpRequest.JSON_CONTENT_TYPE, jsonEntityModel)
                        .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
                newEntityModels = getEntitiesResponse(octaneHttpRequest);

            } catch (Exception e) {

                handleException(e, true);
            }

            return newEntityModels;

        }

        /**
         * Update query parameters
         *
         * @param query - new query parameters
         * @return Update object with new query parameters
         */
        public Update query(Query query) {
            queryParams = query;
            return this;
        }

        /**
         * Set new entities collection
         *
         * @param entities
         * @return create Object with new entities collection
         */
        public Update entities(Collection<EntityModel> entities) {
            entityModels = entities;
            return this;
        }

    }

    /**
     * This class hold the Update objects and serve all functions concern to
     * REST Post.
     *
     * @author Moris Oz
     */
    public class Create extends OctaneRequest<Collection<EntityModel>> {

        private Collection<EntityModel> entityModels = null;

        /**
         * 1. build Entity Json Object from Collection<EntityModel> 2. Post
         * Request execution with json data 3. Parse response to a new
         * Collection<EntityModel> object
         */
        @Override
        public Collection<EntityModel> execute() throws RuntimeException {

            Collection<EntityModel> newEntityModels = null;
            String url = urlBuilder(urlDomain);
            JSONObject objBase = getEntitiesJSONObject(entityModels);
            String strJsonEntityModel = objBase.toString();
            try {
                OctaneHttpRequest octaneHttpRequest =
                        new OctaneHttpRequest.PostOctaneHttpRequest(url, OctaneHttpRequest.JSON_CONTENT_TYPE, strJsonEntityModel)
                                .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
                newEntityModels = getEntitiesResponse(octaneHttpRequest);
            } catch (Exception e) {

                handleException(e, true);
            }

            return newEntityModels;
        }

        /**
         * Post a multipart request - A request made of a json data and file upload:
         * 1. Construct multipart data
         * 2. get response
         *
         * @param entities    - new entities data to create
         * @param inputStream - file stream
         * @return - response - collection of entity models which have been created
         */
        public Collection<EntityModel> executeMultipart(Collection<EntityModel> entities, InputStream inputStream, String contentType, String contentName) {

            Collection<EntityModel> newEntityModels = null;
            String url = urlBuilder(urlDomain);

            JSONObject data = getEntityJSONObject(entities.iterator().next());
            try {

                OctaneHttpRequest octaneHttpRequest =
                        new OctaneHttpRequest.PostBinaryOctaneHttpRequest(url, inputStream, data.toString(), contentName, contentType)
                                .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
                newEntityModels = getEntitiesResponse(octaneHttpRequest);
            } catch (Exception e) {
                handleException(e, false);
            }

            return newEntityModels;
        }

        /**
         * Set new entities collection
         *
         * @param entities
         * @return create Object with new entities collection
         */
        public Create entities(Collection<EntityModel> entities) {

            entityModels = entities;
            return this;
        }
    }

    /**
     * This class hold the Delete objects and serve all functions concern to
     * REST delete.
     *
     * @author Moris Oz
     */
    public class Delete extends OctaneRequest<Collection<EntityModel>> {

        private Query queryParams = null;

        /**
         * Execute a Delete request
         *
         * @return null
         */
        @Override
        public Collection<EntityModel> execute() throws RuntimeException {

            Collection<EntityModel> newEntityModels = null;
            String url = urlBuilder(urlDomain, queryParams);

            try {
                OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(url);
                newEntityModels = getEntitiesResponse(octaneHttpRequest);
            } catch (Exception e) {

                handleException(e, false);
            }

            return newEntityModels;

        }

        /**
         * Update Delete with new Query parameters
         *
         * @param query - new Query parameters
         * @return a Delete Object with new Query parameters
         */
        public Delete query(Query query) {
            queryParams = query;
            return this;
        }
    }

    /**
     * This class hold the Entities object(An object that represent one Entity )
     *
     * @author Moris Oz
     */
    public class Entities {

        private int iEntityId = 0;

        /**
         * Set entityId parameter
         *
         * @param entityId
         */
        public Entities(int entityId) {
            iEntityId = entityId;
        }

        /**
         * getter of a Get object with specific entity
         *
         * @return
         */
        public Get get() {

            return new Get();
        }

        /**
         * getter of a Update object with specific entity
         *
         * @return
         */
        public Update update() {

            return new Update();
        }

        /**
         * getter of a Create object with specific entity
         *
         * @return
         */
        public Delete delete() {

            return new Delete();
        }

        /**
         * This class hold the Get object of one entity
         *
         * @author Moris Oz
         */
        public class Get extends OctaneRequest<EntityModel> {

            private String fieldsParams = "";


            /**
             * 1. Get Request execution with json data 2. Parse response to a
             * new Collection<EntityModel> object
             */
            @Override
            public EntityModel execute() throws RuntimeException {

                EntityModel newEntityModel = null;
                String domain = urlDomain + "/" + String.valueOf(iEntityId);
                String url = urlBuilder(domain, fieldsParams);
                try {
                    OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(url)
                            .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
                    newEntityModel = getEntityResponse(octaneHttpRequest);
                } catch (Exception e) {

                    handleException(e, false);
                }

                return newEntityModel;

            }

            /**
             * Get binary data
             *
             * @return - Stream with binary data
             */
            public InputStream executeBinary() {

                InputStream inputStream = null;
                String domain = urlDomain + "/" + String.valueOf(iEntityId);
                String url = urlBuilder(domain, fieldsParams);

                try {
                    OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(url)
                            .setAcceptType(OctaneHttpRequest.OCTET_STREAM_CONTENT_TYPE);
                    OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

                    if (response.isSuccessStatusCode()) {

                        inputStream = response.getInputStream();
                    }
                } catch (Exception e) {

                    handleException(e, false);

                }

                return inputStream;
            }

            /**
             * Set Fields Parameters
             *
             * @param fields
             * @return a new Get object with new Fields Parameters
             */
            public Get addFields(String... fields) {

                fieldsParams += String.join(",", (CharSequence[]) fields) + ",";
                return this;
            }
        }

        /**
         * This class hold the Update object of one entity
         *
         * @author Moris Oz
         */
        public class Update extends OctaneRequest<EntityModel> {

            private EntityModel entityModel;

            /**
             * 1. Update Request execution with json data 2. Parse response to
             * a new EntityModel object
             */
            @Override
            public EntityModel execute() {

                EntityModel newEntityModel = null;
                String domain = urlDomain + "/" + String.valueOf(iEntityId);
                JSONObject objBase = getEntityJSONObject(entityModel);
                String jsonEntityModel = objBase.toString();

                try {
                    OctaneHttpRequest octaneHttpRequest =
                            new OctaneHttpRequest.PutOctaneHttpRequest(domain, OctaneHttpRequest.JSON_CONTENT_TYPE,
                                    jsonEntityModel)
                                    .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);

                    newEntityModel = getEntityResponse(octaneHttpRequest);
                } catch (Exception e) {
                    handleException(e, false);
                }

                return newEntityModel;
            }

            /**
             * set a new entity for updating
             *
             * @param entityModel
             * @return an update object with new entity
             */
            public Update entity(EntityModel entityModel) {
                this.entityModel = entityModel;
                return this;
            }
        }

        /**
         * This class hold the Delete object of one entity
         *
         * @author Moris Oz
         */
        public class Delete extends OctaneRequest<EntityModel> {

            /**
             * 1. Get Request execution with json data 2. Parse response to a
             * new Collection<EntityModel> object
             */
            @Override
            public EntityModel execute() throws RuntimeException {

                EntityModel newEntityModel = null;
                String domain = urlDomain + "/" + String.valueOf(iEntityId);
                String url = urlBuilder(domain);
                try {
                    OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.DeleteOctaneHttpRequest(url);
                    newEntityModel = getEntityResponse(octaneHttpRequest);
                } catch (Exception e) {

                    handleException(e, false);
                }

                return newEntityModel;

            }
        }
    }
}

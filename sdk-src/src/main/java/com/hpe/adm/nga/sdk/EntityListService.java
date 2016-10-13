package com.hpe.adm.nga.sdk;

import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hpe.adm.nga.sdk.exception.NgaException;
import com.hpe.adm.nga.sdk.exception.NgaPartialException;
import com.hpe.adm.nga.sdk.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;


/**
 * This class hold the entities objects and serve all functionality concern to
 * entities.
 * 
 * @author moris oz
 *
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
	private static final String LOGGER_REQUEST_FORMAT = "Request: %s - %s - %s";
	private static final String LOGGER_RESPONSE_FORMAT = "Response: %d - %s - %s";
	private static final String LOGGER_RESPONSE_JASON_FORMAT = "Response_Jason: %s";
	private static final String HTTP_MEDIA_TYPE_MULTIPART_NAME = "multipart/form-data";
	private static final String HTTP_MULTIPART_BOUNDARY_NAME = "boundary";
	private static final String HTTP_MULTIPART_BOUNDARY_VALUE = "---------------------------92348603315617859231724135434";
	private static final String HTTP_MULTIPART_PART_DISPOSITION_NAME = "Content-Disposition";
	private static final String HTTP_MULTIPART_PART1_DISPOSITION_FORMAT = "form-data; name=\"%s\"";
	private static final String HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE = "entity";
	private static final String HTTP_MULTIPART_PART2_DISPOSITION_FORMAT = "form-data; name=\"content\"; filename=\"%s\"";
	private static final String HTTP_APPLICATION_JASON_VALUE = "application/json";
	private static final String HTTP_APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
	private static final long HTTPS_CONFLICT_STATUS_CODE = 409;
	private static final String LOGGER_INVALID_FIELD_SCHEME_FORMAT = " field scheme is invalid";
	private static final String REGEX_DATE_FORMAT = "\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}Z";
	private  static final String DATE_TIME_ISO_FORMAT 	= "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private  static final String LOGGER_INVALID_DATE_SCHEME_FORMAT 	= " date scheme is invalid";

	
	// private members
	private String urlDomain = "";
	private HttpRequestFactory requestFactory = null;
	private Logger logger = LogManager.getLogger(EntityListService.class.getName());

	// **** public Functions ***

	/**
	 * Creates a new EntityList object
	 * 
	 * @param reqFactory
	 *            - Http Request Factory
	 * @param strEntityListDomain
	 *            - Domain Name
	 */
	public EntityListService(HttpRequestFactory reqFactory, String entityListDomain) {

		urlDomain = entityListDomain;
		requestFactory = reqFactory;
	}

	/**
	 * getter of an Entities object ( Entities object handle a unique entity
	 * model )
	 * 
	 * @param entityId
	 *            - entity id
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
	 * 
	 */
	public Collection<EntityModel> testGetEntityModels(String jason)  {


		JSONObject jasonObj = new JSONObject(jason);
		JSONArray jasoDataArr = jasonObj.getJSONArray(JSON_DATA_NAME);
		Collection<EntityModel> entityModels = new ArrayList<EntityModel>();
		IntStream.range(0, jasoDataArr.length()).forEach((i)->entityModels.add(getEntityModel(jasoDataArr.getJSONObject(i))));
	
		// TBD - remove after debugging
		/*Set<FieldModel> setFieldModel = null;
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jasoEntityObj);
			entityModels.add(entityModel);
		}*/

		return entityModels;
	}

	
	/**
	 * A utility class for building a URIs with various components, based on the
	 * given domain name and the global quarry parameters of entity list.
	 * 
	 * @param urlDomain
	 *            - domain name
	 * @return url string ready to transmit
	 */
	protected String urlBuilder(String urlDomain, String fieldsParams, String orderByParam, long limitParam, long offsetParam, Query queryParams)  {

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
		params = (params!=null && !params.isEmpty()) && params.charAt(params.length() - 1) == '&'
				? params.substring(0, params.length() - 1) : params;
		params = (params!=null && params.isEmpty()) ? "" : "?" + params;

		String res = urlDomain + params;
		return res;

	}
	
	/**
	 * A utility class for building a URIs with various components, based on the
	 * given domain name and the global quarry parameters of entity list.
	 * 
	 * @param urlDomain	 - domain name
	 * @param queryParams- query parameters
	 * @return url string ready to transmit
	 * @throws UnsupportedEncodingException
	 */
	protected String urlBuilder(String urlDomain,Query queryParams)  {

		
		return urlBuilder(urlDomain, "", "", Long.MIN_VALUE, Long.MIN_VALUE, queryParams);

	}
	
	/**
	 * A utility class for building a URIs with various components, based on the
	 * given domain name and the global quarry parameters of entity list.
	 * 
	 * @param urlDomain	 - domain name
	 * @param fieldsParams - field parameters
	 * @return url string ready to transmit
	 */
	protected String urlBuilder(String urlDomain, String fieldsParams) {

		return urlBuilder(urlDomain,fieldsParams,"",Long.MIN_VALUE,Long.MIN_VALUE,null);
	}
	
	/**
	 * A utility class for building a URIs with various components, based on the
	 * given domain name and the global quarry parameters of entity list.
	 * 
	 * @param urlDomain	 - domain name
	 * @return url string ready to transmit
	 */
	protected String urlBuilder(String urlDomain)  {

		
		Query queryParams = null;
		return urlBuilder(urlDomain, queryParams);

	}

	/**
	 * get a entity model collection based on a given jason string
	 * 
	 * @param json
	 * @return entity model collection based on a given jason string
	 */
	protected  Collection<EntityModel> getEntities(String json) {

		JSONTokener tokener = new JSONTokener(json);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoDataArr = jasoObj.getJSONArray(JSON_DATA_NAME);
		Collection<EntityModel> entityModels = new ArrayList<EntityModel>();
		IntStream.range(0, jasoDataArr.length()).forEach((i)->entityModels.add(getEntityModel(jasoDataArr.getJSONObject(i))));
		
		
		// TBD - remove after debugging
		/*for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jasoEntityObj);
			entityModels.add(entityModel);
		}*/

		return entityModels;
	}
	
	/**
	 * Get an object that represent a field value based on the Field Model
	 * @param fieldModel
	 * @return field value
	 */
	protected Object getFieldValue(FieldModel fieldModel){
		
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
			try{
				fieldValue = df.format(fieldModel.getValue());
			}
		    catch ( Exception ex ){
		    	logger.debug(fieldModel.getValue().toString() + LOGGER_INVALID_DATE_SCHEME_FORMAT);
		    }

		} 
		else {

			fieldValue = fieldModel.getValue();
		}
		
		return fieldValue;
	}
	
	
	/**
	 * get a new jason object based on a given EntityModel object
	 * 
	 * @param entityModel
	 * @return new jason object based on a given EntityModel object
	 */
	protected JSONObject getEntityJSONObject(EntityModel entityModel)  {

		Set<FieldModel> fieldModels = entityModel.getValues();
		JSONObject objField = new JSONObject();
		fieldModels.forEach((i)->objField.put(i.getName(),getFieldValue(i)));
		
		// TBD - Remove after debugging
		/*for (Iterator iterator2 = fieldModels.iterator(); iterator2.hasNext();) {
			FieldModel fieldModel = (FieldModel) iterator2.next();
			Object fieldValue = getFieldValue(fieldModel);
			objField.put(fieldModel.getName(), fieldValue);
		}*/

		return objField;
	}

	/**
	 * get a new jason object based on a given EntityModel list
	 * 
	 * @param colEntities
	 *            - Collection of entities models
	 * @return new jason object conatin entities data
	 */
	protected JSONObject getEntitiesJSONObject(Collection<EntityModel> entitiesModels)  {

		JSONObject objBase = new JSONObject();
		JSONArray objEntities = new JSONArray();
		objBase.put(JSON_DATA_NAME, objEntities);
		objBase.put(JSON_TOTAL_COUNT_NAME, entitiesModels.size());
		objBase.put(JSON_EXCEEDS_TOTAL_COUNT_NAME, false);
		entitiesModels.forEach((i)->objEntities.put(getEntityJSONObject(i)));
		
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
	 * get a new EntityModel object based on jason object
	 * 
	 * @param jasoEntityObj
	 *            - Jason object
	 * @return new EntityModel object
	 */
	protected EntityModel getEntityModel(JSONObject jasoEntityObj) {

		Set<FieldModel> fieldModels = new HashSet<FieldModel>();
		Iterator<?> keys = jasoEntityObj.keys();
		EntityModel entityModel = null;

		while (keys.hasNext()) {

			FieldModel fldModel = null;
			String strKey = (String) keys.next();
			Object aObj = jasoEntityObj.get(strKey);
			if (aObj==JSONObject.NULL){
				fldModel = new ReferenceFieldModel(strKey, null);
			}
			else if(aObj instanceof Long || aObj instanceof Integer){
				fldModel = new LongFieldModel(strKey, Long.parseLong(aObj.toString()));
			}
			else if (aObj instanceof Float) {
				fldModel = new FloatFieldModel(strKey, Float.parseFloat(aObj.toString()));
			}
			else if(aObj instanceof Boolean ){
				fldModel = new BooleanFieldModel(strKey, Boolean.parseBoolean(aObj.toString()));
			}
			else if( aObj instanceof JSONObject ){

				JSONObject fieldObject = jasoEntityObj.getJSONObject(strKey);
				
				if (!fieldObject.isNull(JSON_DATA_NAME)){
					
					Collection<EntityModel> entities = getEntities(aObj.toString());
					fldModel = new MultiReferenceFieldModel(strKey,entities);
				}
				else 
				{
					EntityModel ref = getEntityModel(jasoEntityObj.getJSONObject(strKey));
					fldModel = new ReferenceFieldModel(strKey, ref);
				}
						
			}
			else if( aObj instanceof String ){
				
				boolean isMatch =  aObj.toString().matches(REGEX_DATE_FORMAT);
				if(isMatch == true){

					DateFormat df = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
					try{
						Date result =  df.parse(aObj.toString());
						fldModel = new DateFieldModel(strKey, result);
					}
				    catch ( Exception ex ){
				    	logger.debug(aObj + LOGGER_INVALID_DATE_SCHEME_FORMAT);
				    }
				}
				else{
					fldModel = new StringFieldModel(strKey, aObj.toString());
				}
			}
			else{
				logger.debug(strKey + LOGGER_INVALID_FIELD_SCHEME_FORMAT);
			}
				
			fieldModels.add(fldModel);	
		}

		entityModel = new EntityModel(fieldModels);
		return entityModel;
	}
		
	
	/**
	 * Get Error models based on a given error jason string
	 * @param jason - jason string with error information
	 * @return collection of error models
	 */
	protected Collection<ErrorModel> getErrorModels(String jason) {

		JSONTokener tokener = new JSONTokener(jason);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoErrArr = jasoObj.getJSONArray(JSON_ERRORS_NAME);
		Collection<ErrorModel> ErrModels = new ArrayList<ErrorModel>();
		IntStream.range(0, jasoErrArr.length()).forEach((i)->ErrModels.add(getErrorModelFromJason(jasoErrArr.getJSONObject(i).toString())));
		
		// TBD- Remove after debug
		// prepare entity collection
		/*Collection<ErrorModel> ErrModels = new ArrayList<ErrorModel>();
		for (int i = 0; i < jasoErrArr.length(); i++) {
			JSONObject jasoErrObj = jasoErrArr.getJSONObject(i);
			ErrorModel errorModel = getErrorModel(jasoErrObj.toString());
			ErrModels.add(errorModel);
		}*/
		
		return ErrModels;
	}
	
	/** 
	 *  Get Error model based on a given error jason string
	 * @param jason - jason string with error information
	 * @return error model
	 */
	protected ErrorModel getErrorModelFromJason(String jason){

			JSONTokener tokener = new JSONTokener(jason);
			JSONObject jasoErrObj = new JSONObject(tokener);
			
			Set<FieldModel> fieldModels = new HashSet<FieldModel>();
			Iterator<?> keys = jasoErrObj.keys();
			
			while (keys.hasNext()) {

				String strKey = (String) keys.next();
				Object aObj = jasoErrObj.get(strKey);
	
				FieldModel fldModel = null;

				if (aObj == JSONObject.NULL )
				{
					fldModel = new ReferenceErrorModel(strKey, null);
				}
				else if ( aObj instanceof JSONObject  || aObj == JSONObject.NULL ) {
					EntityModel ref = getEntityModel(jasoErrObj.getJSONObject(strKey));
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
	 * @param httpRequest - http request
	 * @return entities ased on Http Request
	 * * @throws Exception
	 */
	protected Collection<EntityModel> getEntitiesResponse(HttpRequest httpRequest) throws Exception{
	
		Collection<EntityModel> newEntityModels = null;
		logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), httpRequest.getUrl().toString(),httpRequest.getHeaders().toString()));

		HttpResponse response = httpRequest.execute();
		logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage(),response.getHeaders().toString()));
		
		String json = response.parseAsString();
		logger.debug(String.format(LOGGER_RESPONSE_JASON_FORMAT, json));
			
		if (response.isSuccessStatusCode() && json!=null && !json.isEmpty()) {
			newEntityModels = getEntities(json);

		}

		// Update request factory with the latest response Cookie
		requestFactory.getInitializer().initialize(httpRequest.setContent(ByteArrayContent.fromString(null, response.toString())));
		return newEntityModels;
	}
	
	/**
	 * get entity result based on Http Request
	 * @param httpRequest
	 * @return EntityModel
	 * @throws Exception
	 */
	protected EntityModel getEntityResponse(HttpRequest httpRequest) throws Exception{
		
		EntityModel newEntityModel = null;
		logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), httpRequest.getUrl().toString(),httpRequest.getHeaders().toString()));
		
		HttpResponse response = httpRequest.execute();
		logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage(),response.getHeaders().toString()));
		String json = response.parseAsString();
		logger.debug(String.format(LOGGER_RESPONSE_JASON_FORMAT, json));
		if (response.isSuccessStatusCode() && (json!=null && !json.isEmpty())) {

				JSONTokener tokener = new JSONTokener(json);
				JSONObject jasoObj = new JSONObject(tokener);
				newEntityModel = getEntityModel(jasoObj);
        }
			
		// Update request factory with the latest response Cookie
		requestFactory.getInitializer().initialize(httpRequest.setContent(ByteArrayContent.fromString(null, response.toString())));
		return newEntityModel;
		
	}
	
	/**
	 * Handle exceptions
	 * @param e - exception
	 * @param partialSupport - Is Partial ?
	 * @throws RuntimeException
	 */
	protected void handleException(Exception e,boolean partialSupport) throws RuntimeException{
		
		
		if (e instanceof HttpResponseException ){
			
			HttpResponseException httpResponseException = (HttpResponseException)e; 
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, httpResponseException.getStatusCode(), httpResponseException.getStatusMessage(),httpResponseException.getHeaders().toString()));
			if (partialSupport && httpResponseException.getStatusCode() == HTTPS_CONFLICT_STATUS_CODE ) {
               
            	Collection<EntityModel> entities = getEntities(httpResponseException.getContent());
            	Collection<ErrorModel> errorModels = getErrorModels(httpResponseException.getContent());
            	throw new NgaPartialException(errorModels,entities);
            }
			else
			{
				ErrorModel errorModel = getErrorModelFromJason(httpResponseException.getContent());
				throw new NgaException(errorModel);
			}
	
		}
		else{
			
			ErrorModel errorModel =  new ErrorModel(e.getMessage());
			throw new NgaException(errorModel);
			
		}
	}
	
	// **** Classes ***
	/**
	 * This class hold the Get objects and serve all functions concern to REST
	 * Get.
	 * 
	 * @author Moris Oz
	 *
	 */
	public class Get extends NGARequest<Collection<EntityModel>> {

		private String fieldsParams = "";
		private String orderByParam = "";
		private long limitParam = Long.MIN_VALUE;
		private long offsetParam =  Long.MIN_VALUE;
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
			GenericUrl domain = new GenericUrl(url);
			
			try{
				HttpRequest httpRequest = requestFactory.buildGetRequest(domain);
				newEntityModels =  getEntitiesResponse(httpRequest);
			}
			catch (Exception e){
				
				handleException(e,false);
			}
			
			return newEntityModels;
		}

		/**
		 * Add Fields parameters
		 * 
		 * @param fields
		 * @return Get Object with new Fields parameters
		 */
		public Get addFields(String... fields) {

			fieldsParams += String.join(",", fields) + ",";
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
		 * @param asc
		 *            - true=ascending/false=descending
		 * @return Get Object with new OrderBy parameters
		 */
		public Get addOrderBy(String orderBy, boolean asc) {

			String ascString = asc ? "" : "-";

			orderByParam += ascString + String.join(",", orderBy) + ",";
			return this;
		}

		/**
		 * 
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
	 *
	 */
	public class Update extends NGARequest<Collection<EntityModel>> {

		private Collection<EntityModel> entityModels = null;
		private Query queryParams = null;
		
		/**
		 * 1. Request Update Execution 
		 * 2. Parse response to a new Collection
		 * 
		 * <EntityModel> object
		 */
		@Override
		public Collection<EntityModel> execute() throws RuntimeException  {

			Collection<EntityModel> newEntityModels = null;
			String url = urlBuilder(urlDomain,queryParams);
			GenericUrl domain = new GenericUrl(url);
			JSONObject objBase = getEntitiesJSONObject(entityModels);
			String jasonEntityModel = objBase.toString();
			
			try{
				HttpRequest httpRequest = requestFactory.buildPutRequest(domain,
						ByteArrayContent.fromString(null, jasonEntityModel));

				// add default headers
				httpRequest.getHeaders().setContentType("application/json");
				httpRequest.getHeaders().setAccept("application/json");
				newEntityModels = getEntitiesResponse(httpRequest) ;
				
			}
			catch (Exception e){
				
				handleException(e,true);
			}
			
			return newEntityModels;
			
		}

		/**
		 * Update query parameters
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
	 *
	 */
	public class Create extends NGARequest<Collection<EntityModel>> {

		private Collection<EntityModel> entityModels = null;

		/**
		 * 1. build Entity Jason Object from Collection<EntityModel> 2. Post
		 * Request execution with jason data 3. Parse response to a new
		 * Collection<EntityModel> object
		 */
		@Override
		public Collection<EntityModel> execute() throws RuntimeException {

			Collection<EntityModel> newEntityModels = null;
			String url = urlBuilder(urlDomain);
			GenericUrl urlDomain = new GenericUrl(url);
			JSONObject objBase = getEntitiesJSONObject(entityModels);
			String strJasonEntityModel = objBase.toString();
			try{
				HttpRequest httpRequest = requestFactory.buildPostRequest(urlDomain,
						ByteArrayContent.fromString(null, strJasonEntityModel));

				// add default headers
				httpRequest.getHeaders().setContentType("application/json");
				httpRequest.getHeaders().setAccept("application/json");
				newEntityModels =  getEntitiesResponse(httpRequest);
			}
			catch (Exception e){
				
				handleException(e,true);
			}
			
			return newEntityModels;
		}
		
		/**
		 * Post a multipart request - A request made of a jason data and file upload :
		 * 1. Construct multipart data
		 * 2. get response 
		 * @param entities - new entities data to create 
		 * @param strFileName - file path
		 * @return - response - collection of entity models which have been created
		 * @throws Exception 
		 */
		public Collection<EntityModel> executeMultipart(Collection<EntityModel> entities, InputStream inputStream,String contentType,String contentName)
				throws RuntimeException {

			Collection<EntityModel> newEntityModels = null;
			String url = urlBuilder(urlDomain);
			GenericUrl urlDomain = new GenericUrl(url);

			JSONObject objBase = getEntitiesJSONObject(entities);
			String strJasonEntityModel = objBase.toString();
						
			// Add parameters
			MultipartContent content = new MultipartContent()
					.setMediaType(new HttpMediaType(HTTP_MEDIA_TYPE_MULTIPART_NAME).setParameter(HTTP_MULTIPART_BOUNDARY_NAME, HTTP_MULTIPART_BOUNDARY_VALUE));
			
			
			MultipartContent.Part part1 = new MultipartContent.Part(new JsonHttpContent(new JacksonFactory(), strJasonEntityModel));
			part1.setHeaders(new HttpHeaders().set(HTTP_MULTIPART_PART_DISPOSITION_NAME, String.format(HTTP_MULTIPART_PART1_DISPOSITION_FORMAT, HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE)));
			content.addPart(part1);

			// Add Stream
			InputStreamContent inputStreamContent  = new InputStreamContent(contentType,inputStream);
			MultipartContent.Part part2 = new MultipartContent.Part(inputStreamContent);
			part2.setHeaders(new HttpHeaders().set(HTTP_MULTIPART_PART_DISPOSITION_NAME, String.format(HTTP_MULTIPART_PART2_DISPOSITION_FORMAT, contentName)));
			content.addPart(part2);
			
			try{
				HttpRequest httpRequest = requestFactory.buildPostRequest(urlDomain, content);
				newEntityModels =  getEntitiesResponse(httpRequest);
			}
			catch (Exception e){
				handleException(e,false);
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
	 *
	 */
	public class Delete extends NGARequest<Collection<EntityModel>> {
		
		private Query queryParams = null;
		
		/**
		 * Execute a Delete request 
		 * @return null
		 */
		@Override
		public Collection<EntityModel> execute() throws RuntimeException {

			Collection<EntityModel> newEntityModels = null;
			String url = urlBuilder(urlDomain,queryParams);
			GenericUrl urlDomain = new GenericUrl(url);
			
			try{
				HttpRequest httpRequest = requestFactory.buildDeleteRequest(urlDomain);
				newEntityModels = getEntitiesResponse(httpRequest);
			}
			catch (Exception e){
				
				handleException(e,false);
			}
			
			return newEntityModels;
			
		}
		
		/**
		 * Update Delete with new Query parameters
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
	 *
	 */
	public  class Entities {

		private  int iEntityId = 0;

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
		 *
		 */
		public class Get extends NGARequest<EntityModel> {

			private String fieldsParams = "";
			
			
			/**
			 * 
			 * 1. Get Request execution with jason data 2. Parse response to a
			 * new Collection<EntityModel> object
			 */
			@Override
			public EntityModel execute() throws RuntimeException {

				EntityModel newEntityModel = null;
				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				String url = urlBuilder(domain,fieldsParams);
				GenericUrl urlDomain = new GenericUrl(url);
				try{
					HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
					httpRequest.getHeaders().setAccept(HTTP_APPLICATION_JASON_VALUE);
					newEntityModel = getEntityResponse(httpRequest);
				}
				catch (Exception e){
					
					handleException(e,false);
				}
				
				return newEntityModel;
				
			}
			
			/**
			 * Get binary data
			 * @return - Stream with binary data
			 * @throws RuntimeException
			 */
			public InputStream executeBinary() throws RuntimeException {

				InputStream inputStream = null;
				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				String url = urlBuilder(domain,fieldsParams);

				GenericUrl urlDomain = new GenericUrl(url);
				try{
					HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
					httpRequest.getHeaders().setAccept(HTTP_APPLICATION_OCTET_STREAM_VALUE);
					logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url,httpRequest.getHeaders().toString()));
				
					HttpResponse response = httpRequest.execute();
					logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage(),response.getHeaders().toString()));
					
					if (response.isSuccessStatusCode()) {

						inputStream = response.getContent();
					}
					
					// Update request factory with the latest response Cookie
					requestFactory.getInitializer().initialize(httpRequest.setContent(ByteArrayContent.fromString(null, response.toString())));
				
				}
				catch (Exception e){
						
					handleException(e,false);
					
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

				fieldsParams += String.join(",", fields) + ",";
				return this;
			}
		}

		/**
		 * This class hold the Update object of one entity
		 * 
		 * @author Moris Oz
		 *
		 */
		public class Update extends NGARequest<EntityModel> {

			private EntityModel entityModel;

			/**
			 * 
			 * 1. Update Request execution with jason data 2. Parse response to
			 * a new EntityModel object
			 * 
			 * @throws RuntimeException
			 */
			@Override
			public EntityModel execute() throws RuntimeException {

				EntityModel newEntityModel = null;
				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				GenericUrl urlDomain = new GenericUrl(domain);
				JSONObject objBase = getEntityJSONObject(entityModel);
				String jasonEntityModel = objBase.toString();
				
				try{
					HttpRequest httpRequest = requestFactory.buildPutRequest(urlDomain,
							ByteArrayContent.fromString(null, jasonEntityModel));

					// add default headers
					httpRequest.getHeaders().setContentType(HTTP_APPLICATION_JASON_VALUE);
					httpRequest.getHeaders().setAccept(HTTP_APPLICATION_JASON_VALUE);
					newEntityModel = getEntityResponse(httpRequest);
				}
				catch (Exception e){
					
					handleException(e,false);
				}
				
				return newEntityModel;

			}

			/**
			 * set a new entity for updating
			 * 
			 * @param entityModel
			 * @return an update object with new entity
			 */
			public Update entity(EntityModel entModel) {
				entityModel = entModel;
				return this;
			}
		}

		/**
		 * This class hold the Delete object of one entity
		 * 
		 * @author Moris Oz
		 *
		 */
		public class Delete extends NGARequest<EntityModel> {

			/**
			 * 
			 * 1. Get Request execution with jason data 2. Parse response to a
			 * new Collection<EntityModel> object
			 */
			@Override
			public EntityModel execute() throws RuntimeException {

				EntityModel newEntityModel = null;
				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				String url = urlBuilder(domain);
				GenericUrl urlDomain = new GenericUrl(url);
				try{
					HttpRequest httpRequest = requestFactory.buildDeleteRequest(urlDomain);
					newEntityModel = getEntityResponse(httpRequest);
				}
				catch (Exception e){
					
					handleException(e,false);
				}
				
				return newEntityModel;
				
			}
		}
	}
}

package com.hpe.adm.nga.sdk;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.MultipartContent;
import com.google.gson.Gson;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel.ReferenceModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This class hold the entities objects and serve all functunality concern to
 * entities.
 * 
 * @author moris oz
 *
 */
public class EntityListService {

	// constant
	private static final String JSON_DATA_NAME = "data";
	private static final String JSON_ID_NAME = "id";
	private static final String JSON_TYPE_NAME = "type";
	private static final String JSON_TOTAL_COUNT_NAME = "total_count";
	private static final String JSON_EXCEEDS_TOTAL_COUNT_NAME = "exceeds_total_count";
	private static final String JSON_NULL_VALUE = "null";
	private static final String LIMIT_PARAM_FORMAT = "limit=%d&";
	private static final String OFFSET_PARAM_FORMAT = "offset=%d&";
	private static final String FIELDS_PARAM_FORMAT = "fields=%s";
	private static final String ORDER_BY_PARAM_FORMAT = "order_by=%s";
	private static final String QUERY_PARAM_FORMAT = "query=\"%s\"";
	private static final String LOGGER_REQUEST_FORMAT = "Request: %s - %s";
	private static final String LOGGER_REQUEST_CONTENT_FORMAT = "Request: %s - %s:%s";
	private static final String LOGGER_RESPONSE_FORMAT = "Response: %s:%s";
	private static final String HTTP_MEDIA_TYPE_MULTIPART_NAME = "multipart/form-data";
	private static final String HTTP_MULTIPART_BOUNDARY_NAME = "boundary";
	private static final String HTTP_MULTIPART_BOUNDARY_VALUE = "__END_OF_PART__";
	private static final String HTTP_MULTIPART_PART_DISPOSITION_NAME = "Content-Disposition";
	private static final String HTTP_MULTIPART_PART1_DISPOSITION_FORMAT = "form-data; name=\"%s\"";
	private static final String HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE = "entity";
	private static final String HTTP_MULTIPART_PART2_DISPOSITION_FORMAT = "form-data; name=\"content\"; filename=\"%s\"";
	private static final String HTTP_APPLICATION_JASON_VALUE = "application/json";
	private static final String HTTP_APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
	private static final String LOGGER_INVALID_FIELD_SCHEME_FORMAT = " field scheme is invalid";
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
	 * @throws JSONException
	 */
	public Collection<EntityModel> testGetEntityModels(String jason) throws JSONException {

		Collection<EntityModel> entityModels = null;

		JSONObject jasonObj = new JSONObject(jason);
		JSONArray jasoDataArr = jasonObj.getJSONArray(JSON_DATA_NAME);

		// prepare entity collection
		entityModels = new ArrayList<EntityModel>();
		Set<FieldModel> setFieldModel = null;
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jasoEntityObj);
			entityModels.add(entityModel);
		}

		return entityModels;
	}

	
	/**
	 * A utility class for building a URIs with various components, based on the
	 * given domain name and the global quarry parameters of entity list.
	 * 
	 * @param urlDomain
	 *            - domain name
	 * @return url string ready to transmit
	 * @throws UnsupportedEncodingException
	 */
	protected String urlBuilder(String urlDomain,String fieldsParams,String orderByParam,long limitParam,long ofsetParam,Query queryParams) throws UnsupportedEncodingException {

		// Construct url paramters
		fieldsParams = (fieldsParams != null && !fieldsParams.isEmpty())
				? String.format(FIELDS_PARAM_FORMAT, fieldsParams) : "";
		fieldsParams = (fieldsParams != null && !fieldsParams.isEmpty())
				? fieldsParams.substring(0, fieldsParams.length() - 1) + "&" : "";
		String limitParamString = limitParam != 0 ? String.format(LIMIT_PARAM_FORMAT, limitParam) : "";
		String OfsetParamString = ofsetParam != 0 ? String.format(OFFSET_PARAM_FORMAT, ofsetParam) : "";
		orderByParam = (orderByParam != null && !orderByParam.isEmpty())
				? String.format(ORDER_BY_PARAM_FORMAT, orderByParam) : "";
		orderByParam = (orderByParam != null && !orderByParam.isEmpty())
				? orderByParam.substring(0, orderByParam.length() - 1) + "&" : "";
		String queryParamsString = queryParams != null ? String.format(QUERY_PARAM_FORMAT, queryParams.getQueryString())
				: "";
		String params = fieldsParams + limitParamString + OfsetParamString + orderByParam + queryParamsString;
		params = !params.isEmpty() && params.charAt(params.length() - 1) == '&'
				? params.substring(0, params.length() - 1) : params;
		params = params.isEmpty() ? "" : "?" + params;

		String res = urlDomain + params;
		return res;

	}
	
	protected String urlBuilder(String urlDomain,Query queryParams) throws UnsupportedEncodingException {

		
		return urlBuilder(urlDomain,"","",0,0,queryParams);

	}
	
	protected String urlBuilder(String urlDomain) throws UnsupportedEncodingException {

		
		return urlBuilder(urlDomain,null);

	}

	/**
	 * get a entity model collection based on a given jason string
	 * 
	 * @param json
	 * @return entity model collection based on a given jason string
	 * @throws JSONException
	 */
	protected  Collection<EntityModel> getEntities(String json) throws JSONException {

		JSONTokener tokener = new JSONTokener(json);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoDataArr = jasoObj.getJSONArray(JSON_DATA_NAME);

		// prepare entity collection
		Collection<EntityModel> entityModels = new ArrayList<EntityModel>();
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jasoEntityObj);
			entityModels.add(entityModel);
		}

		return entityModels;
	}

	/**
	 * get a new jason object based on a given EntityModel object
	 * 
	 * @param entityModel
	 * @return new jason object based on a given EntityModel object
	 * @throws JSONException
	 */
	protected JSONObject getEntityJSONObject(EntityModel entityModel) throws JSONException {

		Set<FieldModel> fieldModels = entityModel.getValue();

		JSONObject objField = new JSONObject();
		for (Iterator iterator2 = fieldModels.iterator(); iterator2.hasNext();) {
			FieldModel fieldModel = (FieldModel) iterator2.next();

			if (fieldModel.getClass() == LongFieldModel.class) {
				objField.put(fieldModel.getName(), fieldModel.getValue());

			} else if (fieldModel.getClass() == ReferenceFieldModel.class) {
				ReferenceModel referenceModel = ((ReferenceFieldModel) fieldModel).getValue();
				JSONObject objref = null;

				if (referenceModel != null) {
					objref = new JSONObject();
					objref.put(JSON_ID_NAME, referenceModel.getId());
					objref.put(JSON_TYPE_NAME, referenceModel.gettype());
					objField.put(fieldModel.getName(), objref);
				} else {
					objField.put(fieldModel.getName(), JSONObject.NULL);
				}

			} else if (fieldModel.getClass() == MultiReferenceFieldModel.class) {

				String Jason = new Gson().toJson(fieldModel);
				JSONObject objref = new JSONObject(Jason);
				JSONArray objArray = objref.getJSONArray("data");
				objref = new JSONObject();
				objref.put(JSON_TOTAL_COUNT_NAME, objArray.length());
				objref.put(JSON_DATA_NAME, objArray);
				objField.put(fieldModel.getName(), objref);

			} else {

				objField.put(fieldModel.getName(), fieldModel.getValue());
			}

		}

		return objField;
	}

	/**
	 * get a new jason object based on a given EntityModel list
	 * 
	 * @param colEntities
	 *            - Collection of entities models
	 * @return new jason object conatin entities data
	 * @throws JSONException
	 */
	protected JSONObject getEntitiesJSONObject(Collection<EntityModel> entitiesModels) throws JSONException {

		JSONObject objBase = new JSONObject();
		JSONArray objEntities = new JSONArray();
		objBase.put(JSON_DATA_NAME, objEntities);
		objBase.put(JSON_TOTAL_COUNT_NAME, entitiesModels.size());
		objBase.put(JSON_EXCEEDS_TOTAL_COUNT_NAME, false);

		for (Iterator iterator1 = entitiesModels.iterator(); iterator1.hasNext();) {
			EntityModel entityModel = (EntityModel) iterator1.next();
			Set<FieldModel> setFieldModel = entityModel.getValue();
			JSONObject objField = getEntityJSONObject(entityModel);
			objEntities.put(objField);
		}

		return objBase;

	}

	/**
	 * get a new EntityModel object based on jason object
	 * 
	 * @param jasoEntityObj
	 *            - Jason object
	 * @return new EntityModel object
	 * @throws JSONException
	 */
	protected EntityModel getEntityModel(JSONObject jasoEntityObj) throws JSONException {

		Set<FieldModel> fieldModels = new HashSet<FieldModel>();
		Iterator<?> keys = jasoEntityObj.keys();
		EntityModel entityModel = null;

		while (keys.hasNext()) {

			FieldModel fldModel = null;
			String strKey = (String) keys.next();
			Object aObj = jasoEntityObj.get(strKey);
			if(aObj instanceof Long || aObj instanceof Integer){
				fldModel = new LongFieldModel(strKey, Long.parseLong(aObj.toString()));
			}
			else if( aObj instanceof JSONArray){

				fldModel = new Gson().fromJson(aObj.toString(), MultiReferenceFieldModel.class);
				((MultiReferenceFieldModel) fldModel).setName(strKey);
			}
			else if( aObj instanceof JSONObject || aObj==JSONObject.NULL){
				ReferenceModel ref = null;
				if (aObj!=JSONObject.NULL) {
					ref = new Gson().fromJson(aObj.toString(), ReferenceModel.class);
				}

				fldModel = new ReferenceFieldModel(strKey, ref);
			}
			else if( aObj instanceof String || aObj instanceof Boolean){
				fldModel = new StringFieldModel(strKey, aObj.toString());
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
	 * Check if String represent a jason Object
	 * 
	 * @param str
	 * @return true if String is a jason Object
	 */
	/*protected  boolean isJasonObject(String str) {

		try {
			new JSONObject(str);
			return true;
		} catch (JSONException e) {
			return false;
		}

	}

	/**
	 * Check if String represent a jason array
	 * 
	 * @param str
	 * @return true if String is a jason array
	 */
	/*protected boolean isJasonArray(String str) {

		try {
			JSONObject jasonObj = new JSONObject(str);
			try {
				jasonObj.getJSONArray(JSON_DATA_NAME);
				return true;
			} catch (JSONException e) {
				return false;
			}
		} catch (JSONException e) {
			return false;
		}

	}

	/**
	 * Check if string represent a long type
	 * 
	 * @param str
	 * @return true if string is a long type
	 */
	/*protected  boolean isLong(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * set Fields Parameters
	 * 
	 * @param fields
	 */
	/*protected  void setFieldsBuilder(String... fields) {

		fieldsParams += String.join(",", fields) + ",";
		 
	}*/

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
		private long limitParam = 0;
		private long ofsetParam = 0;
		private Query queryParams = null;

		

		// Public

		/**
		 * 1. Request Get Execution 2. Parse response to a new Collection
		 * <EntityModel> object
		 */
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {

			String url = urlBuilder(urlDomain,fieldsParams,orderByParam,limitParam,ofsetParam,queryParams);

			GenericUrl domain = new GenericUrl(url);
			HttpRequest httpRequest = requestFactory.buildGetRequest(domain);
			logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url));
			HttpResponse response = httpRequest.execute();
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
			httpRequest.getHeaders().setAccept("application/json");
			Collection<EntityModel> entityModels = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				entityModels = getEntities(json);
			}

			return entityModels;
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
			ofsetParam = offset;
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

		private Collection<EntityModel> entityModels;
		private Query queryParams = null;
		
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {

			String url = urlBuilder(urlDomain,queryParams);
			GenericUrl domain = new GenericUrl(url);
			JSONObject objBase = getEntitiesJSONObject(entityModels);
			String jasonEntityModel = objBase.toString();

			HttpRequest httpRequest = requestFactory.buildPutRequest(domain,
					ByteArrayContent.fromString(null, jasonEntityModel));

			// add default headers
			httpRequest.getHeaders().setContentType("application/json");
			httpRequest.getHeaders().setAccept("application/json");
			logger.debug(
					String.format(LOGGER_REQUEST_CONTENT_FORMAT, httpRequest.getRequestMethod(), urlDomain, jasonEntityModel));
			HttpResponse response = httpRequest.execute();
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
			Collection<EntityModel> entityModels = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				entityModels = getEntities(json);

			}

			return entityModels;

		}

		/**
		 * 
		 * @param query
		 * @return
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

		private Collection<EntityModel> entityModels;

		/**
		 * 1. build Entity Jason Object from Collection<EntityModel> 2. Post
		 * Request execution with jason data 3. Parse response to a new
		 * Collection<EntityModel> object
		 */
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {

			String url = urlBuilder(urlDomain);
			GenericUrl urlDomain = new GenericUrl(url);
			JSONObject objBase = getEntitiesJSONObject(entityModels);
			String strJasonEntityModel = objBase.toString();

			HttpRequest httpRequest = requestFactory.buildPostRequest(urlDomain,
					ByteArrayContent.fromString(null, strJasonEntityModel));

			// add default headers
			httpRequest.getHeaders().setContentType("application/json");
			httpRequest.getHeaders().setAccept("application/json");
			logger.debug(String.format(LOGGER_REQUEST_CONTENT_FORMAT, httpRequest.getRequestMethod(), urlDomain,
					strJasonEntityModel));
			HttpResponse response = httpRequest.execute();
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
			Collection<EntityModel> newEntityModels = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				newEntityModels = getEntities(json);

			}

			return newEntityModels;

		}

		public Collection<EntityModel> executeMultipart(Collection<EntityModel> entities, String strFileName)
				throws IOException, JSONException {

			String url = urlBuilder(urlDomain);
			GenericUrl urlDomain = new GenericUrl(url);

			JSONObject objBase = getEntitiesJSONObject(entities);
			String strJasonEntityModel = objBase.toString();
			
			// Add parameters
			MultipartContent content = new MultipartContent()
					.setMediaType(new HttpMediaType(HTTP_MEDIA_TYPE_MULTIPART_NAME).setParameter(HTTP_MULTIPART_BOUNDARY_NAME, HTTP_MULTIPART_BOUNDARY_VALUE));

			
			MultipartContent.Part part1 = new MultipartContent.Part(
					ByteArrayContent.fromString(null, strJasonEntityModel));
			part1.setHeaders(
					new HttpHeaders().set(HTTP_MULTIPART_PART_DISPOSITION_NAME, String.format(HTTP_MULTIPART_PART1_DISPOSITION_FORMAT, HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE)));

			content.addPart(part1);
			

			// Add file
			File file = new File(strFileName);
			FileContent fileContent = new FileContent(HTTP_APPLICATION_JASON_VALUE, file);
			MultipartContent.Part part2 = new MultipartContent.Part(fileContent);
			part2.setHeaders(new HttpHeaders().set(HTTP_MULTIPART_PART_DISPOSITION_NAME,
					String.format(HTTP_MULTIPART_PART2_DISPOSITION_FORMAT, strFileName)));
			content.addPart(part2);

			HttpRequest httpRequest = requestFactory.buildPostRequest(urlDomain, content);
			httpRequest.execute();
			// content.writeTo(System.out);
			logger.debug(String.format(LOGGER_REQUEST_CONTENT_FORMAT, httpRequest.getRequestMethod(), urlDomain,
					strJasonEntityModel));
			HttpResponse response = httpRequest.execute();
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
			Collection<EntityModel> newEntityModels = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				newEntityModels = getEntities(json);

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
		
		@Override
		public Collection<EntityModel> execute() throws IOException {

			String url = urlBuilder(urlDomain,queryParams);

			GenericUrl urlDomain = new GenericUrl(url);
			HttpRequest httpRequest = requestFactory.buildDeleteRequest(urlDomain);
			logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url));
			HttpResponse response = httpRequest.execute();
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
			if (response.isSuccessStatusCode()) {

			}

			return null;
		}

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

			//init();
			return new Get();
		}

		/**
		 * getter of a Update object with specific entity
		 * 
		 * @return
		 */
		public Update update() {

			//init();
			return new Update();
		}

		/**
		 * getter of a Create object with specific entity
		 * 
		 * @return
		 */
		public Delete delete() {
			//init();
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
			private String orderByParam = "";
			private long limitParam = 0;
			private long ofsetParam = 0;
			private Query queryParams = null;
			
			/**
			 * 
			 * 1. Get Request execution with jason data 2. Parse response to a
			 * new Collection<EntityModel> object
			 */
			@Override
			public EntityModel execute() throws JSONException, IOException {

				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				String url = urlBuilder(domain,fieldsParams,orderByParam,limitParam,ofsetParam,queryParams);

				GenericUrl urlDomain = new GenericUrl(url);

				HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
				httpRequest.getHeaders().setAccept(HTTP_APPLICATION_JASON_VALUE);
				logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url));
				HttpResponse response = httpRequest.execute();
				logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
				EntityModel newEntityModel = null;

				if (response.isSuccessStatusCode()) {

					String json = response.parseAsString();
					JSONTokener tokener = new JSONTokener(json);
					JSONObject jasoObj = new JSONObject(tokener);
					newEntityModel = getEntityModel(jasoObj);

				}

				return newEntityModel;
			}

			public InputStream executeBinary() throws JSONException, IOException {

				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				String url = urlBuilder(domain,fieldsParams,orderByParam,limitParam,ofsetParam,queryParams);

				GenericUrl urlDomain = new GenericUrl(url);

				HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
				httpRequest.getHeaders().setAccept(HTTP_APPLICATION_OCTET_STREAM_VALUE);
				logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url));
				HttpResponse response = httpRequest.execute();
				logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));

				if (response.isSuccessStatusCode()) {

					return response.getContent();

				}

				return null;
			}

			/**
			 * Set Fields Parameters
			 * 
			 * @param fields
			 * @return a new Get object with new Fields Parameters
			 */
			public Get addFields(String... fields) {

				//setFieldsBuilder(fields);
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
			 * @throws IOException
			 * @throws JSONException
			 */
			@Override
			public EntityModel execute() throws IOException, JSONException {

				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				GenericUrl urlDomain = new GenericUrl(domain);
				JSONObject objBase = getEntityJSONObject(entityModel);
				String jasonEntityModel = objBase.toString();

				HttpRequest httpRequest = requestFactory.buildPutRequest(urlDomain,
						ByteArrayContent.fromString(null, jasonEntityModel));

				// add default headers
				httpRequest.getHeaders().setContentType(HTTP_APPLICATION_JASON_VALUE);
				httpRequest.getHeaders().setAccept(HTTP_APPLICATION_JASON_VALUE);
				logger.debug(
						String.format(LOGGER_REQUEST_CONTENT_FORMAT, httpRequest.getRequestMethod(), domain, jasonEntityModel));
				HttpResponse response = httpRequest.execute();
				logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
				EntityModel newEntityModel = null;

				if (response.isSuccessStatusCode()) {

					String json = response.parseAsString();
					JSONTokener tokener = new JSONTokener(json);
					JSONObject jasoObj = new JSONObject(tokener);
					newEntityModel = getEntityModel(jasoObj);

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
		 * @author mor4095
		 *
		 */
		public class Delete extends NGARequest<EntityModel> {

			/**
			 * 
			 * 1. Get Request execution with jason data 2. Parse response to a
			 * new Collection<EntityModel> object
			 */
			@Override
			public EntityModel execute() throws IOException, JSONException {

				String domain = urlDomain + "/" + String.valueOf(iEntityId);
				String url = urlBuilder(domain);

				GenericUrl urlDomain = new GenericUrl(url);

				HttpRequest httpRequest = requestFactory.buildDeleteRequest(urlDomain);
				logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url));
				HttpResponse response = httpRequest.execute();
				logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
				EntityModel newEntityModel = null;

				if (response.isSuccessStatusCode()) {

					String json = response.parseAsString();
					if (!json.isEmpty()) {
						JSONTokener tokener = new JSONTokener(json);
						JSONObject jasoObj = new JSONObject(tokener);
						newEntityModel = getEntityModel(jasoObj);
					}
				}

				return newEntityModel;
			}
		}
	}
}

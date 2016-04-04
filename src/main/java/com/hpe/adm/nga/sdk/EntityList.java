package com.hpe.adm.nga.sdk;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Charsets;
import com.google.api.client.util.StringUtils;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel.ReferenceModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.client.utils.URIUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This class hold the entities objects and serve all functunality concern to entities.
 * 
 * @author moris oz
 *
 */
public class EntityList {

	//Constants
	private static String strUrlDomain = "";
	private static String strFieldsParams = "";
	private static String strOrderByParam = "";
	private static long lLimitParam = 0;
	private static long lOfsetParam = 0;
	private static Query queryParams = null;
	private static HttpRequestFactory requestFactory = null;
	
	// **** public Functions ***
	
	/**
	 * Creates a new EntityList object 
	 * @param reqFactory - Http Request Factory
	 * @param strEntityListDomain - Domain Name 
	 */
	public EntityList(HttpRequestFactory reqFactory, String strEntityListDomain) {

		strUrlDomain = strEntityListDomain;
		requestFactory = reqFactory;
	}
	
	/**
	 * getter of an Entities object ( Entities object handle a unique entity model ) 
	 * @param entityId - entity id
	 * @return a new Entities object with specific id
	 */
	public Entities at(int entityId) {
		return new Entities(entityId);
	}
	
	/**
	 * getter of an Get object of EntityList ( EntityList object handle a collection of entity models  )
	 * @return a new Get object 
	 */
	public Get get() {
		init();
		return new Get();
	}
	
	/**
	 * getter of an Update object of EntityList ( EntityList object handle a collection of entity models  )
	 * @return a new Update object 
	 */
	public Update update() {
		init();
		return new Update();
	}
	
	/**
	 * getter of an Create object of EntityList ( EntityList object handle a collection of entity models
	 * @return a new Create object 
	 */
	public Create create() {
		init();
		return new Create();
	}
	/**
	 * getter of an Delete object of EntityList ( EntityList object handle a collection of entity models
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
	public Collection<EntityModel> testGetEntityModels(String Jason) throws JSONException {

		Collection<EntityModel> colEntityModel = null;

		JSONObject jasonObj = new JSONObject(Jason);
		JSONArray jasoDataArr = jasonObj.getJSONArray("data");

		// prepare entity collection
		colEntityModel = new ArrayList<EntityModel>();
		Set<FieldModel> setFieldModel = null;
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jasoEntityObj);
			colEntityModel.add(entityModel);
		}

		return colEntityModel;
	}

	// ****protected static Functions ***
	
	/**
	 * basic init of class variables
	 */
	protected static void init() {

		strFieldsParams = "";
		strOrderByParam = "";
		lLimitParam = 0;
		lOfsetParam = 0;
		queryParams = null;
	}
	
	/**
	 *  A utility class for building a URIs with various components, 
	 *  based on the given domain name and the global quarry parameters of entity list.
	 * @param urlDomain - domain name
	 * @return url string ready to transmit 
	 * @throws UnsupportedEncodingException 
	 */
	protected static String urlBuilder(String urlDomain) throws UnsupportedEncodingException {

		// Construct url paramters
		strFieldsParams = strFieldsParams != "" ? "fields=" + strFieldsParams : "";
		strFieldsParams = strFieldsParams.isEmpty() ? ""
				: strFieldsParams.substring(0, strFieldsParams.length() - 1) + "&";
		String strLimitParam = lLimitParam != 0 ? "limit=" + String.valueOf(lLimitParam) + "&" : "";
		String strOfsetParam = lOfsetParam != 0 ? "offset=" + String.valueOf(lOfsetParam) + "&" : "";
		strOrderByParam = strOrderByParam != "" ? "order_by=" + strOrderByParam : "";
		strOrderByParam = strOrderByParam.isEmpty() ? ""
				: strOrderByParam.substring(0, strOrderByParam.length() - 1) + "&";
		
		
		//String strQueryParams = queryParams==null ? "" : URLEncoder.encode("query=\"" + queryParams.getQueryString() + "\"","UTF-8");
		String strQueryParams = queryParams==null ? "" : ("query=\"" + queryParams.getQueryString() + "\"");
		String params = strFieldsParams + strLimitParam + strOfsetParam + strOrderByParam + strQueryParams;

		params = params.isEmpty() ? "" : "?" + params.substring(0, params.length() - 1);
		
		
		String res = urlDomain + params;
		return res;

	}
	
	/**
	 * get a entity model collection based on a given jason string
	 * @param json
	 * @return entity model collection based on a given jason string
	 * @throws JSONException
	 */
	protected static Collection<EntityModel> getEntities(String json) throws JSONException {
		
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoDataArr = jasoObj.getJSONArray("data");

		// prepare entity collection
		Collection<EntityModel> colEntityModel = new ArrayList<EntityModel>();
		Set<FieldModel> setFieldModel = null;
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityModel entityModel = getEntityModel(jasoEntityObj);
			colEntityModel.add(entityModel);

		}
		
		return colEntityModel;
	}
	
	/**
	 * get a new jason object based on a given EntityModel object
	 * @param entityModel
	 * @return new jason object based on a given EntityModel object
	 * @throws JSONException
	 */
	protected static JSONObject getEntityJSONObject(EntityModel entityModel) throws JSONException {
		
		Set<FieldModel> setFieldModel = entityModel.getValue();

		JSONObject objField = new JSONObject();
		for (Iterator iterator2 = setFieldModel.iterator(); iterator2.hasNext();) {
			FieldModel fieldModel = (FieldModel) iterator2.next();

			if (fieldModel.getClass() == LongFieldModel.class) {
				objField.put(fieldModel.getName(), fieldModel.getValue());

			} else if (fieldModel.getClass() == ReferenceFieldModel.class) {
				ReferenceModel referenceModel = ((ReferenceFieldModel) fieldModel).getValue();
				JSONObject objref = null;

				if (referenceModel != null) {
					objref = new JSONObject();
					objref.put("id", referenceModel.getId());
					objref.put("type", referenceModel.gettype());
					objField.put(fieldModel.getName(), objref);
				} else {
					objField.put(fieldModel.getName(), JSONObject.NULL);
				}

			} else if (fieldModel.getClass() == MultiReferenceFieldModel.class) {

				String Jason = new Gson().toJson(fieldModel);
				JSONObject objref = new JSONObject(Jason);
				JSONArray objArray = objref.getJSONArray("data");
				objref = new JSONObject();
				objref.put("total_count", objArray.length());
				objref.put("data", objArray);

				// JSONObject objref2 = new JSONObject();
				objField.put(fieldModel.getName(), objref);
				

			} else {

				objField.put(fieldModel.getName(), fieldModel.getValue());
			}

		}
		
		return objField;
	}
	
	/**
	 * get a new jason object based on a given EntityModel list
	 * @param colEntities - Collection of entities models
	 * @return new jason object conatin entities data
	 * @throws JSONException
	 */
	protected static JSONObject getEntitiesJSONObject(Collection<EntityModel> colEntities) throws JSONException {
	
		JSONObject objBase = new JSONObject();
		JSONArray objEntities = new JSONArray();
		objBase.put("data", objEntities);
		objBase.put("total_count", 158);
		objBase.put("exceeds_total_count", false);

		for (Iterator iterator1 = colEntities.iterator(); iterator1.hasNext();) {
			EntityModel entityModel = (EntityModel) iterator1.next();
			JSONObject objEntitye = new JSONObject();
			
			Set<FieldModel> setFieldModel = entityModel.getValue();

			JSONObject objField = getEntityJSONObject(entityModel);
			/*for (Iterator iterator2 = setFieldModel.iterator(); iterator2.hasNext();) {
				FieldModel fieldModel = (FieldModel) iterator2.next();

				if (fieldModel.getClass() == LongFieldModel.class) {
					objField.put(fieldModel.getName(), fieldModel.getValue());

				} else if (fieldModel.getClass() == ReferenceFieldModel.class) {
					ReferenceModel referenceModel = ((ReferenceFieldModel) fieldModel).getValue();
					JSONObject objref = null;

					if (referenceModel != null) {
						objref = new JSONObject();
						objref.put("id", referenceModel.getId());
						objref.put("type", referenceModel.gettype());
						objField.put(fieldModel.getName(), objref);
					} else {
						objField.put(fieldModel.getName(), JSONObject.NULL);
					}

				} else if (fieldModel.getClass() == MultiReferenceFieldModel.class) {

					String Jason = new Gson().toJson(fieldModel);
					JSONObject objref = new JSONObject(Jason);
					JSONArray objArray = objref.getJSONArray("data");
					objref = new JSONObject();
					objref.put("total_count", objArray.length());
					objref.put("data", objArray);

					// JSONObject objref2 = new JSONObject();
					objField.put(fieldModel.getName(), objref);
					

				} else {

					objField.put(fieldModel.getName(), fieldModel.getValue());
				}

			}*/

			objEntities.put(objField);

		}

		return objBase;
				
	}
				
	/**
	 * get a new EntityModel object based on jason object
	 * @param jasoEntityObj - Jason object
	 * @return new EntityModel object
	 * @throws JSONException
	 */
	protected static EntityModel getEntityModel(JSONObject jasoEntityObj) throws JSONException {

		Set<FieldModel> setFieldModel = new HashSet<FieldModel>();
		Iterator<?> keys = jasoEntityObj.keys();
		EntityModel entityModel = null;

		while (keys.hasNext()) {

			String strKey = (String) keys.next();
			String strValue = jasoEntityObj.getString(strKey);
			FieldModel fldModel = null;


			if (isLong(strValue)) {
				fldModel = new LongFieldModel(strKey, Long.parseLong(strValue));
			} else if (isJasonArray(strValue)) {
				JSONObject jasonObj = new JSONObject(strValue);
				JSONArray jasonArr = jasonObj.getJSONArray("data");

				fldModel = new Gson().fromJson(strValue, MultiReferenceFieldModel.class);
				((MultiReferenceFieldModel) fldModel).setName(strKey);

			} else if (isJasonObject(strValue) || strValue.equals("null")) {
				ReferenceModel ref = null;
				if (!strValue.equals("null")) {
					JSONObject jasoref = new JSONObject(strValue);
					ref = new ReferenceModel(jasoref.getLong("id"), jasoref.getString("type"));
				}

				fldModel = new ReferenceFieldModel(strKey, ref);
			} else {

				fldModel = new StringFieldModel(strKey, strValue);
			}

			setFieldModel.add(fldModel);

		}

		entityModel = new EntityModel(setFieldModel);
		return entityModel;
	}
	
	/**
	 * Check if String represent a jason Object
	 * @param str
	 * @return true if String is a jason Object
	 */
	protected static boolean isJasonObject(String str) {

		try {
			new JSONObject(str);
			return true;
		} catch (JSONException e) {
			return false;
		}

	}
	
	/**
	 * Check if String represent a jason array
	 * @param str
	 * @return true if String is a jason array
	 */
	protected static boolean isJasonArray(String str) {

		try {
			JSONObject jasonObj = new JSONObject(str);
			try {
				jasonObj.getJSONArray("data");
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
	 * @param str
	 * @return true if string is a long type
	 */
	protected static boolean isLong(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// **** Classes ***
	/**
	 * This class hold the Get objects and serve all functions concern to REST Get.
	 * @author Moris Oz
	 *
	 */
	public static class Get extends NGARequest<Collection<EntityModel>> {

		// **** static Functions ***
		
		/**
		 * set Fields Parameters
		 * @param fields
		 */
		protected static void setFieldsBuilder(String... fields) {

			strFieldsParams += String.join(",", fields) + ",";
		}

		// Public
		
		/**
		 * 1. Request Get Execution
		 * 2. Parse response to a new Collection<EntityModel> object 
		 */
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {

			String strUrl = urlBuilder(strUrlDomain);
			
			
			GenericUrl urlDomain = new GenericUrl(strUrl);
			HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
			HttpResponse response = httpRequest.execute();
			Collection<EntityModel> colEntityModel = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				colEntityModel = getEntities(json);
			}

			return colEntityModel;
		}
		
		/**
		 * Add Fields parameters
		 * @param fields
		 * @return Get Object with new Fields parameters
		 */
		public Get addFields(String... fields) {

			strFieldsParams += String.join(",", fields) + ",";
			return this;
		}
		
		/**
		 * Add Limit parameter
		 * @param limit
		 * @return Get Object with new limit parameter
		 */
		public Get limit(int limit) {

			lLimitParam = limit;
			return this;
		}
		
		/**
		 * Add offset parameter
		 * @param offset
		 * @return Get Object with new offset parameter
		 */
		public Get offset(int offset) {
			lOfsetParam = offset;
			return this;
		}
		
		/**
		 * Add OrderBy parameters
		 * @param orderBy
		 * @param asc - true=ascending/false=descending
		 * @return Get Object with new OrderBy parameters
		 */
		public Get addOrderBy(String orderBy, boolean asc) {

			String strAsc = asc ? "" : "-";

			strOrderByParam += strAsc + String.join(",", orderBy) + ",";
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
	 * This class hold the Update objects and serve all functions concern to REST put.
	 * @author moris oz
	 *
	 */
	public static class Update extends NGARequest<Collection<EntityModel>> {
		
		private Collection<EntityModel> colEntities;
		
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {
			
			String strUrl = urlBuilder(strUrlDomain);
			GenericUrl urlDomain = new GenericUrl(strUrl);
			JSONObject objBase = getEntitiesJSONObject(colEntities);
			String strJasonEntityModel = objBase.toString();

			HttpRequest httpRequest = requestFactory.buildPutRequest(urlDomain,
					ByteArrayContent.fromString(null, strJasonEntityModel));
			

			// add default headers
			httpRequest.getHeaders().setContentType("application/json");
			httpRequest.getHeaders().setAccept("application/json");

			HttpResponse response = httpRequest.execute();
			Collection<EntityModel> colEntityModel = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				colEntityModel = getEntities(json);

			}

			return colEntityModel;
			
			
		}
		
		/**
		 * 
		 * @param query
		 * @return
		 */
		public Update query(Query query) {
			return this;
		}

		/**
		 * Set new entities collection 
		 * @param entities
		 * @return create Object with new entities collection 
		 */
		public Update entities(Collection<EntityModel> entities) {
			colEntities = entities;
			return this;
		}
				
	}
	
	/**
	 * This class hold the Update objects and serve all functions concern to REST Post.
	 * @author Moris Oz
	 *
	 */
	public static class Create extends NGARequest<Collection<EntityModel>> {

		private Collection<EntityModel> colEntities;
		
		/**
		 * 1.  build Entity Jason Object from Collection<EntityModel>
		 * 2.  Post Request execution with jason data
		 * 3.  Parse response to a new Collection<EntityModel> object 
		 */
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {

			String strUrl = urlBuilder(strUrlDomain);
			GenericUrl urlDomain = new GenericUrl(strUrl);
			JSONObject objBase = getEntitiesJSONObject(colEntities);
			String strJasonEntityModel = objBase.toString();

			HttpRequest httpRequest = requestFactory.buildPostRequest(urlDomain,
					ByteArrayContent.fromString(null, strJasonEntityModel));
			

			// add default headers
			httpRequest.getHeaders().setContentType("application/json");
			httpRequest.getHeaders().setAccept("application/json");

			HttpResponse response = httpRequest.execute();
			Collection<EntityModel> colEntityModel = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				colEntityModel = getEntities(json);
				
			}

			return colEntityModel;

		}
		
		/**
		 * Set new entities collection 
		 * @param entities
		 * @return create Object with new entities collection 
		 */
		public Create entities(Collection<EntityModel> entities) {

			colEntities = entities;
			return this;
		}
	}
	
	/**
	 * This class hold the Delete objects and serve all functions concern to REST delete.
	 * @author Moris Oz
	 *
	 */
	public static class Delete extends NGARequest<Collection<EntityModel>> {
		@Override
		public Collection<EntityModel> execute() {
			return null;
		}

		public Delete query(Query query) {
			return this;
		}
	}
	
	/**
	 * This class hold the Entities object(An object that represent one Entity )
	 * @author Moris Oz
	 *
	 */
	public static class Entities {

		private static int iEntityId = 0;
		
		/**
		 * Set entityId parameter
		 * @param entityId
		 */
		public Entities(int entityId) {
			iEntityId = entityId;
		}
		
		/**
		 * getter of a Get object with specific entity
		 * @return
		 */
		public Get get() {

			init();
			return new Get();
		}
		
		/**
		 * getter of a Update object with specific entity
		 * @return
		 */
		public Update update() {

			init();
			return new Update();
		}
		
		/**
		 * getter of a Create object with specific entity
		 * @return
		 */
		public Delete delete() {
			init();
			return new Delete();
		}
		
		/**
		 * This class hold the Get object of one entity
		 * @author Moris Oz
		 *
		 */
		public static class Get extends NGARequest<EntityModel> {
			
			/**
			 * 
			 * 1.  Get Request execution with jason data
			 * 2.  Parse response to a new Collection<EntityModel> object 
			 */
			@Override
			public EntityModel execute() throws JSONException, IOException {

				String strUrlDomain = EntityList.strUrlDomain + "/" + String.valueOf(iEntityId);
				String strUrl = EntityList.urlBuilder(strUrlDomain);

				GenericUrl urlDomain = new GenericUrl(strUrl);

				HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
				HttpResponse response = httpRequest.execute();
				EntityModel entityModel = null;

				if (response.isSuccessStatusCode()) {

					String json = response.parseAsString();
					JSONTokener tokener = new JSONTokener(json);
					JSONObject jasoObj = new JSONObject(tokener);
					entityModel = getEntityModel(jasoObj);

				}

				return entityModel;
			}
			
			/**
			 * Set Fields Parameters
			 * @param fields
			 * @return a new Get object with new Fields Parameters
			 */
			public Get addFields(String... fields) {

				EntityList.Get.setFieldsBuilder(fields);

				return this;
			}
		}
		
		/**
		 *   This class hold the Update object of one entity
		 * @author Moris Oz
		 *
		 */
		public static class Update extends NGARequest<EntityModel> {
			
			private EntityModel entityModel;
			
			/**
			 * 
			 * 1.  Update Request execution with jason data
			 * 2.  Parse response to a new EntityModel object 
			 * @throws IOException 
			 * @throws JSONException 
			 */
			@Override
			public EntityModel execute() throws IOException, JSONException {
				
				String strUrlDomain = EntityList.strUrlDomain + "/" + String.valueOf(iEntityId);
				GenericUrl urlDomain = new GenericUrl(strUrlDomain);
				JSONObject objBase = getEntityJSONObject(entityModel);
				String strJasonEntityModel = objBase.toString();

				HttpRequest httpRequest = requestFactory.buildPutRequest(urlDomain,
						ByteArrayContent.fromString(null, strJasonEntityModel));
				

				// add default headers
				httpRequest.getHeaders().setContentType("application/json");
				httpRequest.getHeaders().setAccept("application/json");

				HttpResponse response = httpRequest.execute();
				EntityModel entityModel = null;

				if (response.isSuccessStatusCode()) {

					String json = response.parseAsString();
					JSONTokener tokener = new JSONTokener(json);
					JSONObject jasoObj = new JSONObject(tokener);
					entityModel = getEntityModel(jasoObj);

				}

				return entityModel;
				
			}
			
			/**
			 * set a new entity for updating
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
		 * @author mor4095
		 *
		 */
		public static class Delete extends NGARequest<EntityModel> {
			
			/**
			 * 
			 * 1.  Get Request execution with jason data
			 * 2.  Parse response to a new Collection<EntityModel> object 
			 */
			@Override
			public EntityModel execute() throws IOException, JSONException {

				String strUrlDomain = EntityList.strUrlDomain + "/" + String.valueOf(iEntityId);
				String strUrl = EntityList.urlBuilder(strUrlDomain);

				GenericUrl urlDomain = new GenericUrl(strUrl);

				HttpRequest httpRequest = requestFactory.buildDeleteRequest(urlDomain);
				HttpResponse response = httpRequest.execute();
				EntityModel entityModel = null;

				if (response.isSuccessStatusCode()) {

					String json = response.parseAsString();
					if (!json.isEmpty()) {
						JSONTokener tokener = new JSONTokener(json);
						JSONObject jasoObj = new JSONObject(tokener);
						entityModel = getEntityModel(jasoObj);
					}
				}

				return entityModel;
			}
		}
	}
}

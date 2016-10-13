package com.hpe.adm.nga.sdk.metadata;

import com.google.api.client.http.*;
import com.google.gson.Gson;
import com.hpe.adm.nga.sdk.NGARequest;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.exception.NgaException;
import com.hpe.adm.nga.sdk.metadata.Features.*;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class hold the  metadata object and serve all functionality concern to fields metadata and entity metadata
 * @autho Moris oz
 *
 */
public class Metadata {

	// constant
	private static final String JSON_DATA_FIELD_NAME = "data";
	private static final String JSON_NAME_FIELD_NAME = "name";
	private static final String JSON_LABEL_FIELD_NAME = "label";
	private static final String JSON_CAN_MODIFY_LABEL_FIELD_NAME = "can_modify_label";
	private static final String JSON_FEATURES_FIELD_NAME = "features";
	private static final String QUERY_NAME_FIELD_NAME = "name";
	private static final String QUERY_ENTITY_NAME_FIELD_NAME = "entity_name";
	private static final String TYPE_NAME_ENTITIES_NAME = "entities";
	private static final String TYPE_NAME_FIELDS_NAME = "fields";
	private static final String TYPE_NAME_FIELDS_QUERY_FORMAT = "fields?query=\"";
	private static final String TYPE_NAME_ENTITIES_QUERY_FORMAT = "entities?query=\"";
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
	private static final String LOGGER_INVALID_FEATURE_FORMAT = ": not a valid feature";
	private static final String LOGGER_REQUEST_FORMAT = "Request: %s - %s - %s";
	private static final String LOGGER_RESPONSE_FORMAT = "Response: %d - %s - %s";	
	private static final String LOGGER_RESPONSE_JASON_FORMAT = "Response_Jason: %s";
	
	// private members
	private HttpRequestFactory requestFactory = null;
	private String urlDomain = "";
	private Logger logger = LogManager.getLogger(Metadata.class.getName());
	
	/**
	 * Creates a new Metadata object
	 * 
	 * @param reqFactory
	 *            - Http Request Factory
	 * @param strMetadataDomain
	 *            - metadata Domain Name
	 */
	public Metadata(HttpRequestFactory reqFactory, String strMetadataDomain){
		
		urlDomain = strMetadataDomain;
		requestFactory = reqFactory;
	}
	
	/**
	 * Get metadata entity object
	 * @return new metadata entity object
	 */
	public Entity entities(){
		
		return new Entity(TYPE_NAME_ENTITIES_NAME);
	}
	
	/**
	 * Get metadata entity object based on given entities names
	 * @return new metadata entity object
	 */
	public Entity entities(String...entities){
		
		
		List<String> entitiesList =  Arrays.asList(entities);
		
		String quaryList =  entitiesList
	            .stream()
	            .map(s -> new Query.QueryBuilder(QUERY_NAME_FIELD_NAME, Query::equalTo, s).getQueryString())
	            .collect(Collectors.joining("||"));
		
		// TBD - Remove after debugging
		/*Query quaryEntities =  new Query();
		String quaryList = "" ;	
		for (String value : entities) {
			
			quaryEntities = new Query.Field(QUERY_NAME_FIELD_NAME).equal(value).build();
			quaryList = quaryList + quaryEntities.getQueryString() + "||";
		}
		quaryList = quaryList.substring(0,quaryList.length()-2);*/
				
		return new Entity(TYPE_NAME_ENTITIES_QUERY_FORMAT + quaryList + "\"");
	}
	
	/**
	 * Get metadata field object
	 * @return
	 */
	public Field fields(){
		
		return new Field(TYPE_NAME_FIELDS_NAME);
	}
	
	/**
	 * Get metadata field object based on given field names
	 * @param entities
	 * @return
	 */
	public Field fields(String...entities){
		
		
		
		// TBD - remove after debugging
		/*String quaryList = "";	
		Query quaryFields =  new Query();
		for (String value : entities) {
			
			quaryFields = new Query.Field(QUERY_ENTITY_NAME_FIELD_NAME).equal(value).build();
			quaryList = quaryList + quaryFields.getQueryString() + "||";
		}
		quaryList = quaryList.substring(0,quaryList.length()-2);*/
		
		List<String> entitiesList =  Arrays.asList(entities);
		String quaryList =  entitiesList
	            .stream()
	            .map(s -> new Query.QueryBuilder(QUERY_ENTITY_NAME_FIELD_NAME, Query::equalTo, s).getQueryString())
	            .collect(Collectors.joining("||"));
	
		
		return new Field(TYPE_NAME_FIELDS_QUERY_FORMAT + quaryList + "\"");
	}
	
	/**
	 * Get Feature Object based on the feature jason object
	 * @param jasoFeatureObj - Jason Feature object
	 * @return Feature Object
	 */
	protected Feature getFeatureObject(JSONObject jasoFeatureObj){
		
		Feature feature = null;
		String featureName = jasoFeatureObj.getString(JSON_NAME_FIELD_NAME);
		
		switch (featureName) {
			case FEATURE_REST_NAME: 
				feature = new Gson().fromJson(jasoFeatureObj.toString(), RestFeature.class);
            break;
			case FEATURE_MAILING_NAME:  
        		feature = new Gson().fromJson(jasoFeatureObj.toString(), MailingFeature.class);
            break;
			case FEATURE_HAS_ATTACHMENTS_NAME:  
		 		feature = new Gson().fromJson(jasoFeatureObj.toString(), AttachmentsFeature.class);
            break;
			case FEATURE_HAS_COMMENTS_NAME:
		 		feature = new Gson().fromJson(jasoFeatureObj.toString(), CommentsFeature.class);
            break;
			case FEATURE_BUSINESS_RULES_NAME:  
		 		feature = new Gson().fromJson(jasoFeatureObj.toString(), BusinessRulesFeature.class);
            break;
			case FEATURE_SUBTYPES_NAME:  
        		feature = new Gson().fromJson(jasoFeatureObj.toString(), SubTypesFeature.class);
            break;
			case FEATURE_SUBTYPE_OF_NAME: 
				feature = new Gson().fromJson(jasoFeatureObj.toString(), SubTypesOfFeature.class);
            break;
			case FEATURE_HIERARCHY_NAME:
				feature = new Gson().fromJson(jasoFeatureObj.toString(), HierarchyFeature.class);
            break;
			case FEATURE_UDF_ENTITY_NAME:
				feature = new Gson().fromJson(jasoFeatureObj.toString(), UdfFearture.class);
				break;
			case FEATURE_ORDERING_ENTITY_NAME:
				feature = new Gson().fromJson(jasoFeatureObj.toString(), OrderingFeature.class);
				break;
			case FEATURE_GROUPING_ENTITY_NAME:
				feature = new Gson().fromJson(jasoFeatureObj.toString(), GroupingFeature.class);
				break;
			case FEATURE_PHASES_ENTITY_NAME:
				feature = new Gson().fromJson(jasoFeatureObj.toString(), PhasesFeature.class);
				break;
			case FEATURE_AUDITING_ENTITY_NAME:
				feature = new Gson().fromJson(jasoFeatureObj.toString(), AuditingFeature.class);
				break;
			default:
				logger.debug(featureName + LOGGER_INVALID_FEATURE_FORMAT);
			break;
		}
		
		return feature;
	}
	
	/**
	 * get a entities metadata collection based on a given jason string
	 * @param json
	 * @return entity metadata collection based on a given jason string
	 */
	protected  Collection<EntityMetadata> getEntitiesMetadata(String json)  {
		
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoDataArr = jasoObj.getJSONArray(JSON_DATA_FIELD_NAME);

		// prepare entity collection
		Collection<EntityMetadata> entitiesMetadata = new ArrayList<EntityMetadata>();
		IntStream.range(0, jasoDataArr.length()).forEach((i)->entitiesMetadata.add(getEntityMetadata(jasoDataArr.getJSONObject(i))));
		
		// TBD - Remove after debugging
		/*for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityMetadata entityModel = getEntityMetadata(jasoEntityObj);
			entitiesMetadata.add(entityModel);

		}*/
		
		return entitiesMetadata;
	}
	
	/**
	 * get a fields metadata collection based on a given jason string
	 * @param json
	 * @return fields metadata collection based on a given jason string
	 */
	protected  Collection<FieldMetadata> getFieldMetadata(String json) {
		
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoDataArr = jasoObj.getJSONArray(JSON_DATA_FIELD_NAME);

		// prepare entity collection
		Collection<FieldMetadata> fieldsMetadata = new ArrayList<FieldMetadata>();
		IntStream.range(0, jasoDataArr.length()).forEach((i)->fieldsMetadata.add(new Gson().fromJson(jasoDataArr.getJSONObject(i).toString(), FieldMetadata.class)));
		
		// TBD - Remove after debugging
		/*Collection<FieldMetadata> fieldsMetadata = new ArrayList<FieldMetadata>();
		
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			FieldMetadata fieldMetadata = new Gson().fromJson(jasoEntityObj.toString(), FieldMetadata.class);
			fieldsMetadata.add(fieldMetadata);

		}*/
		
		return fieldsMetadata;
	}
		
	
	/**
	 * get a new EntityMetadata object based on jason object
	 * @param jasoEntityObj - Jason object
	 * @return new EntityMetadata object
	 */
	protected  EntityMetadata getEntityMetadata(JSONObject jasoEntityObj)  {

		Set<Feature> features = new HashSet<Feature>();
		String name = jasoEntityObj.getString(JSON_NAME_FIELD_NAME);
		String label = jasoEntityObj.getString(JSON_LABEL_FIELD_NAME);
		//Boolean canModifyLabel = jasoEntityObj.getBoolean(JSON_CAN_MODIFY_LABEL_FIELD_NAME);
		JSONArray jasonFeatures = jasoEntityObj.getJSONArray(JSON_FEATURES_FIELD_NAME);
		IntStream.range(0, jasonFeatures.length()).forEach((i)-> {
			Feature featureObject = getFeatureObject(jasonFeatures.getJSONObject(i));
			if (featureObject != null) {
				features.add(featureObject);
			}
		});
		
		// TBD - Remove after debugging
		/*for (int i = 0; i < jasonFeatures.length(); i++) {
			JSONObject jasoFeatureObj = jasonFeatures.getJSONObject(i);
			Feature feature = getFeatureObject(jasoFeatureObj);
			features.add(feature);
		}*/
		
		EntityMetadata entityMetadata = new EntityMetadata(name, label, false, features);
		return entityMetadata;
	}
	
	/**
	 * Handle exceptions
	 * @param e - exception
	 * @throws RuntimeException
	 */
	protected void handleException(Exception e) throws RuntimeException{
		
		ErrorModel errorModel =  new ErrorModel(e.getMessage());
		throw new NgaException(errorModel);
		
	}
	
	/**
	 * This class hold the entity metadata object
	 * @author Moris oz
	 *
	 */
	public  class Entity extends NGARequest<Collection<EntityMetadata>> {
		
		
		private String type = "";
		
		/**
		 * Creates a new entity object
		 * 
		 * @param typeValue  - Type Value
		 */
		public Entity(String typeValue){
			
			type = typeValue;
		}
		
		/**
		 * Get Request execution of metadata's entity info
		 * Collection<EntityModel> object
		 */
		@Override
		public Collection<EntityMetadata> execute() throws RuntimeException {
			
			Collection<EntityMetadata> entitiesMetadata = null;
			String url = urlDomain+ "/" + type;
			String json = "";			
			GenericUrl domain = new GenericUrl(url);
			try{
				HttpRequest httpRequest = requestFactory.buildGetRequest(domain);
				logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url,httpRequest.getHeaders().toString()));
				HttpResponse response = httpRequest.execute();
				logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage(),response.getHeaders().toString()));
				
				if (response.isSuccessStatusCode()) {
	
					json = response.parseAsString();
					entitiesMetadata = getEntitiesMetadata(json);
				}

				logger.debug(String.format(LOGGER_RESPONSE_JASON_FORMAT, json));

                // Update request factory with the latest response Cookie
                requestFactory.getInitializer().initialize(httpRequest.setContent(ByteArrayContent.fromString(null, response.toString())));
            }
			catch (Exception e){
				logger.debug("Fail to execute GET request.", e);
				handleException(e);
			}
			
			return entitiesMetadata;
			
		}
	}
	
	/**
	 * This class hold the field metadata object
	 * @author Moris oz
	 *
	 */
	public  class Field extends NGARequest<Collection<FieldMetadata>> {
		
		private String type = "";
		
		/**
		 * Creates a new Field object
		 * 
		 * @param typeValue  - Type Value
		 */
		public Field(String typeValue){
			
			type = typeValue;
		}
		
		/**
		 * Get Request execution of metadata's field info
		 * Collection<EntityModel> object
		 */
		@Override
		public Collection<FieldMetadata> execute() throws RuntimeException {
			
			Collection<FieldMetadata> colEntitiesMetadata = null;
			String url = urlDomain+ "/" + type;
			String json = "";
			GenericUrl urlDomain = new GenericUrl(url);
			try{
				 
				HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
				logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url,httpRequest.getHeaders().toString()));
				HttpResponse response = httpRequest.execute();
				logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage(),response.getHeaders().toString()));
				
				if (response.isSuccessStatusCode()) {
	
					json = response.parseAsString();
					colEntitiesMetadata = getFieldMetadata(json);
				}

                logger.debug(String.format(LOGGER_RESPONSE_JASON_FORMAT, json));

                // Update request factory with the latest response Cookie
                requestFactory.getInitializer().initialize(httpRequest.setContent(ByteArrayContent.fromString(null, response.toString())));
            }
			catch (Exception e){
				
				handleException(e);
			}
			
			return colEntitiesMetadata;
		
		}
	}

}

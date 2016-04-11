package com.hpe.adm.nga.sdk.metadata;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import com.hpe.adm.nga.sdk.NGARequest;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.metadata.Features.AttachmentsFeature;
import com.hpe.adm.nga.sdk.metadata.Features.BuisnessRuleFeature;
import com.hpe.adm.nga.sdk.metadata.Features.CommentsFeature;
import com.hpe.adm.nga.sdk.metadata.Features.Feature;
import com.hpe.adm.nga.sdk.metadata.Features.HierarchyFeature;
import com.hpe.adm.nga.sdk.metadata.Features.MailingFeature;
import com.hpe.adm.nga.sdk.metadata.Features.RestFeature;
import com.hpe.adm.nga.sdk.metadata.Features.SubTypesFeature;
import com.hpe.adm.nga.sdk.metadata.Features.SubTypesOfFeature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by brucesp on 23/02/2016.
 */
public class Metadata {

	// constant
	private final String JSON_DATA_FIELD_NAME = "data";
	private final String JSON_NAME_FIELD_NAME = "name";
	private final String JSON_FEATURES_FIELD_NAME = "features";
	private final String QUERY_NAME_FIELD_NAME = "name";
	private final String QUERY_ENTITY_NAME_FIELD_NAME = "entity_name";
	private final String TYPE_NAME_ENTITIES_NAME = "entities";
	private final String TYPE_NAME_FIELDS_NAME = "fields";
	private final String TYPE_NAME_FIELDS_QUERY_FORMAT = "fields?query=\"";
	private final String TYPE_NAME_ENTITIES_QUERY_FORMAT = "entities?query=\"";
	private final String FEATURE_REST_NAME = "rest";
	private final String FEATURE_MAILING_NAME = "mailing";
	private final String FEATURE_HAS_ATTACHMENTS_NAME = "has_attachments";
	private final String FEATURE_HAS_COMMENTS_NAME = "has_comments";
	private final String FEATURE_BUSINESS_RULES_NAME = "business_rules";
	private final String FEATURE_SUBTYPES_NAME = "subtypes";
	private final String FEATURE_SUBTYPE_OF_NAME = "subtype_of";
	private final String FEATURE_HIERARCHICAL_ENTITY_NAME = "hierarchical_entity";
	private final String LOGGER_INVALID_FEATURE_FORMAT = ": not a valid feature";
	private final String LOGGER_REQUEST_FORMAT = "Request: %s - %s";
	private final String LOGGER_RESPONSE_FORMAT = "Response: %s:%s";	
    
	// private members
	private HttpRequestFactory requestFactory = null;
	private String urlDomain = "";
	private String type = "";
	private Query quaryEntities = null;
	private Logger logger = LogManager.getLogger(Metadata.class.getName());
	
	public Metadata(HttpRequestFactory reqFactory, String strMetadataDomain){
		
		urlDomain = strMetadataDomain;
		requestFactory = reqFactory;
	}
	
	public Entity entities(){
		type = TYPE_NAME_ENTITIES_NAME;
		return new Entity();
	}

	public Entity entities(String...entities){
		
		Query quaryEntities =  new Query();
		String quaryList = "";	
		for (String value : entities) {
			
			quaryEntities = new Query.Field(QUERY_NAME_FIELD_NAME).equal(value).build();
			quaryList = quaryList + quaryEntities.getQueryString() + "||";
		}
		quaryList = quaryList.substring(0,quaryList.length()-2);
		type = TYPE_NAME_ENTITIES_QUERY_FORMAT + quaryList + "\"";
				
		return new Entity();
	}

	public Field fields(){
		type = TYPE_NAME_FIELDS_NAME;
		return new Field();
	}

	public Field fields(String...entities){
		
		Query quaryFields =  new Query();
		String quaryList = "";	
		for (String value : entities) {
			
			quaryFields = new Query.Field(QUERY_ENTITY_NAME_FIELD_NAME).equal(value).build();
			quaryList = quaryList + quaryFields.getQueryString() + "||";
		}
		quaryList = quaryList.substring(0,quaryList.length()-2);
		type = TYPE_NAME_FIELDS_QUERY_FORMAT + quaryList + "\"";
		
		return new Field();
	}
	
	/**
	 * get a entities metadata collection based on a given jason string
	 * @param json
	 * @return entity metadata collection based on a given jason string
	 * @throws JSONException
	 */
	protected  Collection<EntityMetadata> getEntitiesMetadata(String json) throws JSONException {
		
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoDataArr = jasoObj.getJSONArray(JSON_DATA_FIELD_NAME);

		// prepare entity collection
		Collection<EntityMetadata> EntitiesMetadata = new ArrayList<EntityMetadata>();
		
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			EntityMetadata entityModel = getEntityMetadata(jasoEntityObj);
			EntitiesMetadata.add(entityModel);

		}
		
		return EntitiesMetadata;
	}
	
	/**
	 * get a fields metadata collection based on a given jason string
	 * @param json
	 * @return fields metadata collection based on a given jason string
	 * @throws JSONException
	 */
	protected  Collection<FieldMetadata> getFieldMetadata(String json) throws JSONException {
		
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jasoObj = new JSONObject(tokener);
		JSONArray jasoDataArr = jasoObj.getJSONArray(JSON_DATA_FIELD_NAME);

		// prepare entity collection
		Collection<FieldMetadata> fieldsMetadata = new ArrayList<FieldMetadata>();
		
		for (int i = 0; i < jasoDataArr.length(); i++) {
			JSONObject jasoEntityObj = jasoDataArr.getJSONObject(i);
			FieldMetadata fieldMetadata = new Gson().fromJson(jasoEntityObj.toString(), FieldMetadata.class);
			fieldsMetadata.add(fieldMetadata);

		}
		
		return fieldsMetadata;
	}
		
	
	/**
	 * get a new EntityMetadata object based on jason object
	 * @param jasoEntityObj - Jason object
	 * @return new EntityMetadata object
	 * @throws JSONException
	 */
	protected  EntityMetadata getEntityMetadata(JSONObject jasoEntityObj) throws JSONException {

		Set<Feature> features = new HashSet<Feature>();
		String name = jasoEntityObj.getString(JSON_NAME_FIELD_NAME);
		JSONArray jasonFeatures = jasoEntityObj.getJSONArray(JSON_FEATURES_FIELD_NAME);
		
		
		for (int i = 0; i < jasonFeatures.length(); i++) {
			JSONObject jasoFeatureObj = jasonFeatures.getJSONObject(i);
			
			String featureName = jasoFeatureObj.getString(JSON_NAME_FIELD_NAME);
			Feature feature = null;
			
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
			 		feature = new Gson().fromJson(jasoFeatureObj.toString(), BuisnessRuleFeature.class);
                break;
				case FEATURE_SUBTYPES_NAME:  
            		feature = new Gson().fromJson(jasoFeatureObj.toString(), SubTypesFeature.class);
                break;
				case FEATURE_SUBTYPE_OF_NAME: 
					feature = new Gson().fromJson(jasoFeatureObj.toString(), SubTypesOfFeature.class);
                break;
				case FEATURE_HIERARCHICAL_ENTITY_NAME: 
					feature = new Gson().fromJson(jasoFeatureObj.toString(), HierarchyFeature.class);
                break;
				default:
					logger.debug(featureName + LOGGER_INVALID_FEATURE_FORMAT);
				break;
			}
			
			features.add(feature);
		
		}
		
		EntityMetadata entityMetadata = new EntityMetadata(name,features);
		return entityMetadata;
	}
	
	public  class Entity extends NGARequest<Collection<EntityMetadata>> {
		@Override
		public Collection<EntityMetadata> execute() throws IOException, JSONException {
			
			String url = urlDomain+ "/" + type;
						
			GenericUrl domain = new GenericUrl(url);
			HttpRequest httpRequest = requestFactory.buildGetRequest(domain);
			logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url));
			HttpResponse response = httpRequest.execute();
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
			Collection<EntityMetadata> entitiesMetadata = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				entitiesMetadata = getEntitiesMetadata(json);
			}

			return entitiesMetadata;
			
		}
	}

	public  class Field extends NGARequest<Collection<FieldMetadata>> {
		@Override
		public Collection<FieldMetadata> execute() throws IOException, JSONException {
			
			String url = urlDomain+ "/" + type;
			
			GenericUrl urlDomain = new GenericUrl(url);
			HttpRequest httpRequest = requestFactory.buildGetRequest(urlDomain);
			logger.debug(String.format(LOGGER_REQUEST_FORMAT, httpRequest.getRequestMethod(), url));
			HttpResponse response = httpRequest.execute();
			logger.debug(String.format(LOGGER_RESPONSE_FORMAT, response.getStatusCode(), response.getStatusMessage()));
			Collection<FieldMetadata> colEntitiesMetadata = null;

			if (response.isSuccessStatusCode()) {

				String json = response.parseAsString();
				colEntitiesMetadata = getFieldMetadata(json);
			}

			return colEntitiesMetadata;
		
		}
	}

}

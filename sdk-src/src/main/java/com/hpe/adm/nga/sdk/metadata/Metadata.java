/*
 *
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
package com.hpe.adm.nga.sdk.metadata;

import com.google.gson.Gson;
import com.hpe.adm.nga.sdk.OctaneRequest;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.QueryMethod;
import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.metadata.features.*;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *<p>
 * This class hold the  metadata object and serves all functionality concern to fields metadata and entity metadata.
 * <br/>
 * The REST API metadata is split into two: entities and fields.  For each context the correct method used:
 * <br>
 *</p>
 * <p><code>[server_url]/metadata/entities</code> use {@link #entities()}</p>
 * <p><code>[server_url]/metadata/fields</code> use {@link #fields()}</p>
 * <p>In addition you can use the API to retrieve specific entities and fields.  For example:</p>
 * <p><code>[server_url]/metadata/entities?query="name EQ 'story'"</code> use {@link #entities(String...)} with "story" as the parameter</p>
 * <p><code>[server_url]/metadata/fields?query="entity_name EQ 'pipeline'"</code> use {@link #fields(String...)} with "pipeline" as the parameter</p>
 * <p>Metadata can only be read (HTTP GET) so after the correct method is called the <code>execute()</code> method should be used.</p>
 * @see EntityMetadata for more information about entity metadata
 * @see FieldMetadata for more information about field metadata
 * */
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
	private static final String LOGGER_RESPONSE_JSON_FORMAT = "Response_Json: %s";
	
	// private members
	private OctaneHttpClient octaneHttpClient = null;
	private String urlDomain = "";
	private final Logger logger = LogManager.getLogger(Metadata.class.getName());
	
	/**
	 * Creates a new Metadata object
	 * 
	 * @param octaneHttpClient
	 *            - Http Request Factory
	 * @param strMetadataDomain
	 *            - metadata Domain Name
	 */
	public Metadata(OctaneHttpClient octaneHttpClient, String strMetadataDomain){
		
		urlDomain = strMetadataDomain;
		this.octaneHttpClient = octaneHttpClient;
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
	            .map(s ->  Query.statement(QUERY_NAME_FIELD_NAME, QueryMethod.EqualTo, s).build().getQueryString())
	            .collect(Collectors.joining("||"));

		return new Entity(TYPE_NAME_ENTITIES_QUERY_FORMAT + quaryList + "\"");
	}
	
	/**
	 * Get metadata field object
	 * @return new field object
	 */
	public Field fields(){
		
		return new Field(TYPE_NAME_FIELDS_NAME);
	}
	
	/**
	 * Get metadata field object based on given field names
	 * @param entities list of entities that will be returned
	 * @return an object containing field metadata
	 */
	public Field fields(String...entities){
		

		List<String> entitiesList =  Arrays.asList(entities);
		String quaryList =  entitiesList
	            .stream()
	            .map(s ->  Query.statement(QUERY_ENTITY_NAME_FIELD_NAME, QueryMethod.EqualTo, s).build().getQueryString())
	            .collect(Collectors.joining("||"));
	
		
		return new Field(TYPE_NAME_FIELDS_QUERY_FORMAT + quaryList + "\"");
	}
	
	/**
	 * Get Feature Object based on the feature json object
	 * @param jsonFeatureObj - Json Feature object
	 * @return Feature Object
	 */
	private Feature getFeatureObject(JSONObject jsonFeatureObj){
		
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
	
	/**
	 * get a entities metadata collection based on a given json string
	 * @param json
	 * @return entity metadata collection based on a given json string
	 */
	private Collection<EntityMetadata> getEntitiesMetadata(String json)  {
		
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jsonObj = new JSONObject(tokener);
		JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_FIELD_NAME);

		// prepare entity collection
		Collection<EntityMetadata> entitiesMetadata = new ArrayList<>();
		IntStream.range(0, jsonDataArr.length()).forEach((i)->entitiesMetadata.add(getEntityMetadata(jsonDataArr.getJSONObject(i))));

		return entitiesMetadata;
	}
	
	/**
	 * get a fields metadata collection based on a given json string
	 * @param json
	 * @return fields metadata collection based on a given json string
	 */
	private Collection<FieldMetadata> getFieldMetadata(String json) {
		
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jsonObj = new JSONObject(tokener);
		JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_FIELD_NAME);

		// prepare entity collection
		Collection<FieldMetadata> fieldsMetadata = new ArrayList<>();
		IntStream.range(0, jsonDataArr.length()).forEach((i)->fieldsMetadata.add(new Gson().fromJson(jsonDataArr.getJSONObject(i).toString(), FieldMetadata.class)));

		return fieldsMetadata;
	}
		
	
	/**
	 * get a new EntityMetadata object based on json object
	 * @param jsonEntityObj - Json object
	 * @return new EntityMetadata object
	 */
	private EntityMetadata getEntityMetadata(JSONObject jsonEntityObj)  {

		Set<Feature> features = new HashSet<>();
		String name = jsonEntityObj.getString(JSON_NAME_FIELD_NAME);
		String label = jsonEntityObj.getString(JSON_LABEL_FIELD_NAME);
		//Boolean canModifyLabel = jsonEntityObj.getBoolean(JSON_CAN_MODIFY_LABEL_FIELD_NAME);
		JSONArray jsonFeatures = jsonEntityObj.getJSONArray(JSON_FEATURES_FIELD_NAME);
		IntStream.range(0, jsonFeatures.length()).forEach((i)-> {
			Feature featureObject = getFeatureObject(jsonFeatures.getJSONObject(i));
			if (featureObject != null) {
				features.add(featureObject);
			}
		});

		// TODO: Check this
		return new EntityMetadata(name, label, false, features);
	}
	
	/**
	 * Handle exceptions
	 * @param e - exception
	 */
	private void handleException(Exception e){
		
		ErrorModel errorModel =  new ErrorModel(e.getMessage());
		throw new OctaneException(errorModel);
		
	}
	
	/**
	 * This class hold the entity metadata object
	 *
	 */
	public  class Entity extends OctaneRequest<Collection<EntityMetadata>> {
		
		
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
			try{
				OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(url);
				OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);
				
				if (response.isSuccessStatusCode()) {
	
					json = response.getContent();
					entitiesMetadata = getEntitiesMetadata(json);
				}

				logger.debug(String.format(LOGGER_RESPONSE_JSON_FORMAT, json));
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
	 *
	 */
	public  class Field extends OctaneRequest<Collection<FieldMetadata>> {
		
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
			try{
				 
				OctaneHttpRequest octaneHttpRequest =
						new OctaneHttpRequest.GetOctaneHttpRequest(url).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
				OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);
				
				if (response.isSuccessStatusCode()) {
	
					json = response.getContent();
					colEntitiesMetadata = getFieldMetadata(json);
				}

                logger.debug(String.format(LOGGER_RESPONSE_JSON_FORMAT, json));
            }
			catch (Exception e){
				
				handleException(e);
			}
			
			return colEntitiesMetadata;
		
		}
	}

}

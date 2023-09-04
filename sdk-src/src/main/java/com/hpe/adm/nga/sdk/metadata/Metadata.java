/**
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
package com.hpe.adm.nga.sdk.metadata;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 *<p>
 * This class hold the  metadata object and serves all functionality concern to fields metadata and entity metadata.
 * <br>
 * The REST API metadata is split into two: entities and fields.  For each context the correct method used:
 * <br>
 *</p>
 * <p>{@code [server_url]/metadata/entities} use {@link #entities()}</p>
 * <p>{@code [server_url]/metadata/fields} use {@link #fields()}</p>
 * <p>In addition you can use the API to retrieve specific entities and fields.  For example:</p>
 * <p>{@code [server_url]/metadata/entities?query="name EQ 'story'"} use {@link #entities(String...)} with "story" as the parameter</p>
 * <p>{@code [server_url]/metadata/fields?query="entity_name EQ 'pipeline'"} use {@link #fields(String...)} with "pipeline" as the parameter</p>
 * <p>Metadata can only be read (HTTP GET) so after the correct method is called the {@code execute()} method should be used.</p>
 * @see EntityMetadata for more information about entity metadata
 * @see FieldMetadata for more information about field metadata
 * */
public class Metadata {

	// private members
	private final OctaneHttpClient octaneHttpClient;
	private final String urlDomain;
	
	/**
	 * Creates a new Metadata object
	 * 
	 * @param octaneHttpClient
	 *            - Http Request Factory
	 * @param strMetadataDomain
	 *            - metadata Domain Name
	 */
	public Metadata(OctaneHttpClient octaneHttpClient, String strMetadataDomain){
		urlDomain = strMetadataDomain + "metadata";
		this.octaneHttpClient = octaneHttpClient;
	}
	
	/**
	 * GetEntities metadata entity object
	 * @return new metadata entity object
	 */
	public GetEntityMetadata entities(){
		
		return new GetEntityMetadata(octaneHttpClient, urlDomain);
	}
	
	/**
	 * GetEntities metadata entity object based on given entities names
	 * @param entities A comma separated array of entities that will be created to the context
	 * @return new metadata entity object
	 */
	public GetEntityMetadata entities(String...entities){
		final GetEntityMetadata entity = new GetEntityMetadata(octaneHttpClient, urlDomain);
		entity.addEntities(entities);
		return entity;
	}
	
	/**
	 * GetEntities metadata field object
	 * @return new field object
	 */
	public GetFieldMetadata fields(){
		
		return new GetFieldMetadata(octaneHttpClient, urlDomain);
	}
	
	/**
	 * GetEntities metadata field object based on given field names
	 * @param entities list of entities that will be returned
	 * @return an object containing field metadata
	 */
	public GetFieldMetadata fields(String...entities){
		final GetFieldMetadata field = new GetFieldMetadata(octaneHttpClient, urlDomain);
		field.addEntities(entities);
		return field;
	}

}

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
package com.microfocus.adm.nga.sdk.metadata;

import com.microfocus.adm.nga.sdk.metadata.features.Feature;

import java.util.Collection;

/**
 *
 * This class hold the entity metadata object and serve all functionality concern to metadata of entities.
 * <br>
 * See the entity metadata REST API documentation for more information.  By calling {@link #features()} a collection of
 * features is returned
 *
 */
public class EntityMetadata {
	
	private String name = "";
	private String label = "";
	private static final String type = "entity_metadata";
	private boolean canModifyLabel = false;
	private Collection<Feature> features = null;
	
	/**
	 * Creates a new EntityMetadata object
	 * 
	 * @param name - Metadata name
	 * @param newFeatures - Metadata features
	 * @param canModifyLabel Whether the label can be modified
	 * @param label The label
	 */
	public EntityMetadata(String name, String label, boolean canModifyLabel, Collection<Feature> newFeatures){
		
		this.name = name;
		this.label = label;
		this.canModifyLabel = canModifyLabel;
		features = newFeatures;
	}

	/**
	 * get metadata name
	 * @return metadata name
	 */
	public String getName(){
		return name;
	}

	/**
	 * get metadata's features
	 * @return metadata's features
	 */
	public Collection<Feature> features(){
		return features;
	}

	/**
	 * get metadata's label
	 * @return metadata's label
     */
	public String getLabel() { return label; }

	/**
	 * get metadata's type
	 * @return metadata's type
     */
	public String getType() { return type; }

	/**
	 * get metadata's canModifyLabel
	 * @return metadata's canModifyLabel
     */
	public boolean canModifyLabel() { return canModifyLabel; }
}



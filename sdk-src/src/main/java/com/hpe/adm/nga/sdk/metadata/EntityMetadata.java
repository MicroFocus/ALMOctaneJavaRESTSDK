package com.hpe.adm.nga.sdk.metadata;

import com.hpe.adm.nga.sdk.metadata.features.Feature;

import java.util.Collection;

/**
 * This class hold the entity metadata object and serve all functionality concern to metadata of entities
 * @author Moris oz
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
	 *           
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



package com.hpe.adm.nga.sdk.metadata;

import java.util.Collection;

import com.hpe.adm.nga.sdk.metadata.Features.Feature;

/**
 * This class hold the entity metadata object and serve all functionality concern to metadata of entities
 * @author Moris oz
 *
 */
public class EntityMetadata {
	
	private String name = "";
	private String label = "";
	private final String type = "entity_metadata";
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
	};
	
	/**
	 * get metadata name
	 * @return
	 */
	public String getName(){
		return name;
	};

	/**
	 * get metadata's features
	 * @return
	 */
	public Collection<Feature> features(){
		return features;
	}

	/**
	 * get metadata's label
	 * @return
     */
	public String getLabel() { return label; }

	/**
	 * get metadata's type
	 * @return
     */
	public String getType() { return type; }

	/**
	 * get metadata's canModifyLabel
	 * @return
     */
	public boolean canModifyLabel() { return canModifyLabel; }
}



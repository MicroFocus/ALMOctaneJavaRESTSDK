package com.hpe.adm.nga.sdk.metadata;

import com.hpe.adm.nga.sdk.metadata.Features.Feature;
import java.util.Collection;


/**
 * This class hold the entity metadata object and serve all functionality concern to metadata of entities
 * @author Moris oz
 *
 */
public class EntityMetadata {
	
	private String name = "";
	private Collection<Feature> features = null;
	
	/**
	 * Creates a new EntityMetadata object
	 * 
	 * @param name - Metadata name
	 * @param newFeatures - Metadata features
	 *           
	 */
	public  EntityMetadata(String name,Collection<Feature> newFeatures){
		
		setName(name);
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
	 * set metadata name
	 * @param newName
	 */
	public void setName(String newName){
		name = newName;
	};
	
	/**
	 * get metadata's features
	 * @return
	 */
	public Collection<Feature> features(){
		return features;
	}
	
	/**
	 * set metadata's features
	 * @param newFeatures
	 */
	public void setfeatures(Collection<Feature> newFeatures){
		features = newFeatures;
	}

	
}



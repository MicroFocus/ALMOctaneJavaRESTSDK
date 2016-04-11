package com.hpe.adm.nga.sdk.metadata;

import java.util.Collection;
import com.hpe.adm.nga.sdk.metadata.Features.Feature;

/**
 * Created by brucesp on 23/02/2016.
 */
public class EntityMetadata {
	
	private String name = "";
	private Collection<Feature> features = null;
	
	public  EntityMetadata(String name,Collection<Feature> newFeatures){
		
		setName(name);
		features = newFeatures;
	};
	
	public String getName(){
		return name;
	};
	
	public void setName(String newName){
		name = newName;
	};
	
	public Collection<Feature> features(){
		return features;
	}
	
	public void setfeatures(Collection<Feature> newFeatures){
		features = newFeatures;
	}

	
}



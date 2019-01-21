package com.hpe.adm.nga.sdk.metadata.features;

/**
 *
 * This class hold the base class of all Features
 *
 */
public class Feature {
	
	private String name ="";
	
	/**
	 * get feature name
	 * @return The name of the feature
	 */
	public String getName(){
		return name;
		}
	
	/**
	 * set feature name
	 * @param newName The name of the feature
	 */
	public void setName(String newName){
		name = newName; 
		}
	
}



package com.hpe.adm.nga.sdk.metadata.features;

/**
 * This class hold the base class of all Features
 * @autho Moris oz
 *
 */
public class Feature {
	
	private String name ="";
	
	/**
	 * get feature name
	 * @return
	 */
	public String getName(){
		return name;
		}
	
	/**
	 * set feature name
	 * @param newName
	 */
	public void setName(String newName){
		name = newName; 
		}
	
}



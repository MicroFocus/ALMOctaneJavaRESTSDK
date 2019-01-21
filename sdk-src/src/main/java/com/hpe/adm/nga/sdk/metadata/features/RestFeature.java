package com.hpe.adm.nga.sdk.metadata.features;


/**
 *
 * This class hold the RestFeature object and serve all functionality concern to rest Feature
 *
 */
public class RestFeature extends Feature{
	
	private String url; 
	private String[] methods;
	
	/**
	 * get url
	 * @return The url of the rest feature
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * get supported methods
	 * @return the supported methods (GET, POST, PUT, DELETE)
	 */
	public String[] getMethods(){
		return methods;
	}
}



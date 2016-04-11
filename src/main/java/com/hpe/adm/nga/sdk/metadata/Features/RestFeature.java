package com.hpe.adm.nga.sdk.metadata.Features;


/**
 * Created by brucesp on 23/02/2016.
 */
public class RestFeature extends Feature{
	
	private String url; 
	private String[] methods;
	
	public String getUrl(){
		return url;
	}
	
	public String[] getMethods(){
		return methods;
	}
}



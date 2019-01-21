package com.hpe.adm.nga.sdk.metadata.features;

/**
 *
 * This class hold the SubTypesFeature object and serve all functionality concern to SubTypes Feature
 *
 */
public class SubTypesFeature extends Feature{
	
	private String[] types;
	private boolean editable;

	/**
	 * get types
	 * @return an array of subtypes
	 */
	public String[] getTypes(){return types;}

    /**
	 * get editable
	 * @return whether this is editable
     */
	public boolean getEditable() { return editable; }
}




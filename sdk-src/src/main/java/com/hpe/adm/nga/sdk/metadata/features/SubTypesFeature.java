package com.hpe.adm.nga.sdk.metadata.features;

/**
 * This class hold the SubTypesFeature object and serve all functionality concern to SubTypes Feature
 * @autho Moris oz
 *
 */
public class SubTypesFeature extends Feature{
	
	private String[] types;
	private boolean editable;

	/**
	 * get types
	 * @return
	 */
	public String[] getTypes(){return types;};

	/**
	 * get editable
	 * @return
     */
	public boolean getEditable() { return editable; }
}




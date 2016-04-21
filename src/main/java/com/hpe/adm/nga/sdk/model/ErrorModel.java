package com.hpe.adm.nga.sdk.model;

import java.util.Collection;
import java.util.Set;

import com.hpe.adm.nga.sdk.model.StringFieldModel;

/**
 * This class hold the ErrorModel objects and server as an error data holder
 * entities.
 * 
 * @author moris oz
 *
 */
public class ErrorModel extends  EntityModel{
	
	/**
	 * Creates a new ErrorModel object with given field models
	 * 
	 * @param value
	 *            - a collection of field models
	 */
	public ErrorModel(Set<FieldModel> value) {
		super(value);
		
	}

	
}

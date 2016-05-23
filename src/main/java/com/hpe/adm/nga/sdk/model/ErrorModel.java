package main.java.com.hpe.adm.nga.sdk.model;

import java.util.Set;

/**
 * This class hold the ErrorModel objects and server as an error data holder
 * entities.
 * 
 * @author moris oz
 *
 */
public class ErrorModel extends  EntityModel{
	
	private static final String ERROR_DESCRIPTION_KEY 	= "Description";
	
	/**
	 * Creates a new ErrorModel object with given field models
	 * 
	 * @param value
	 *            - a collection of field models
	 */
	public ErrorModel(Set<FieldModel> value) {
		super(value);
		
	}
	
	/**
	 * Creates a new ErrorModel object with given error message
	 * @param value -  error message
	 */
	public ErrorModel(String value) {
		
		super(ERROR_DESCRIPTION_KEY,value);
			
	}
}

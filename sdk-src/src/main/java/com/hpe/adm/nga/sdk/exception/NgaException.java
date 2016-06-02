package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.model.ErrorModel;

/**
 * This extends the RuntimeException objects and serve all functionality concern to
 * NGA Exceptions.
 * @author Moris oz
 *
 */
public class NgaException extends RuntimeException {

		
	private ErrorModel errorModel = null;
	
	/**
	 * Creates a new NgaException object based on error model
	 * 
	 * @param error - error model
	 *   
	 */
	public NgaException(ErrorModel  error){
		setError(error);
	}
	
	/**
	 * get error model
	 * @return error model
	 */
	public ErrorModel getError(){
		return errorModel;
	}
	
	/**
	 * set a new error model
	 * @param error - error model
	 */
	public void setError(ErrorModel  error){
		errorModel = error;
	}
	
}


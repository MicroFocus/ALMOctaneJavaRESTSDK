package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.model.ErrorModel;

/**
 * This extends the RuntimeException objects and serve all functionality concern to
 * Octane Exceptions.
 * @author Moris oz
 *
 */
public class OctaneException extends RuntimeException {

		
	private ErrorModel errorModel = null;
	
	/**
	 * Creates a new OctaneException object based on error model
	 * 
	 * @param error - error model
	 *   
	 */
	public OctaneException(ErrorModel  error){
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
	private void setError(ErrorModel error){
		errorModel = error;
	}

	@Override
	public String getMessage() {
		return errorModel.getDescription();
	}
}


/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.model.ErrorModel;

/**
 *
 * This extends the RuntimeException objects and serve all functionality concern to
 * Octane Exceptions.
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
		return errorModel.toString();
	}
}


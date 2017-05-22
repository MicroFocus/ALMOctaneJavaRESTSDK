/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ErrorModel;

import java.util.Collection;

/**
 *
 * This extends the RuntimeException objects and serve all functionality concern to
 * Octane Partial Exceptions ( an exception that contain error and entities data )
 *
 */
public class OctanePartialException extends RuntimeException{

	private Collection<EntityModel> entities = null;
	private Collection<ErrorModel> errors = null;
	
	/**
	 * Creates a new OctanePartialException object based on errors and entities models
	 * 
	 * @param errorModels - error models
	 * @param entities - entities models  
	 */
	public OctanePartialException(Collection<ErrorModel> errorModels, Collection<EntityModel> entities){
		
		setEntitiesModels(entities);
		setErrorModels(errorModels);
		
	}
	
	/**
	 * getter of collection of entities models
	 * @return collection of entities models
	 */
	public Collection<EntityModel> getEntitiesModels(){
		return entities;
	}
	
	/**
	 * setter of collection of entities models
	 *
	 */
	private void setEntitiesModels(Collection<EntityModel> entCollection){
		entities = entCollection;
	}
	
	/**
	 * getter of collection of error models
	 * @return the error models
	 */
	public Collection<ErrorModel> getErrorModels(){
		return errors;
	}
	
	/**
	 * setter of collection of error models
	 *
	 */
	private void setErrorModels(Collection<ErrorModel> errorModels){
		errors = errorModels;
	}
		
		
}

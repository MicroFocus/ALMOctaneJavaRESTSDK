package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ErrorModel;

import java.util.Collection;

/**
 * This extends the RuntimeException objects and serve all functionality concern to
 * NGA Partial Exceptions ( an exception that contain error and entities data )
 * @author Moris oz
 *
 */
public class NgaPartialException extends RuntimeException{

	private Collection<EntityModel> entities = null;
	private Collection<ErrorModel> errors = null;
	
	/**
	 * Creates a new NgaPartialException object based on errors and entities models 
	 * 
	 * @param errorModels - error models
	 * @param entities - entities models  
	 */
	public NgaPartialException(Collection<ErrorModel> errorModels,Collection<EntityModel> entities){
		
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
	public void setEntitiesModels(Collection<EntityModel> entCollection){
		entities = entCollection;
	}
	
	/**
	 * getter of collection of error models
	 * @return
	 */
	public Collection<ErrorModel> getErrorModels(){
		return errors;
	}
	
	/**
	 * setter of collection of error models
	 *
	 */
	public void setErrorModels(Collection<ErrorModel> errorModels){
		errors = errorModels;
	}
		
		
}

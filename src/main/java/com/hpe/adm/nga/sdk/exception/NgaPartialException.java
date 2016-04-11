package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.NGAError;
import com.hpe.adm.nga.sdk.model.EntityModel;

import java.util.Collection;

/**
 * Created by moris on 23/03/2016.
 */
public class NgaPartialException extends NgaException{

	public Collection<EntityModel> getEntitiesModels(){return null;};
	public Collection<NGAError> getErrors(){return null;};
	
}

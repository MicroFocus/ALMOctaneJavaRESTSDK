package java.com.hpe.adm.nga.sdk.metadata;

import java.com.hpe.adm.nga.sdk.NGAError;
import java.com.hpe.adm.nga.sdk.NGARequest;
import java.com.hpe.adm.nga.sdk.authorisation.Authorisation;
import java.com.hpe.adm.nga.sdk.model.EntityModel;

import java.util.Collection;

/**
 * Created by moris on 23/03/2016.
 */
public class NgaPartialException extends NgaException{

	public Collection<EntityModel> getEntitiesModels() {
		
	}
	
	public Collection<NGAError> getErrors();
	
}

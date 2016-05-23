package com.hpe.adm.nga.sdk.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class hold the EntityModel objects and server as an entity data holder
 * entities.
 * 
 * @author moris oz
 *
 */
public class EntityModel {

	
	private Set<FieldModel> data = null;
	
	/**
	 * Creates a new EntityModel object with given field models
	 * 
	 * @param value
	 *            - a collection of field models
	 */
	public EntityModel(Set<FieldModel> value){
				
		setValues(value);
	}
	
	/**
	 * Creates a new EntityModel object with solo string filed
	 * 
	 * @param value
	 *            - a collection of field models
	 */
	public EntityModel(String key,String value){
				
		Set<FieldModel> fieldModels = new HashSet<FieldModel>();
		FieldModel fldModel = new StringFieldModel(key,value);
		fieldModels.add(fldModel);
		setValues(fieldModels);
	}
		
	/**
	 * getter of entity value
	 * @return  a collection of field models
	 */
	public Set<FieldModel> getValues(){
		return data;
	};
			
	/**
	 * 	setter of new entity value	
	 * @param value - a collection of field models
	 */
	public void setValues(Set<FieldModel> value){
		
		data = value;
	};
}

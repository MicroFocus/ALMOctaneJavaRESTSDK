package com.hpe.adm.nga.sdk.model;

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
				
		setValue(value);
	}
		
	/**
	 * getter of entity value
	 * @return  a collection of field models
	 */
	public Set<FieldModel> getValue(){
		return data;
	};
			
	/**
	 * 	setter of new entity value	
	 * @param value - a collection of field models
	 */
	public void setValue(Set<FieldModel> value){
		
		data = value;
	};
}

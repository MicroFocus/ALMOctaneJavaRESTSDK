package com.hpe.adm.nga.sdk.model;


/**
 * This class hold the ReferenceFieldModel objects and serve as a ReferenceField type FieldModel data holder 
 * 
 * @author moris oz
 *
 */
public class ReferenceFieldModel implements FieldModel<EntityModel> {
	
	//Private 
	private EntityModel refValue;
	private String refName;
	
	/**
	 * Creates a new ReferenceFieldModel object
	 * 
	 * @param name - Field name
	 * @param value - Field Value
	 */
	public ReferenceFieldModel(String name,EntityModel value){
		
		setValue( name, value);
	}
	
	/**
	 * Get Value
	 */
	public EntityModel getValue(){
		return refValue ;
	}
	
	/**
	 * Get name
	 */
	public String getName(){
		return refName;
	}
	
	/**
	 * Set name/value
	 */
	public void setValue(String name,EntityModel value){
		
		refValue = value;
		refName = name;
	}
	
	
}

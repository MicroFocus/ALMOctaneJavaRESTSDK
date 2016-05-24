package com.hpe.adm.nga.sdk.model;

/**
 * This class hold the BooleanFieldModel objects and serve as a Boolean type FieldModel data holder 
 * 
 * @author moris oz
 *
 */
public class BooleanFieldModel implements FieldModel<Boolean> {
	
	//Private 
		private String name = "";
		private Boolean value = false;
		
		/**
		 * Creates a new BooleanFieldModel object
		 * 
		 * @param newName - Field name
		 * @param newValue - Field Value
		 */
		public BooleanFieldModel(String newName,Boolean newValue){
			
			setValue(newName,newValue);
		}
		
		/**
		 * get value
		 */
		public Boolean getValue()	{
			return value;
		};
		
		/**
		 * get Field's name
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * set Field's name/value
		 */
		public void setValue(String newName,Boolean newValue){
			
			name = newName;
			value = newValue;
		};
	
	
}

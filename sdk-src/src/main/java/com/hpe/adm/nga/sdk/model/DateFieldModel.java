package com.hpe.adm.nga.sdk.model;

import java.util.Date;

/**
 * This class hold the DateFieldModel objects and serve as a Date type FieldModel data holder 
 * 
 * @author moris oz
 *
 */
public class DateFieldModel implements FieldModel<Date> {
	
	//Private 
		private String name = "";
		private Date value = null;
		
		/**
		 * Creates a new DateFieldModel object
		 * 
		 * @param newName - Field name
		 * @param newValue - Field Value
		 */
		public DateFieldModel(String newName,Date newValue){
			
			setValue(newName,newValue);
		}
		
		/**
		 * get value
		 */
		public Date getValue()	{
			return value;
		}

	/**
		 * get Field's name
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * set Field's name/value
		 */
		public void setValue(String newName,Date newValue){
			
			name = newName;
			value = newValue;
		}


}

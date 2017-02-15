/*
 *
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
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
 */
package com.hpe.adm.nga.sdk.model;

import java.util.Date;

/**
 *
 * This class hold the DateFieldModel objects and serve as a Date type FieldModel data holder 
 *
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

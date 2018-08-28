/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microfocus.adm.nga.sdk.model;

/**
 *
 * This class hold the BooleanFieldModel objects and serve as a Boolean type FieldModel data holder
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
		public void setValue(String newName,Boolean newValue){
			
			name = newName;
			value = newValue;
		}


}

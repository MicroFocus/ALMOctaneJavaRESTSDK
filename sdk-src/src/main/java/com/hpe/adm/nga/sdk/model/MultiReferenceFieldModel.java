/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.model;

import java.util.Collection;


/**
 *
 * This class hold the MultiReferenceFieldModel objects and serve as a multi reference type FieldModel data holder 
 *
 *
 */
public class MultiReferenceFieldModel implements FieldModel<Collection<EntityModel>> {
	
	
	
		//Private 
		private String name = "";
		private Collection<EntityModel> data = null;
		
		/**
		 * Creates a new MultiReferenceFieldModel object
		 * 
		 * @param newName - Field name
		 * @param value - Field Value
		 */
		public MultiReferenceFieldModel(String newName,Collection<EntityModel> value){
			
			setValue(newName,value);
		}
		
		/**
		 * get value
		 */
		public Collection<EntityModel> getValue(){
			return data;
		}

    /**
		 * get name
		 */
		public String getName(){
			
			return name;
		}
		
		/**
		 * set name
		 * @param newName - new field name
		 */
		public void setName(String newName){
			
			name = newName;
		}
		
		/**
		 * set name/value;
		 */
		public void setValue(String newName,Collection<EntityModel> value){
			
			name = newName;
			data = value;
		}


}

/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.model;


/**
 *
 * This class hold the ReferenceErrorModel objects and serve as a ReferenceError type FieldModel data holder 
 *
 *
 */
public class ReferenceErrorModel implements FieldModel<ReferenceErrorModel.ReferenceError> {
	
	//Private 
	private ReferenceError refValue;
	private String refName;
	
	/**
	 * Creates a new ReferenceErrorModel object
	 * 
	 * @param name - Field name
	 * @param value - Field Value
	 */
	public ReferenceErrorModel(String name,ReferenceError value){
		
		setValue( name, value);
	}
	
	/**
	 * get value
	 */
	public ReferenceError getValue(){
		return refValue ;
	}
	
	/**
	 * get name
	 */
	public String getName(){
		return refName;
	}
	
	/**
	 * set value
	 */
	public void setValue(String name,ReferenceError value){
		
		refValue = value;
		refName = name;
	}
	
	/**
	 * data structure for referenceError
	 *
	 */
	public static class ReferenceError  {
		
		private long entity_id;  // Variable name must reflect Rest variable name ( Gson().fromJson )
		private String entity_type; // Variable name must reflect Rest variable name ( Gson().fromJson )
		
		/**
		 * Creates a new ReferenceError object
		 * 
		 * @param Id - ReferenceError id
		 * @param type - ReferenceError type
		 */
		public ReferenceError(long Id,String type){
			
			setValues(Id,type);
		}
				
		/**
		 * set value
		 * @param id the entity id
		 * @param type the entity type
		 */
		public void setValues(long id,String type){
			
			entity_id = id;
			entity_type = type;
		}
		
		/**
		 * get id
		 * @return entity id
		 */
		public long getId(){
			return entity_id;
		}
		
		/**
		 * get type
		 * @return entity type
		 */
		public String gettype(){
			return entity_type;
		}
		
	}
	
}

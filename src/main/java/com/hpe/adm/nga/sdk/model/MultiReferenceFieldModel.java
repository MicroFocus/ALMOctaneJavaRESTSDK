package com.hpe.adm.nga.sdk.model;

import java.util.Collection;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel.ReferenceModel;

/**
 * Created by brucesp on 22/02/2016.
 */
public class MultiReferenceFieldModel implements FieldModel<Collection<ReferenceModel>> {
	
	
	
		//Private 
		private String name;
		

		Collection<ReferenceModel> data;
		
		public MultiReferenceFieldModel(String newName,Collection<ReferenceModel> value){
			
			setValue(newName,value);
		}
		
		public Collection<ReferenceModel> getValue(){
			return data;
		};
		
		public String getName(){
			
			return name;
		}
		
		public void setName(String newName){
			
			name = newName;
		}
		
		public void setValue(String newName,Collection<ReferenceModel> value){
			
			name = newName;
			data = value;
		};
		
				
		
}

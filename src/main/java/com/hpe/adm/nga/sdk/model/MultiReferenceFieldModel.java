package com.hpe.adm.nga.sdk.model;

import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel.ReferenceModel;

/**
 * Created by brucesp on 22/02/2016.
 */
public class MultiReferenceFieldModel implements FieldModel<Collection<ReferenceModel>> {
	
	
	
		//Private 
		private String strName;
		
		//private List<ReferenceModel> data; // Variable name must refelct Rest variable name ( Gson().fromJson )
		Collection<ReferenceModel> data;
		
		public MultiReferenceFieldModel(String name,Collection<ReferenceModel> value){
			
			setValue(name,value);
		}
		
		public Collection<ReferenceModel> getValue(){
			return data;
		};
		
		public String getName(){
			
			return strName;
		}
		
		public void setName(String name){
			
			strName = name;
		}
		
		public void setValue(String name,Collection<ReferenceModel> value){
			
			strName = name;
			data = value;
		};
		
				
		
}

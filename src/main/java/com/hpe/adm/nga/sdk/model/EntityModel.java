package com.hpe.adm.nga.sdk.model;

import java.util.Collection;
import java.util.Set;

import com.hpe.adm.nga.sdk.model.ReferenceFieldModel.ReferenceModel;

/**
 * Created by brucesp on 22/02/2016.
 */
public class EntityModel {

	
	Set<FieldModel> data;
			
	public EntityModel(Set<FieldModel> value){
				
		setValue(value);
	}
			
	public Set<FieldModel> getValue(){
		return data;
	};
			
			
	public void setValue(Set<FieldModel> value){
		
		data = value;
	};
}

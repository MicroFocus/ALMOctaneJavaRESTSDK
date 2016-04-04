package com.hpe.adm.nga.sdk.model;

import java.util.Collection;


/**
 * Created by brucesp on 22/02/2016.
 */
public class ReferenceFieldModel implements FieldModel<ReferenceFieldModel.ReferenceModel> {
	
	//Private 
	ReferenceModel refValue;
	String refName;
	
	public ReferenceFieldModel(String name,ReferenceModel value){
		
		setValue( name, value);
	}
	
	public ReferenceModel getValue(){
		return refValue ;
	}
	
	public String getName(){
		return refName;
	}

	public void setValue(String name,ReferenceModel value){
		
		refValue = value;
		refName = name;
	}
	
	public static class ReferenceModel  {
		
		private long id;  // Variable name must refelct Rest variable name ( Gson().fromJson )
		private String type; // Variable name must refelct Rest variable name ( Gson().fromJson )
		
		public ReferenceModel(long Id,String type){
			
			setValues(Id,type);
		}
				
		
		public void setValues(long strId,String strType){
			
			id = strId;
			type = strType;
		}
		
		public long getId(){
			return id;
		}
		
		public String gettype(){
			return type;
		}
		
	}
	
}

package com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 22/02/2016.
 */
public class LongFieldModel implements FieldModel<Long> {
	
	//Private 
		private String name;
		private Long value;
		
		public LongFieldModel(String newName,Long newValue){
			
			setValue(newName,newValue);
		}
		
		public Long getValue()	{
			return value;
		};
		
		public String getName(){
			return name;
		}
		
		public void setValue(String newName,Long newValue){
			
			name = newName;
			value = newValue;
		};
	
	
}

package com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 22/02/2016.
 */
public class LongFieldModel implements FieldModel<Long> {
	
	//Private 
		private String strName;
		private Long lValue;
		
		public LongFieldModel(String name,Long value){
			
			setValue(name,value);
		}
		
		public Long getValue()	{
			return lValue;
		};
		
		public String getName(){
			return strName;
		}
		
		public void setValue(String name,Long value){
			
			strName = name;
			lValue = value;
		};
	
	
}

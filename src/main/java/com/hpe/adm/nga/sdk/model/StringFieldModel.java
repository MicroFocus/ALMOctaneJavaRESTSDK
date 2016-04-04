package com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 22/02/2016.
 */
public class StringFieldModel implements FieldModel<String> {
	
	//Private 
	private String strName;
	private String strValue;
	
	public StringFieldModel(String name,String value){
		
		setValue(name,value);
	}
	
	public String getValue()	{
		return strValue;
	};
	
	public String getName(){
		return strName;
	}
	
	public void setValue(String name,String value){
		
		strName = name;
		strValue = value;
	};
}

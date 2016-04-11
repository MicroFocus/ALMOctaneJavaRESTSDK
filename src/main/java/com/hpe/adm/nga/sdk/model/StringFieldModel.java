package com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 22/02/2016.
 */
public class StringFieldModel implements FieldModel<String> {
	
	//Private 
	private String name;
	private String value;
	
	public StringFieldModel(String newName,String newValue){
		
		setValue(newName,newValue);
	}
	
	public String getValue()	{
		return value;
	};
	
	public String getName(){
		return name;
	}
	
	public void setValue(String newName,String newValue){
		
		name = newName;
		value = newValue;
	};
}

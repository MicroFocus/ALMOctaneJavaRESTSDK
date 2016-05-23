package com.hpe.adm.nga.sdk.model;

/**
 * Interface of FieldModel
 * 
 * @author moris oz
 *
 */
public interface FieldModel<T> {
	
	public T getValue();
	public void setValue(String name,T value);
	public String getName();
	
}

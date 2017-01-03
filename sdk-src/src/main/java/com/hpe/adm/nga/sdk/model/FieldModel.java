package com.hpe.adm.nga.sdk.model;

/**
 * Interface of FieldModel
 * 
 * @author moris oz
 *
 */
public interface FieldModel<T> {
	
	T getValue();
	void setValue(String name, T value);
	String getName();
	
}

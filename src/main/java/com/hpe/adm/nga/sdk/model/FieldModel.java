package com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 22/02/2016.
 */
public interface FieldModel<T> {
	
	public T getValue();
	public void setValue(String name,T value);
	public String getName();
	
}

package com.hpe.adm.nga.sdk;

import java.io.IOException;

import org.json.JSONException;

/**
 * NGA request Interface 
 * @author Moris oz
 *
 * @param <T>
 */
public abstract class NGARequest<T> {

	public abstract T execute() throws Exception;

}

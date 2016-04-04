package com.hpe.adm.nga.sdk;

import java.io.IOException;

import org.json.JSONException;

/**
 * Created by brucesp on 22/02/2016.
 */
public abstract class NGARequest<T> {

	public abstract T execute() throws IOException, JSONException;

}

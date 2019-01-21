package com.hpe.adm.nga.sdk.model;

import org.json.JSONObject;

import java.util.Set;

/**
 *
 * This class hold the ErrorModel objects and server as an error data holder
 * entities.
 */
public class ErrorModel extends EntityModel{

	public static final String HTTP_STATUS_CODE_PROPERTY_NAME = "http_status_code";

	/**
	 * Creates a new ErrorModel object with given field models
	 * 
	 * @param value
	 *            - a collection of field models
	 */
	public ErrorModel(Set<FieldModel> value) {
		super(value);
	}

	@Override
	public String toString() {
		JSONObject jsonObject = ModelParser.getInstance().getEntityJSONObject(this);
		return jsonObject.toString();
	}

}
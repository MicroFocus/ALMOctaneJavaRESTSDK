/*
 * © Copyright 2016-2021 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	public static final String STACK_TRACE_PROPERTY_NAME = "stack_trace";

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
		jsonObject.remove(STACK_TRACE_PROPERTY_NAME);
		return jsonObject.toString();
	}

}

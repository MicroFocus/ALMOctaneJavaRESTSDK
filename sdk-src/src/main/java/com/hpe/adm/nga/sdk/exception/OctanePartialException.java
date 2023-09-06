/*
 * Copyright 2016-2023 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ErrorModel;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * This extends the RuntimeException objects and serve all functionality concern to
 * Octane Partial Exceptions ( an exception that contain error and entities data )
 *
 */
public class OctanePartialException extends RuntimeException{

	private final Collection<EntityModel> entities;
	private final Collection<ErrorModel> errors;

	/**
	 * Creates a new OctanePartialException object based on errors and entities models
	 *
	 * @param errorModels - error models
	 * @param entities - entities models
	 */
	public OctanePartialException(Collection<ErrorModel> errorModels, Collection<EntityModel> entities){
		this.errors = errorModels;
		this.entities = entities;
	}

	/**
	 * getter of collection of entities models
	 * @return collection of entities models
	 */
	public Collection<EntityModel> getEntitiesModels(){
		return entities;
	}

	/**
	 * getter of collection of error models
	 * @return the error models
	 */
	public Collection<ErrorModel> getErrorModels(){
		return errors;
	}

	@Override
	public String getMessage() {
		return errors.stream()
				.map(errorModel -> {
					if (errorModel == null) {
						return "Unknown error.";
					}
					return errorModel.toString();
				}).collect(Collectors.joining(".\n", "Errors:\n", "."));
	}

}

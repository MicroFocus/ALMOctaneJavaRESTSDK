/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.metadata.features;

import com.hpe.adm.nga.sdk.metadata.FieldMetadata.AccessLevel;

/**
 *
 * This class hold the RestFeature object and serve all functionality concern to rest Feature
 *
 */
public class RestFeature extends Feature{
	
	private String url; 
	private String[] methods;
	private AccessLevel access_level;
	
	/**
	 * get url
	 * 
	 * @return The url of the rest feature
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * get supported methods
	 * 
	 * @return the supported methods (GET, POST, PUT, DELETE)
	 */
	public String[] getMethods(){
		return methods;
	}

	/**
	 * get access level of the methods
	 * 
	 * @return the Access Level of the Feature
	 */
	public AccessLevel getAccessLevel() {
		return access_level;
    }
}



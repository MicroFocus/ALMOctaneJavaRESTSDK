package com.hpe.adm.nga.sdk.unit_tests.common;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;

import java.util.Collection;
/*
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
public class Demo {

	public static void main(String[] args) {
		final String MY_APP_ID = "moris@korentec.co.il";
	    final String MY_APP_SECRET = "Moris4095";
	    
		Octane octane = (new Octane.Builder(
    		      new SimpleUserAuthentication(MY_APP_ID, MY_APP_SECRET)
    		      )).Server("https://mqast001pngx.saas.hpe.com").sharedSpace(21025).workSpace(1002).build();
		
		Metadata metadata = octane.metadata();
		// all entities
//		Collection<EntityMetadata> colEntityMetadata  = metadata.entities().execute();
//		Collection<EntityMetadata> colEntityMetadata  = metadata.entities("taxonomy_item_node","test_suite","theme").execute();
		Collection<FieldMetadata> colFieldMetadat = metadata.fields("pipeline", "metaphase").execute();
	}

}

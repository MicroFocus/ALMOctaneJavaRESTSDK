package com.hpe.adm.nga.sdk.unit_tests.common;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authorisation.UserAuthorisation;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;

import java.util.Collection;

public class Demo {

	public static void main(String[] args) {
		final String MY_APP_ID = "moris@korentec.co.il";
	    final String MY_APP_SECRET = "Moris4095";
	    
		Octane octane = (new Octane.Builder(
    		      new UserAuthorisation(MY_APP_ID, MY_APP_SECRET)
    		      )).Server("https://mqast001pngx.saas.hpe.com").sharedSpace(21025).workSpace(1002).build();
		
		Metadata metadata = octane.metadata();
		// all entities
//		Collection<EntityMetadata> colEntityMetadata  = metadata.entities().execute();
//		Collection<EntityMetadata> colEntityMetadata  = metadata.entities("taxonomy_item_node","test_suite","theme").execute();
		Collection<FieldMetadata> colFieldMetadat = metadata.fields("pipeline", "metaphase").execute();
	}

}

package com.hpe.adm.nga.sdk.unit_tests.common;

import java.util.Collection;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.authorisation.UserAuthorisation;
import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;

public class Demo {

	public static void main(String[] args) {
		final String MY_APP_ID = "moris@korentec.co.il";
	    final String MY_APP_SECRET = "Moris4095";
	    
		NGA nga = (new NGA.Builder(
    		      new UserAuthorisation(MY_APP_ID, MY_APP_SECRET)
    		      )).Server("https://mqast001pngx.saas.hpe.com").sharedSpace(21025).workSpace(1002).build();
		
		Metadata metadata = nga.metadata();
		// all entities
//		Collection<EntityMetadata> colEntityMetadata  = metadata.entities().execute();
//		Collection<EntityMetadata> colEntityMetadata  = metadata.entities("taxonomy_item_node","test_suite","theme").execute();
		Collection<FieldMetadata> colFieldMetadat = metadata.fields("pipeline", "metaphase").execute();
	}

}

package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;

/**
 * Created by brucesp on 22/02/2016.
 */
public class TestNGA {

	public void testNGA() {
		NGA nga = new NGA.Builder(
				new BasicAuthorisation() {
					@Override
					public String getUsername() {
						return null;
					}

					@Override
					public String getPassword() {
						return null;
					}
				}
		).sharedSpace(1212121).workSpace(22121).build();

		final Entity defects = nga.entity("defects");
		// get examples
		// get entity 1001 with only field "test"
		defects.entities(1001).get().addFields("test").execute();
		// get 10 entities from page 1 with fields "test" and "date"
		defects.entityList().get().addFields("test", "date").limit(10).offset(1).execute();

		// create
		defects.entityList().create().entity(new com.hpe.adm.nga.sdk.model.Entity()).execute();

		// update
		defects.entityList().update().entity(new com.hpe.adm.nga.sdk.model.Entity()).query(new Query().parameter("test", "ewewe")).execute();

		// delete
		defects.entities(1002).delete().execute();

		// metadata
		final Metadata metadata = nga.metadata();
		// all entities
		metadata.entities().execute();
		// only defects
		metadata.entities("defects").execute();

		// fields
		metadata.fields().execute();
		// only fields of defects and tests
		metadata.fields("defects", "tests").execute();

	}

}

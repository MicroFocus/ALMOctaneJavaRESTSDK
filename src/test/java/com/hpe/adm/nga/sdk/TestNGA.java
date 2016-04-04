package com.hpe.adm.nga.sdk;

import java.com.hpe.adm.nga.sdk.EntityList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.com.hpe.adm.nga.sdk.NGA;
import java.com.hpe.adm.nga.sdk.Query;
import java.com.hpe.adm.nga.sdk.attachments.Attachments;
import java.com.hpe.adm.nga.sdk.metadata.Metadata;
import java.com.hpe.adm.nga.sdk.model.EntityModel;
import java.com.hpe.adm.nga.sdk.model.IFieldModel;
import java.com.hpe.adm.nga.sdk.model.StringFieldModel;
import java.com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;

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
		).Server("https://sandbox.nextgenalm.com").sharedSpace(1212121).workSpace(22121).build();

		final EntityList defects = nga.entityList("defects");


		// get examples
		// get entity 1001 with only field "test"
		defects.at(1001).get().addFields("test").execute();
		// get 10 entities from page 1 with fields "test" and "date"
		defects.get().addFields("test", "date").limit(10).offset(1).execute();

		// create
		Collection<EntityModel> entityModels = new HashSet<EntityModel>();
		defects.create().entities(entityModels).execute();

		// update
		//defects.entityList().update().entity(new EntityModel()).query(new Query().parameter("test", "ewewe")).execute();
		EntityModel entityModel = new EntityModel();
		Set<IFieldModel> fieldsModels = new HashSet<IFieldModel>();
		IFieldModel fieldModel = new StringFieldModel();
		fieldModel.setValue("id", "id1234");
		fieldsModels.add(fieldModel);
		entityModel.setFields(fieldsModels);
		defects.update().entity(entityModel).query(new Query().field("date").less(new Date()).build()).execute();

		// delete
		defects.at(1002).delete().execute();

		// Attachmnts
		final Attachments attachmnts = nga.attachments();	
		// get all attachemnts
		attachmnts.get().execute();
		// get binary data of attachmnet with id=1001 
		attachmnts.at(1001).getBinary().execute();
		// get entity data of attachmnet with id=1001 
		attachmnts.at(1001).get().execute();
		
		//update 
		attachmnts.update().entity(new EntityModel()).execute();
		
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

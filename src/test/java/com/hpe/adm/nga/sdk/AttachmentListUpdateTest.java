package com.hpe.adm.nga.tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;

public class AttachmentListUpdateTest  {

	@Test
	public void testUpdateExecute() {
		
		final String MY_APP_ID = "moris@korentec.co.il";
	    final String MY_APP_SECRET = "Moris4095";
	    NGA nga;
		try {
			nga =	new NGA.Builder(
					new BasicAuthorisation(){
						@Override
						public String getUsername(){
							
							return MY_APP_ID;
						}
						
						@Override
						public String getPassword(){
							
							return MY_APP_SECRET;
						}
						
					}
					).Server("https://mqast001pngx.saas.hpe.com").sharedSpace(4063).workSpace(1002).build();
			
			assertNotNull(nga);		
			// Attachmnts
			AttachmentList attachmnts = nga.AttachmentList();
			
			Collection<EntityModel> entityModelsIn = new HashSet<EntityModel>();
			Set<FieldModel> data = new HashSet<FieldModel>();
			FieldModel FieldModel1 = new StringFieldModel("type", "attachment");
			FieldModel FieldModel2 = new StringFieldModel("description", "description test1");
			FieldModel FieldModel3 = new LongFieldModel("id",new Long(5001));
			data.add(FieldModel1);
			data.add(FieldModel2);
			data.add(FieldModel3);
			EntityModel entityModel = new EntityModel(data);
			entityModelsIn.add(entityModel);
			FieldModel1 = new StringFieldModel("type", "attachment");
			FieldModel2 = new StringFieldModel("description", "description test2");
			FieldModel3 = new LongFieldModel("id",new Long(5002));
			data = new HashSet<FieldModel>();
			data.add(FieldModel1);
			data.add(FieldModel2);
			data.add(FieldModel3);
			entityModel = new EntityModel(data);
			entityModelsIn.add(entityModel);
			
			Collection<EntityModel> collEntityModel = attachmnts.update().entities(entityModelsIn).execute();
			assertNotNull(collEntityModel);
			assertSame(entityModelsIn.size(),collEntityModel.size());
		}
		catch (Exception e) {
			fail("Exception:"+e);
		}
	}

}

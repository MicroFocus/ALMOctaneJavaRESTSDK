package com.hpe.adm.nga.tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;

public class EntityListServiceEntitiesGetTest {

	@Test
	public void testGetExecute() {
		
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
			
			final EntityList defects = nga.entityList("defects"); // product_areas
	
			
			EntityModel entityModel  = defects.at(8024).get().addFields("description").execute();
			assertNotNull(entityModel);
			Set<FieldModel> fields = entityModel.getValue();
			assertSame(fields.size(),3); // Description+id+type 
			
			
		}
		catch (Exception e) {
			fail("Exception:"+e);
		}
	}

}

package com.hpe.adm.nga.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import com.google.api.client.http.HttpRequestFactory;
import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.EntityListService;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.model.EntityModel;


public class EntityListServiceGetTest {


	@SuppressWarnings("null")
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
			int maxEntitieslimit = 10;
			
			Collection<EntityModel> ColEntityList = defects.get().addFields("version_stamp", "item_type").limit(maxEntitieslimit).offset(1).addOrderBy("version_stamp",false).execute();
			assertNotNull(ColEntityList);
			assertSame(ColEntityList.size(),maxEntitieslimit);
			
		}
		catch (Exception e) {
			fail("Exception:"+e);
		}
	}


}

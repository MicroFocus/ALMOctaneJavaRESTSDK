/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
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
package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.unit_tests.common.CommonUtils;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.fail;
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TestUpdateEntities {
	private static Octane octane;
	private static EntityList defects;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		octane = CommonMethods.getOctaneForTest();
		defects = octane.entityList("defects");
	}
	
	@Test
	public void testUpdateEntity(){		
		final String jsonUpdateString = "{\"parent\":{\"id\":1002,\"type\":\"feature\"},\"phase\":{\"id\":1007,\"type\":\"phase\"},\"severity\":{\"id\":1004,\"type\":\"list_node\"},\"id\":1,\"name\":\"name\"}";
		// test single entity
		UpdateEntity spiedUpdateEntity = PowerMockito.spy(defects.at(1002).update());
		
		try{	
			// convert string to json object
			JSONObject inJsonEntity = new JSONObject(jsonUpdateString);
			// run protected method which converts to EntityModel
			EntityModel entityModelIn = ModelParser.getInstance().getEntityModel(inJsonEntity);
			
			// Insert data to update entity
			spiedUpdateEntity.entity(entityModelIn);
			// get internal state of EntityModel
			EntityModel entityModelOut = (EntityModel) Whitebox.getInternalState(spiedUpdateEntity, "entityModel");
			// convert internal data to JSONObject via internal method and convert it to EntityModel

			Assert.assertTrue(CommonUtils.isEntityAInEntityB(entityModelIn, entityModelOut));
		}
		catch(Exception ex){
			fail("Failed with exception: " + ex);
		}
		
	}
}

package com.hpe.adm.nga.sdk.unit_tests.entityListService;

import com.hpe.adm.nga.sdk.Octane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.EntityListService;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

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
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TestServiceMethods{

	private EntityListService service = null;
	private EntityListService spiedService = null;
	private EntityListService.Get spiedGetEntity = null;
	private static Octane octane;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		octane = CommonMethods.getOctaneForTest();

	}
	
	@Before
	public void setUpBeforeMethod(){
		EntityList defects = octane.entityList("defects");
		spiedGetEntity = PowerMockito.spy(defects.get());
		EntityList spiedDefects = PowerMockito.spy(defects);
		service = (EntityListService)Whitebox.getInternalState(spiedDefects, "entityListService");					
		spiedService = PowerMockito.spy(service);
		
	}

	/**
	 * Test correct build of URL with field specification, limits, offset and orderBy.
	 * The method invokes internal protected method with retrieved private parameters.
	 */
	@Test
	public void testUrlBuilder(){
		final String expectedResult = CommonMethods.getDomain() + "/api/shared_spaces/" + CommonMethods.getSharedSpace() + "/workspaces/" + CommonMethods.getWorkSpace() + "/defects?fields=version_stamp,item_type&limit=10&offset=1&order_by=-version_stamp";
		try{					
			spiedGetEntity.addFields("version_stamp", "item_type").limit(10).offset(1).addOrderBy("version_stamp",false);
			// get Internal fields values and call internal protected method			
			String urlDomain = (String)Whitebox.getInternalState(spiedService, "urlDomain");
			String fieldsParams = (String) Whitebox.getInternalState(spiedGetEntity, "fieldsParams");
			String orderByParam = (String) Whitebox.getInternalState(spiedGetEntity, "orderByParam");
			long limitParam = (Long) Whitebox.getInternalState(spiedGetEntity, "limitParam");
			long offsetParam = (Long) Whitebox.getInternalState(spiedGetEntity, "offsetParam");
			Query queryParams = (Query) Whitebox.getInternalState(spiedGetEntity, "queryParams");						
			
			Method urlBuilder = service.getClass().getDeclaredMethod("urlBuilder", new Class[] {String.class, String.class, String.class, long.class, long.class, Query.class});
			urlBuilder.setAccessible(true);
			String url = (String)urlBuilder.invoke(service, urlDomain, fieldsParams, orderByParam, limitParam, offsetParam, queryParams);
			assertEquals(expectedResult, url);
		}
		catch(Exception ex){
			fail("Failed with exception: " + ex);
		}
	}

	/**
	 * Test inequality between the expected URL and the created one. 
	 * The method invokes internal protected method with retrieved private parameters.
	 */
	@Test
	public void testUrlBuilderFailure(){
		final String expectedFalseResult = CommonMethods.getDomain() + "/api/shared_spaces/" + CommonMethods.getSharedSpace() + "/workspaces/" + CommonMethods.getWorkSpace() + "/defects?fields=version_stamp,item_type&limit=10&offset=1&order_by=-version_stamp";
		try{					
			spiedGetEntity.addFields("version_stamp", "item_type", "id").limit(10).offset(1).addOrderBy("version_stamp",true); // Field was added, and orderBy was changed to TRUE value
			// get Internal fields values and call internal protected method			
			String urlDomain = (String)Whitebox.getInternalState(spiedService, "urlDomain");
			String fieldsParams = (String) Whitebox.getInternalState(spiedGetEntity, "fieldsParams");
			String orderByParam = (String) Whitebox.getInternalState(spiedGetEntity, "orderByParam");
			long limitParam = (Long) Whitebox.getInternalState(spiedGetEntity, "limitParam");
			long offsetParam = (Long) Whitebox.getInternalState(spiedGetEntity, "offsetParam");
			Query queryParams = (Query) Whitebox.getInternalState(spiedGetEntity, "queryParams");						
			
			Method urlBuilder = service.getClass().getDeclaredMethod("urlBuilder", new Class[] {String.class, String.class, String.class, long.class, long.class, Query.class});
			urlBuilder.setAccessible(true);
			String url = (String)urlBuilder.invoke(service, urlDomain, fieldsParams, orderByParam, limitParam, offsetParam, queryParams);
			assertFalse(expectedFalseResult.equals(url));
		}
		catch(Exception ex){
			fail("Failed with exception: " + ex);
		}
	}
	
	@Test 
	public void testGetSimpleFieldValue(){
		FieldModel model = new StringFieldModel("fieldName", "value");
		try{
			Method getFieldValue = service.getClass().getDeclaredMethod("getFieldValue", new Class[] {FieldModel.class});	
			getFieldValue.setAccessible(true);
			Object obj = (Object)getFieldValue.invoke(service, model);			
			assertEquals(model.getValue(), obj);
			
		} catch(Exception ex){
			fail("Failed with exception: " + ex);
		}
		
	}
	
	@Test 
	public void testGetReferenceFieldValue(){
		FieldModel refModel = new StringFieldModel("fieldName", "value");
		Set<FieldModel> set = new HashSet<FieldModel>();
		set.add(refModel);
		EntityModel entModel = new EntityModel(set);
		FieldModel model = new ReferenceFieldModel("fieldName", entModel);
		try{
			Method getFieldValue = service.getClass().getDeclaredMethod("getFieldValue", new Class[] {FieldModel.class});	
			getFieldValue.setAccessible(true);
			Object obj = (Object)getFieldValue.invoke(service, model);
			assertTrue(obj instanceof JSONObject);
			if (obj instanceof JSONObject)
				assertEquals(refModel.getValue(), ((JSONObject)obj).get("fieldName"));
			
		} catch(Exception ex){
			fail("Failed with exception: " + ex);
		}		
	}
	
	@Test
	public void testGetMultiReferenceFieldValue(){
		FieldModel fieldModel = new StringFieldModel("fieldName", "value");
		Set<FieldModel> set = new HashSet<FieldModel>();
		set.add(fieldModel);
		EntityModel firstModel = new EntityModel(set);
		EntityModel secondModel = new EntityModel(set);
		Collection<EntityModel> entCol = new ArrayList<EntityModel>();
		entCol.add(firstModel);
		entCol.add(secondModel);
		FieldModel model = new MultiReferenceFieldModel("fieldName", entCol);
		try{
			Method getFieldValue = service.getClass().getDeclaredMethod("getFieldValue", new Class[] {FieldModel.class});	
			getFieldValue.setAccessible(true);
			Object obj = (Object)getFieldValue.invoke(service, model);
			assertTrue(obj instanceof JSONObject);
			if (obj instanceof JSONObject){
				JSONObject jObj = (JSONObject)obj;
				// check that total_count is correct
				assertEquals(2, Integer.parseInt(jObj.get("total_count").toString()));
				// check data								
				assertTrue(jObj.get("data") instanceof JSONArray);
				if (jObj.get("data") instanceof JSONArray){
					JSONArray jArray = (JSONArray)jObj.get("data");
					Iterator<Object> iter = jArray.iterator();
					for (int i=0; i < jArray.length(); i++){
						JSONObject currObj = jArray.getJSONObject(i);
						assertEquals(fieldModel.getValue(), currObj.get("fieldName"));
					}
				}				
			}							
		} catch(Exception ex){
			fail("Failed with exception: " + ex);
		}		
	}
}

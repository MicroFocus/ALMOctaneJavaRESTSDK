package com.hpe.adm.nga.sdk.unit_tests.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Collection;

import com.hpe.adm.nga.sdk.Octane;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TestMetadata {
	private static Octane octane;
	private static Metadata metaData;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		octane = new Octane(CommonMethods.getRequestfactory(), CommonMethods.getDomain(), CommonMethods.getSharedSpace() , CommonMethods.getWorkSpace());
		metaData = octane.metadata();
	}
	
	/**
	 * Test which type is created internally before performing GET from server
	 * This type should be equal to "entities" which means get all entities from server
	 */
	@Test
	public void testAllEntitiesType(){
		final String expectedResult = "entities";
		Metadata.Entity spiedEnt = PowerMockito.spy(metaData.entities());
		String entType = (String)Whitebox.getInternalState(spiedEnt, "type");
		assertEquals(expectedResult, entType);
		
	}
	
	/**
	 * Test which type is created internally before performing GET from server
	 * This type should contain a queried type which means get those specific entities
	 */
	@Test
	public void testSpecificEntitiesType(){
		final String expectedResult = "entities?query=\"(name EQ 'ent1')||(name EQ 'ent2')\"";
		Metadata.Entity spiedEnt = PowerMockito.spy(metaData.entities("ent1", "ent2"));
		String entType = (String)Whitebox.getInternalState(spiedEnt, "type");
		assertEquals(expectedResult, entType);
	}
	
	/**
	 * Test which type is created internally before performing GET from server
	 * This type should be equal to "entities" which means get all entities from server
	 */
	@Test
	public void testAllFieldsType(){
		final String expectedResult = "fields";
		Metadata.Field spiedEnt = PowerMockito.spy(metaData.fields());
		String entType = (String)Whitebox.getInternalState(spiedEnt, "type");
		assertEquals(expectedResult, entType);
		
	}
	
	/**
	 * Test which type is created internally before performing GET from server
	 * This type should contain a queried type which means get those specific entities
	 */
	@Test
	public void testSpecificFieldsType(){
		final String expectedResult = "fields?query=\"(entity_name EQ 'field1')||(entity_name EQ 'field2')\"";
		Metadata.Field spiedEnt = PowerMockito.spy(metaData.fields("field1", "field2"));
		String entType = (String)Whitebox.getInternalState(spiedEnt, "type");
		assertEquals(expectedResult, entType);
	}
	
	/**
	 * Test internal method which receives a json string and returns a collection of FieldMetadata
	 */
	@Test
	public void testFieldMetadataMethod(){		
		final String jsonFieldMetaString;		
		try{
			
			InputStream is = getClass().getResourceAsStream("/getFieldsMetadataString.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder builder = new StringBuilder();
	        while ((line = reader.readLine()) != null) {
	            builder.append(line);
	        }
	        jsonFieldMetaString = builder.toString();
			Method getFieldMetadata = metaData.getClass().getDeclaredMethod("getFieldMetadata", new Class[] {String.class});
			getFieldMetadata.setAccessible(true);
			Collection<FieldMetadata> fieldMD = (Collection<FieldMetadata>)getFieldMetadata.invoke(metaData, jsonFieldMetaString);	
			// should hold 28 entities
			Assert.assertTrue(fieldMD.size() == 28);
			
			// check that each field is of type "pipeline" or "metaphase"
			for (FieldMetadata field : fieldMD){
				Assert.assertTrue(field.getEntityName().equals("pipeline") || field.getEntityName().equals("metaphase"));
			}
		}
		catch(Exception ex){
			fail("Failed with exception: " + ex);
		}
	}
	
	/**
	 * Test internal method which receives a json string and returns a collection of EntityMetadata
	 */
	@Test
	public void testEntityMetadataMethod(){		
		final String jsonEntityMetaString;		
		try{
			
			InputStream is = getClass().getResourceAsStream("/getEntitiesMetadataString.txt");			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder builder = new StringBuilder();
	        while ((line = reader.readLine()) != null) {
	            builder.append(line);
	        }
	        jsonEntityMetaString = builder.toString();
			Method getEntitiesMetadata = metaData.getClass().getDeclaredMethod("getEntitiesMetadata", new Class[] {String.class});
			getEntitiesMetadata.setAccessible(true);
			Collection<EntityMetadata> entityMD = (Collection<EntityMetadata>)getEntitiesMetadata.invoke(metaData, jsonEntityMetaString);	
			// should hold 3 entities
			Assert.assertTrue(entityMD.size() == 3);
			
			// check that each Entity name is "taxonomy_item_node","test_suite","theme"
			for (EntityMetadata ent : entityMD){
				Assert.assertTrue(ent.getName().equals("taxonomy_item_node") || ent.getName().equals("test_suite") || ent.getName().equals("theme"));
			}
		}
		catch(Exception ex){
			fail("Failed with exception: " + ex);
		}	
	}
}

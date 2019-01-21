package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.entities.create.CreateEntities;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import static org.junit.Assert.fail;
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TestCreateEntities {

	private final static String JSON_DATA_NAME = "data";

	private static Octane octane;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		octane = CommonMethods.getOctaneForTest();

	}
	
	@Test
	public void testCreateEntity(){		
		final String jsonCreateString = "{\"data\":[{\"parent\":{\"id\":1002,\"type\":\"feature\"},\"phase\":{\"id\":1007,\"type\":\"phase\"},\"severity\":{\"id\":1004,\"type\":\"list_node\"},\"id\":1,\"name\":\"moris2\"}],\"total_count\":1}";

		EntityList defects = octane.entityList("defects");
		CreateEntities spiedCreateEntity = PowerMockito.spy(defects.create());
		
		try{
			Collection<EntityModel> entityModelsIn = testGetEntityModels(jsonCreateString);

			spiedCreateEntity.entities(entityModelsIn);
			
			Collection<EntityModel> internalModels = (Collection<EntityModel>) Whitebox.getInternalState(spiedCreateEntity, "entityModels");
			JSONObject jsonEntity = ModelParser.getInstance().getEntitiesJSONObject(internalModels);

			Collection<EntityModel> entityModelsOut = testGetEntityModels(jsonEntity.toString());
			Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(entityModelsIn, entityModelsOut));
		}
		catch(Exception ex){
			fail("Failed with exception: " + ex);
		}
		
	}

	private Collection<EntityModel> testGetEntityModels(String jason) throws Exception{
		JSONObject jasonObj = new JSONObject(jason);
		JSONArray jasoDataArr = jasonObj.getJSONArray(JSON_DATA_NAME);
		Collection<EntityModel> entityModels = new ArrayList<>();
		IntStream.range(0, jasoDataArr.length()).forEach((i) -> {entityModels.add(ModelParser.getInstance().getEntityModel(jasoDataArr.getJSONObject(i)));

		});

		return entityModels;
	}


}

package com.hpe.adm.nga.tests.crud;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import com.hpe.adm.nga.tests.base.TestBase;

public class CreateEntitytheme extends TestBase{
	
	
	 public CreateEntitytheme() {
	        entityName = "themes";
	    }

	
	
	
	 @Test
	    public void testCreateEntitytheme() throws Exception{

	        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga,entityName);
	        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
	        EntityModel entityModel = entityModels.iterator().next();
	        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

	        EntityModel getEntity = entityList.at(entityId).get().execute();

	        
	        Assert.assertTrue(CommonUtils.isEntityAInEntityB(entityModel, getEntity));
	    }
	    
	    @Test
	    public void testCreateEntitythemeCollection() throws Exception {

	        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
	        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
	        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);
	        Query query = QueryUtils.getQueryForIds(entityIds);
	        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
	        assertSame(getEntity.size(),1);
	    }
	    
	    @Test
	    public void testCreateEntityNametheme()throws Exception
	    {
	    	  
			  Set<FieldModel> fields = new HashSet<>();
			  String entName = "sdk_release5" + UUID.randomUUID();
			  FieldModel storypoints = new  LongFieldModel("story_points", new Long(12345678));
	          FieldModel rank = new LongFieldModel("rank",new Long(123));
	          FieldModel description = new StringFieldModel("description", "korentec_Desc");
	          FieldModel name = new StringFieldModel("name", "hanna");
	          fields.add(storypoints);
	          fields.add(rank);
	          fields.add(name);
	          fields.add(description);
	          EntityModel em = new EntityModel(fields);
	          Collection<EntityModel> entities = new ArrayList<>();
	          entities.add(em);
	          Collection<EntityModel> entityModels = entityList.create().entities(entities).execute();
	          Query query = new Query.Field("name").equal(new String(entName)).build();
	          Collection<EntityModel> getEntity = entityList.get().query(query).execute();
	          assertSame(getEntity.size(),1);
	        
	         
	    }

}

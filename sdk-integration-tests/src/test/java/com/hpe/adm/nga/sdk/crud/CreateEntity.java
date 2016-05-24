package com.hpe.adm.nga.sdk.crud;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.base.TestBase;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertSame;

/**
 * Created by Guy Guetta on 12/04/2016.
 */
public class CreateEntity extends TestBase {

    public CreateEntity() {
        entityName = "releases";
    }

    @Test
    public void testCreateEntity() throws Exception{

        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        EntityModel getEntity = entityList.at(entityId).get().execute();

        Assert.assertTrue(CommonUtils.isEntityAInEntityB(entityModel, getEntity));
    }

    @Test
    public void testCreateEntityCollection() throws Exception {

        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);
        Query query = QueryUtils.getQueryForIds(entityIds);
        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(entityModels, getEntity));
    }
    
    @Test
    public void testCreateEntityName()throws Exception
    {
    	  
		  Set<FieldModel> fields = new HashSet<>();
		  String entName = "sdk_release5" + UUID.randomUUID();
          FieldModel name = new StringFieldModel("name", entName);
          FieldModel startDate = new StringFieldModel("start_date","2012-05-17T12:00:00Z");
          FieldModel endDate = new StringFieldModel("end_date", "2016-05-02T14:00:00Z");
          fields.add(name);
          fields.add(startDate);
          fields.add(endDate);
          EntityModel em = new EntityModel(fields);
          Collection<EntityModel> entities = new ArrayList<>();
          entities.add(em);
          Collection<EntityModel> entityModels = entityList.create().entities(entities).execute();
          Query query = new Query.Field("name").equal(new String(entName)).build();
          Collection<EntityModel> getEntity = entityList.get().query(query).execute();
          assertSame(getEntity.size(),1);
    }
    
   
}

package com.hpe.adm.nga.sdk.tests.crud;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by Guy Guetta on 25/04/2016.
 */
public class DeleteEntity extends TestBase {
    public DeleteEntity() {
        entityName = "releases";
    }

    @Test
    public void testDeleteEntityById() throws Exception{
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();

        EntityModel entityModel = entityModels.iterator().next();
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        entityList.at(entityId).delete().execute();

        Collection<EntityModel> getEntity = entityList.get().execute();

        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(getEntity);

        Assert.assertFalse(entityIds.contains(entityId));
    }

    @Test
    public void testDeleteEntitiesByQuery() throws Exception{
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);

        Query query = QueryUtils.getQueryForIds(entityIds);

        entityList.delete().query(query).execute();

        Collection<EntityModel> getEntity = entityList.get().execute();

        List<Integer> actualEntityIds = CommonUtils.getIdFromEntityModelCollection(getEntity);

        //check there are no common ids
        actualEntityIds.retainAll(entityIds);

        Assert.assertTrue(actualEntityIds.isEmpty());
    }
}

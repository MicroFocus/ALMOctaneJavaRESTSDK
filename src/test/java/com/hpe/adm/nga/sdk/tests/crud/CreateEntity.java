package com.hpe.adm.nga.sdk.tests.crud;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Guy Guetta on 12/04/2016.
 */
public class CreateEntity extends TestBase {

    public CreateEntity() {
        entityName = "product_areas";
    }

    @Test
    public void testCreateEntity() throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName, fields);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        EntityModel getEntity = entityList.at(entityId).get().execute();

        Assert.assertTrue(CommonUtils.isEntityAInEntityB(generatedEntity.iterator().next(), getEntity));
    }

    @Test
    public void testCreateEntityCollection() throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName, fields);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);
        Query query = QueryUtils.getQueryForIds(entityIds);

        Collection<EntityModel> getEntity = entityList.get().query(query).execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(generatedEntity, getEntity));
    }


}

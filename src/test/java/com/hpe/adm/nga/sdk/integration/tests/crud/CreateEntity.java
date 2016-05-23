package test.java.com.hpe.adm.nga.sdk.integration.tests.crud;

import main.java.com.hpe.adm.nga.sdk.Query;
import main.java.com.hpe.adm.nga.sdk.model.FieldModel;

import main.java.com.hpe.adm.nga.sdk.model.EntityModel;
import test.java.com.hpe.adm.nga.sdk.integration.tests.base.TestBase;
import test.java.com.hpe.adm.nga.sdk.integration.utils.CommonUtils;
import test.java.com.hpe.adm.nga.sdk.integration.utils.QueryUtils;
import test.java.com.hpe.adm.nga.sdk.integration.utils.generator.DataGenerator;

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
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        EntityModel getEntity = entityList.at(entityId).get().execute();

        Assert.assertTrue(CommonUtils.isEntityAInEntityB(generatedEntity.iterator().next(), getEntity));
    }

    @Test
    public void testCreateEntityCollection() throws Exception {
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);
        Query query = QueryUtils.getQueryForIds(entityIds);

        Collection<EntityModel> getEntity = entityList.get().query(query).execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(generatedEntity, getEntity));
    }


}

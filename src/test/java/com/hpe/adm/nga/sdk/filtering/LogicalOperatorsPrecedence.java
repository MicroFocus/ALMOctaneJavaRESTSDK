package com.hpe.adm.nga.tests.filtering;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * Created by Guy Guetta on 25/04/2016.
 */
public class LogicalOperatorsPrecedence extends TestBase {

//    public LogicalOperatorsPrecedence() {
//        entityName = "features";
//    }
//
//    @Test
//    public void supportEqual() throws Exception {
//        Query query = new Query();
//        testFiltering(query);
//    }
//
//
//    private void testFiltering(Query query) throws Exception {
//        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName);
//        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
//        EntityModel entityModel = entityModels.iterator().next();
//        String entityName = CommonUtils.getNameFromEntityModel(entityModel);
//
//        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
//
//        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(generatedEntity, getEntity));
//    }
//
//    private Query getQuery(String entityName, String logicalOperation) {
//        switch (logicalOperation) {
//            case "EQ":
//                return new Query().field("name").equal(entityName).build();
//            case "LT":
//                return new Query().field("name").less("z_" + entityName).build();
//            case "GT":
//                return new Query().field("name").greater("a_" + entityName).build();
//            case "LE":
//                return new Query().field("name").lessEqual(entityName).build();
//            case "GE":
//                return new Query().field("name").greaterEqual(entityName).build();
//            default:
//                return new Query();
//        }
//    }
}

package com.hpe.adm.nga.sdk.tests.filtering;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.Query.QueryBuilder;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.*;

/**
 * Created by Guy Guetta on 25/04/2016.
 */
public class TestLogicalOperatorsPrecedence extends TestBase {

    private static List<Integer> featureIds = new ArrayList<>();

    public TestLogicalOperatorsPrecedence() {
        entityName = "features";
    }

//    @Test // cannot build complicated query
    public void supportEqual() throws Exception {
        //query="name='Alfred' AND !story_points=1"
        QueryBuilder logic1 = new Query.QueryBuilder("name", Query::equalTo, "Alfred");
        QueryBuilder logic2 = new Query.QueryBuilder("story_points", Query::equalTo, 1);
//        Query query = logic1.and(logic2);
//        testFiltering(query);
    }


    private void testFiltering(Query query) throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName, fields);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        String entityName = CommonUtils.getValueFromEntityModel(entityModel, "name");

        Collection<EntityModel> getEntity = entityList.get().query(query).execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(generatedEntity, getEntity));
    }

    @BeforeClass
    public static void initTests() throws Exception {

        nga.entityList("features").delete().execute();

        createFeature("Alfred", 0, 0); //1 ( A) & (!B) & (!C)
        createFeature("Alfred", 1, 0); //2 ( A) & ( B) & (!C)
        createFeature("NoName", 1, 0); //3 (!A) & ( B) & (!C)
        createFeature("NoName", 1, 1); //4 (!A) & ( B) & ( C)
        createFeature("NoName", 0, 1); //5 (!A) & (!B) & ( C)
        createFeature("Alfred", 0, 1); //6 ( A) & (!B) & ( C)
        createFeature("Alfred", 1, 1); //7 ( A) & ( B) & ( C)
        createFeature("NoName", 0, 0); //8 (!A) & (!B) & (!C)

    }

    private static void createFeature(String nameValue, long storyPointsValue, long initialEstimateValue) throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        fields.add(new StringFieldModel("name",nameValue));
        fields.add(new LongFieldModel("story_points", storyPointsValue));
        fields.add(new LongFieldModel("initial_estimate", initialEstimateValue));
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName, fields);
        featureIds.addAll(CommonUtils.getIdFromEntityModelCollection(generatedEntity));
    }
}

/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
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
package com.hpe.adm.nga.sdk.tests.filtering;

import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 *
 * Created by dherasymchuk on 12/29/2016.
 */
public class TestLogicalOperators extends TestBase {

    private static List<Integer> defectIds = new ArrayList<>();
    private static List<String> defectNames = new ArrayList<>();


    public TestLogicalOperators() {
        entityName = "defects";
    }


    @Test
    public void testQueryWithOr () throws Exception {
        Query query = Query.statement("id", QueryMethod.EqualTo, defectIds.get(0)).or("id", QueryMethod.EqualTo, defectIds.get(1)).build();
        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
        Assert.assertEquals("Wrong amount of defects in response",2,getEntity.size());
        Assert.assertTrue("Wrong defect id in response", defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(getEntity)));

    }

    @Test
    public void testQueryWithAnd () throws Exception {
        Query query = Query.statement("id", QueryMethod.EqualTo, defectIds.get(0)).and("name", QueryMethod.EqualTo, defectNames.get(0)).build();
        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
        Assert.assertEquals("Wrong amount of defects in response", 1,getEntity.size());
        Assert.assertEquals("Wrong defect id in response", defectIds.get(0),CommonUtils.getIdFromEntityModelCollection(getEntity).get(0));
    }

    @Test
    public void testQueryWithAndPlusOr () throws Exception {
        Query query1 = Query.statement("id", QueryMethod.EqualTo, defectIds.get(0)).and("name", QueryMethod.EqualTo, defectNames.get(0)).or("id", QueryMethod.EqualTo, defectIds.get(1)).and("name", QueryMethod.EqualTo, defectNames.get(1)).build();
        Collection<EntityModel> getEntity = entityList.get().query(query1).execute();
        Assert.assertEquals("Wrong amount of defects in response", 2,getEntity.size());
        Assert.assertTrue("Wrong defect id in response", defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(getEntity)));
    }

    @BeforeClass
    public static void setUp()throws Exception{
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity1 = DataGenerator.generateEntityModel(octane, "defects", fields);
        Collection<EntityModel> createdEntities = DataGenerator.getAllDataForEntities(octane.entityList("defects").create().entities(generatedEntity1).execute(),octane,"defects");
        Collection<EntityModel> generatedEntity2 = DataGenerator.generateEntityModel(octane, "defects", fields);
        Collection<EntityModel> createdEntities2 = DataGenerator.getAllDataForEntities(octane.entityList("defects").create().entities(generatedEntity2).execute(),octane,"defects");
        createdEntities.addAll(createdEntities2);
        defectIds.addAll(CommonUtils.getIdFromEntityModelCollection(createdEntities));
        defectNames.addAll(CommonUtils.getValuesFromEntityModelCollection(createdEntities,"name"));
    }

}

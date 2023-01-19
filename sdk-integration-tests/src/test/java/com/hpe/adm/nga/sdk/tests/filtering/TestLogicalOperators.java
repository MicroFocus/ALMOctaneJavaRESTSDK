/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.tests.filtering;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dherasymchuk on 12/29/2016.
 */
public class TestLogicalOperators extends TestBase {

    private static List<String> defectIds = new ArrayList<>();
    private static List<String> defectNames = new ArrayList<>();
    private static final long time = System.currentTimeMillis();


    public TestLogicalOperators() {
        entityName = "defects";
    }


    @Test
    public void testQueryWithOr() {
        Query query = Query.statement("id", QueryMethod.EqualTo, defectIds.get(0)).or("id", QueryMethod.EqualTo, defectIds.get(1)).build();
        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
        Assert.assertEquals("Wrong amount of defects in response", 2, getEntity.size());
        Assert.assertTrue("Wrong defect id in response", defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(getEntity)));

    }

    @Test
    public void testQueryWithAnd() {
        Query query = Query.statement("id", QueryMethod.EqualTo, defectIds.get(0)).and("name", QueryMethod.EqualTo, defectNames.get(0)).build();
        Collection<EntityModel> getEntity = entityList.get().addFields("name").query(query).execute();
        Assert.assertEquals("Wrong amount of defects in response", 1, getEntity.size());
        Assert.assertEquals("Wrong defect id in response", defectIds.get(0), CommonUtils.getIdFromEntityModelCollection(getEntity).get(0));
    }

    @Test
    public void testQueryWithAndPlusOr() {
        Query query1 = Query.statement("id", QueryMethod.EqualTo, defectIds.get(0)).and("name", QueryMethod.EqualTo, defectNames.get(0)).or("id", QueryMethod.EqualTo, defectIds.get(1)).and("name", QueryMethod.EqualTo, defectNames.get(1)).build();
        Collection<EntityModel> getEntity = entityList.get().addFields("name").query(query1).execute();
        Assert.assertEquals("Wrong amount of defects in response", 2, getEntity.size());
        Assert.assertTrue("Wrong defect id in response", defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(getEntity)));
    }

    @Test
    public void testQueryWithBtw() {
        final Query query = Query.statement("story_points", QueryMethod.Between, new QueryMethod.Between(time - 1L, time + 2L)).build();
        final OctaneCollection<EntityModel> entities = entityList.get().addFields("story_points").query(query).execute();
        Assert.assertEquals("Wrong amount of defects in response", 2, entities.size());
        Assert.assertTrue("Wrong defect id in response", defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(entities)));
    }

    @Test
    public void testQueryWithIn() {
        final Query query = Query.statement("story_points", QueryMethod.In, new Object[]{time, time + 1L}).build();
        final OctaneCollection<EntityModel> entities = entityList.get().addFields("story_points").query(query).execute();
        Assert.assertEquals("Wrong amount of defects in response", 2, entities.size());
        Assert.assertTrue("Wrong defect id in response", defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(entities)));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        fields.add(new LongFieldModel("story_points", time));
        Collection<EntityModel> generatedEntity1 = DataGenerator.generateEntityModel(octane, "defects", fields);
        Collection<EntityModel> createdEntities = DataGenerator.getAllDataForEntities(octane.entityList("defects").create().entities(generatedEntity1).execute(), octane, "defects");
        fields.clear();
        fields.add(new LongFieldModel("story_points", time + 1L));
        Collection<EntityModel> generatedEntity2 = DataGenerator.generateEntityModel(octane, "defects", fields);
        Collection<EntityModel> createdEntities2 = DataGenerator.getAllDataForEntities(octane.entityList("defects").create().entities(generatedEntity2).execute(), octane, "defects");
        createdEntities.addAll(createdEntities2);
        createdEntities = octane.entityList("defects").get().addFields("name").query(Query.statement("id", QueryMethod.In,
                createdEntities.stream().map(EntityModel::getId).toArray()).build()).execute();
        defectIds.addAll(CommonUtils.getIdFromEntityModelCollection(createdEntities));
        defectNames.addAll(CommonUtils.getValuesFromEntityModelCollection(createdEntities, "name"));
    }

}

/*
 * Copyright 2016-2026 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void queryWithOr() {
        Query query = Query.statement("id", QueryMethod.EqualTo, defectIds.getFirst()).or("id", QueryMethod.EqualTo, defectIds.get(1)).build();
        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
        assertEquals(2, getEntity.size(), "Wrong amount of defects in response");
        assertTrue(defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(getEntity)), "Wrong defect id in response");

    }

    @Test
    void queryWithAnd() {
        Query query = Query.statement("id", QueryMethod.EqualTo, defectIds.getFirst()).and("name", QueryMethod.EqualTo, defectNames.getFirst()).build();
        Collection<EntityModel> getEntity = entityList.get().addFields("name").query(query).execute();
        assertEquals(1, getEntity.size(), "Wrong amount of defects in response");
        assertEquals(defectIds.getFirst(), CommonUtils.getIdFromEntityModelCollection(getEntity).getFirst(), "Wrong defect id in response");
    }

    @Test
    void queryWithAndPlusOr() {
        Query query1 = Query.statement("id", QueryMethod.EqualTo, defectIds.getFirst()).and("name", QueryMethod.EqualTo, defectNames.getFirst()).or("id", QueryMethod.EqualTo, defectIds.get(1)).and("name", QueryMethod.EqualTo, defectNames.get(1)).build();
        Collection<EntityModel> getEntity = entityList.get().addFields("name").query(query1).execute();
        assertEquals(2, getEntity.size(), "Wrong amount of defects in response");
        assertTrue(defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(getEntity)), "Wrong defect id in response");
    }

    @Test
    void queryWithBtw() {
        final Query query = Query.statement("story_points", QueryMethod.Between, new QueryMethod.Between(time - 1L, time + 2L)).build();
        final OctaneCollection<EntityModel> entities = entityList.get().addFields("story_points").query(query).execute();
        assertEquals(2, entities.size(), "Wrong amount of defects in response");
        assertTrue(defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(entities)), "Wrong defect id in response");
    }

    @Test
    void queryWithIn() {
        final Query query = Query.statement("story_points", QueryMethod.In, new Object[]{time, time + 1L}).build();
        final OctaneCollection<EntityModel> entities = entityList.get().addFields("story_points").query(query).execute();
        assertEquals(2, entities.size(), "Wrong amount of defects in response");
        assertTrue(defectIds.containsAll(CommonUtils.getIdFromEntityModelCollection(entities)), "Wrong defect id in response");
    }

    @BeforeAll
    static void setUp() throws Exception {
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

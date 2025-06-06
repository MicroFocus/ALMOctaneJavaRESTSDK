/*
 * Copyright 2016-2025 Open Text.
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

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by Guy Guetta on 25/04/2016.
 */
public class TestSupportFiltering extends TestBase {

    public TestSupportFiltering() {
        entityName = "releases";
    }

    @Test
    public void supportEqual() throws Exception {
        testFiltering("EQ");
    }

    @Test
    public void supportLessThan() throws Exception {
        testFiltering("LT");
    }

    @Test
    public void supportGreaterThan() throws Exception {
        testFiltering("GT");
    }

    @Test
    public void supportLessEqual() throws Exception {
        testFiltering("LE");
    }

    @Test
    public void supportGreaterEqual() throws Exception {
        testFiltering("GE");
    }

    private void testFiltering(String logicalOperation) throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, entityName, fields);
        Collection<EntityModel> entityModels = DataGenerator.getAllDataForEntities(entityList.create().entities(generatedEntity).execute(),octane,entityName);
        entityModels = octane.entityList(entityName).get().addFields("name")
                .query(Query.statement("id", QueryMethod.In, entityModels.stream().map(EntityModel::getId).toArray()).build()).execute();
        EntityModel entityModel = entityModels.iterator().next();
        String entityName = CommonUtils.getValueFromEntityModel(entityModel, "name");

        Query query = getQuery(entityName, logicalOperation);

        Collection<EntityModel> getEntity = entityList.get().addFields("name").query(query).execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(entityModels, getEntity));
    }

    private Query getQuery(String entityName, String logicalOperation) {
        switch (logicalOperation) {
            case "EQ":
                return Query.statement("name", QueryMethod.EqualTo, entityName).build();
            case "LT":
                return Query.statement("name", QueryMethod.LessThan, "z_" + entityName).build();
            case "GT":
                return Query.statement("name", QueryMethod.GreaterThan, "a_" + entityName).build();
            case "LE":
                return Query.statement("name", QueryMethod.LessThanOrEqualTo, entityName).build();
            case "GE":
                return Query.statement("name", QueryMethod.GreaterThanOrEqualTo, entityName).build();
            default:
                return null;
        }
    }
}

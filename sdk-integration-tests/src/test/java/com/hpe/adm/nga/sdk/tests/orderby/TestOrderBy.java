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
package com.hpe.adm.nga.sdk.tests.orderby;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * Created by Guy Guetta on 03/05/2016.
 */
public class TestOrderBy extends TestBase {

    private static Query idQuery;

    @BeforeClass
    public static void initTest() throws Exception {
        Collection<EntityModel> generatedEntity = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            generatedEntity.addAll(DataGenerator.generateEntityModel(octane, "releases"));
        }
        Collection<EntityModel> releases = octane.entityList("releases").create().entities(generatedEntity).execute();
        List<String> ids = CommonUtils.getIdFromEntityModelCollection(releases);
        idQuery = QueryUtils.getQueryForIds(ids);
    }

    @Test
    public void orderByOneFieldAscending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().addFields("name").query(idQuery).addOrderBy("name", true).execute();

        List<String> names = CommonUtils.getValuesFromEntityModelCollection(entityModels, "name");

        Assert.assertTrue("Names are not sorted ascending", isSortedAsc(names));
    }

    @Test
    public void orderByOneFieldDescending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().addFields("name").query(idQuery).addOrderBy("name", false).execute();

        List<String> names = CommonUtils.getValuesFromEntityModelCollection(entityModels, "name");

        Assert.assertTrue("Names are not sorted descending", isSortedDes(names));
    }

    @Test
    public void orderByTwoFieldAscending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().addFields("name", "end_date").query(idQuery).addOrderBy("end_date,name", true).execute();

        List<String> names = CommonUtils.getValuesFromEntityModelCollection(entityModels, "name");

        Assert.assertTrue("Names are not sorted ascending", isSortedAsc(names));
    }

    @Test
    public void orderByTwoFieldDescending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().addFields("name", "end_date").query(idQuery).addOrderBy("end_date", true).addOrderBy("name", false).execute();

        List<String> names = CommonUtils.getValuesFromEntityModelCollection(entityModels, "name");

        Assert.assertTrue("Names are not sorted descending", isSortedDes(names));
    }

    private boolean isSortedAsc(List<String> list) {
        boolean sorted = true;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i-1).compareTo(list.get(i)) >= 0) sorted = false;
        }

        return sorted;
    }

    private boolean isSortedDes(List<String> list) {
        boolean sorted = true;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i-1).compareTo(list.get(i)) <= 0) sorted = false;
        }

        return sorted;
    }
}

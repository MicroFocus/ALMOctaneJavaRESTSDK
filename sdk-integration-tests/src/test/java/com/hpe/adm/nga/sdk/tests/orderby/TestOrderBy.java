/*
 *
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
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
 */
package com.hpe.adm.nga.sdk.tests.orderby;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
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
        List<Integer> ids = CommonUtils.getIdFromEntityModelCollection(releases);
        idQuery = QueryUtils.getQueryForIds(ids);
    }

    @Test
    public void orderByOneFieldAscending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().query(idQuery).addOrderBy("name", true).execute();

        List<String> names = CommonUtils.getValuesFromEntityModelCollection(entityModels, "name");

        Assert.assertTrue("Names are not sorted ascending", isSortedAsc(names));
    }

    @Test
    public void orderByOneFieldDescending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().query(idQuery).addOrderBy("name", false).execute();

        List<String> names = CommonUtils.getValuesFromEntityModelCollection(entityModels, "name");

        Assert.assertTrue("Names are not sorted descending", isSortedDes(names));
    }

    @Test
    public void orderByTwoFieldAscending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().query(idQuery).addOrderBy("end_date,name", true).execute();

        List<String> names = CommonUtils.getValuesFromEntityModelCollection(entityModels, "name");

        Assert.assertTrue("Names are not sorted ascending", isSortedAsc(names));
    }

    @Test
    public void orderByTwoFieldDescending() throws Exception {

        Collection<EntityModel> entityModels = octane.entityList("releases").get().query(idQuery).addOrderBy("end_date", true).addOrderBy("name", false).execute();

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

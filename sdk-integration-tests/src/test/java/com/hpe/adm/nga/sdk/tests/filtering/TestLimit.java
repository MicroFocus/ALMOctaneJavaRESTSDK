/*
 * © Copyright 2016-2021 Micro Focus or one of its affiliates.
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

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 *
 * Created by perach on 08/05/2016.
 */
public class TestLimit extends TestBase {

    public TestLimit() {
        entityName = "releases";
    }

    @Test
    public void testLimit() throws Exception {

        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(octane, entityName);
        entityList.create().entities(generatedEntity).execute();

        Collection<EntityModel> getAllEntities = entityList.get().execute();
        int totalCount = getAllEntities.size();

        if (totalCount > 1) {

            Collection<EntityModel> getLimitEntities = entityList.get().limit(totalCount - 1).execute();
            int limit = getLimitEntities.size();

            Assert.assertTrue(limit + 1 == totalCount);
        }

    }

}

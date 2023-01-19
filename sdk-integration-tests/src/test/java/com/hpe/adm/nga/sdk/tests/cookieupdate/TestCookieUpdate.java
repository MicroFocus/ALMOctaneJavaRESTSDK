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
package com.hpe.adm.nga.sdk.tests.cookieupdate;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

/**
 *
 * Created by Dmitry Zavyalov on 08/05/2016.
 */
@Ignore // before to execute this test change mockssso.xml on server -> tokenIdleTimeout="1"
public class TestCookieUpdate extends TestBase {

    public TestCookieUpdate() {
        entityName = "product_areas";
    }

    @Test
    public void testCookieUpdate() throws Exception {
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        String entityId = CommonUtils.getIdFromEntityModel(entityModel);

        int counter = 0;
        while (counter < 2) {
            sleepTime(70);
            EntityModel getEntity = entityList.at(entityId).get().execute();
            Assert.assertTrue(CommonUtils.isEntityAInEntityB(generatedEntity.iterator().next(), getEntity));
            counter++;
        }
    }

    private static void sleepTime(int sleepTimeInSec) {
        try {
            Thread.sleep(sleepTimeInSec * 1000);
        } catch (Exception e) {
            System.out.println("Sleep exception...");
        }
    }

    @Test
    public void testCookieUpdateForPost() throws Exception {
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, entityName);
        int counter = 0;
        while (counter < 2) {
            sleepTime(70);
            entityList.create().entities(generatedEntity).execute();
            counter++;
        }
    }
}

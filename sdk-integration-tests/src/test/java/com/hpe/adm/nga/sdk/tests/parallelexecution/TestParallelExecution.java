/*
 * Copyright 2016-2023 Open Text.
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
package com.hpe.adm.nga.sdk.tests.parallelexecution;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.AuthenticationUtils;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

/**
 *
 * Created by Dmitry Zavyalov on 09/05/2016.
 */

@Ignore //Before remove ignore, please support username = "rest2@hpe.com" with password = "Welcome2"
public class TestParallelExecution extends TestBase {

    @Test
    public void testParallelExecution_two_clients() throws Exception {
        String entityName1 = "product_areas";
        String entityName2 = "defects";

        Octane octane1 = getOctaneClientFirst();
        EntityList entityList1 = octane1.entityList(entityName1);

        Octane octane2 = getOctaneClientSecond();
        EntityList entityList2 = octane2.entityList(entityName2);

        Collection<EntityModel> generatedEntity1 = DataGenerator.generateEntityModel(octane1, entityName1);
        Collection<EntityModel> entityModels1 = entityList1.create().entities(generatedEntity1).execute();
        EntityModel entityModel1 = entityModels1.iterator().next();
        String entityId1 = CommonUtils.getIdFromEntityModel(entityModel1);

        Collection<EntityModel> generatedEntity2 = DataGenerator.generateEntityModel(octane2, entityName2);
        Collection<EntityModel> entityModels2 = entityList2.create().entities(generatedEntity2).execute();
        EntityModel entityModel2 = entityModels2.iterator().next();
        String entityId2 = CommonUtils.getIdFromEntityModel(entityModel2);

        int counter = 0;
        do {
            EntityModel getEntity1 = entityList1.at(entityId1).get().execute();
            Assert.assertTrue(CommonUtils.isEntityAInEntityB(generatedEntity1.iterator().next(), getEntity1));
            sleepTime(5);
            EntityModel getEntity2 = entityList2.at(entityId2).get().execute();
            Assert.assertTrue(CommonUtils.isEntityAInEntityB(generatedEntity2.iterator().next(), getEntity2));
            sleepTime(5);
            counter++;
        } while (counter < 5);
    }

    private Octane getOctaneClientFirst() {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authentication authentication = AuthenticationUtils.getAuthentication();
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        octane = ContextUtils.getContextWorkspace(url, authentication, sharedSpaceId, workspaceId);
        return octane;
    }

    private Octane getOctaneClientSecond() {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authentication authentication = new SimpleUserAuthentication("rest2@hpe.com", "Welcome2");
        String sharedSpaceId = "2002";
        String workspaceId = configuration.getString("sdk.workspaceId");

        octane = ContextUtils.getContextWorkspace(url, authentication, sharedSpaceId, workspaceId);
        return octane;
    }

    private void sleepTime(int sleepTimeInSec) {
        try {
            Thread.sleep(sleepTimeInSec * 1000);
        } catch (Exception e) {
            System.out.println("Sleep exception...");
        }
    }
}

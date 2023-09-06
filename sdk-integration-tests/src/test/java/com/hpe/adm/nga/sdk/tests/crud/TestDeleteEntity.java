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
package com.hpe.adm.nga.sdk.tests.crud;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

/**
 *
 * Created by Guy Guetta on 25/04/2016.
 */
public class TestDeleteEntity extends TestBase {
    public TestDeleteEntity() {
        entityName = "releases";
    }

    @Test
    public void testDeleteEntityById() throws Exception{
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();

        EntityModel entityModel = entityModels.iterator().next();
        String entityId = CommonUtils.getIdFromEntityModel(entityModel);

        entityList.at(entityId).delete().execute();

        Collection<EntityModel> getEntity = entityList.get().execute();

        List<String> entityIds = CommonUtils.getIdFromEntityModelCollection(getEntity);

        Assert.assertFalse(entityIds.contains(entityId));
    }

    @Test
    public void testDeleteEntitiesByQuery() throws Exception{
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(octane, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        List<String> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);

        Query query = QueryUtils.getQueryForIds(entityIds);

        entityList.delete().query(query).execute();

        Collection<EntityModel> getEntity = entityList.get().execute();

        List<String> actualEntityIds = CommonUtils.getIdFromEntityModelCollection(getEntity);

        //check there are no common ids
        actualEntityIds.retainAll(entityIds);

        Assert.assertTrue(actualEntityIds.isEmpty());
    }
}

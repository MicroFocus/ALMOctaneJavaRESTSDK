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

import com.hpe.adm.nga.sdk.model.DateFieldModel;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by Guy Guetta on 21/04/2016.
 */
public class TestUpdateEntity extends TestBase {

    public TestUpdateEntity() {
        entityName = "releases";
    }

    @Test
    public void testUpdateEntityById() throws Exception {

        String updatedNameValue = "updatedName" + UUID.randomUUID();
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, entityName, fields);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        String entityId = CommonUtils.getIdFromEntityModel(entityModel);

        StringFieldModel nameField = new StringFieldModel("name", updatedNameValue);
        fields.add(nameField);
        EntityModel updatedEntity = new EntityModel(fields);

        entityList.at(entityId).update().entity(updatedEntity).execute();

        EntityModel getEntity = entityList.at(entityId).get().addFields("name").execute();

        Assert.assertTrue(CommonUtils.isEntityAInEntityB(updatedEntity, getEntity));
    }

    @Test
    public void testUpdateEntityCollectionIdInBody() throws Exception {

        List<String> updatedNameValues = DataGenerator.generateNamesForUpdate();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(octane, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        List<String> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);

        Collection<EntityModel> updatedEntityCollection = new ArrayList<>();
        for (int i = 0; i < entityIds.size(); i++) {
            Set<FieldModel> fields = new HashSet<>();
            StringFieldModel nameField = new StringFieldModel("name", updatedNameValues.get(i));
            StringFieldModel id = new StringFieldModel("id", entityIds.get(i).toString());
            fields.add(nameField);
            fields.add(id);
            EntityModel updatedEntity = new EntityModel(fields);
            updatedEntityCollection.add(updatedEntity);
        }

        Query query = QueryUtils.getQueryForIds(entityIds);

        entityList.update().entities(updatedEntityCollection).execute();

        Collection<EntityModel> getEntity = entityList.get().addFields("name").query(query).execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(updatedEntityCollection, getEntity));

    }

    @Test // for release entity only
    public void testUpdateEntityCollectionWithQuery() throws Exception {

        String entityName = "releases";

        ZonedDateTime updatedEndDateValue = ZonedDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0).withZoneSameLocal(ZoneId.of("Z"));

        Collection<EntityModel> generatedDefect = DataGenerator.generateEntityModelCollection(octane, entityName);
        Collection<EntityModel> entityModels = octane.entityList(entityName).create().entities(generatedDefect).execute();
        List<String> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);

        Collection<EntityModel> updatedEntityCollection = new ArrayList<>();

        DateFieldModel nameField = new DateFieldModel("end_date", updatedEndDateValue);
        Set<FieldModel> fields = new HashSet<>();

        fields.add(nameField);
        EntityModel updatedEntity = new EntityModel(fields);
        updatedEntityCollection.add(updatedEntity);

        Query query = QueryUtils.getQueryForIds(entityIds);

        octane.entityList(entityName).update().entities(updatedEntityCollection).query(query).execute();

        Collection<EntityModel> getEntity = octane.entityList(entityName).get().addFields("end_date").query(query).execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(updatedEntityCollection, getEntity));
    }
}

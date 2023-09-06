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
package com.hpe.adm.nga.sdk.tests.errrohandling;

import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.exception.OctanePartialException;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Test octane server specific runtime exceptions
 */
public class TestOctaneException extends TestBase {

    public TestOctaneException() {
        entityName = "defects";
    }

    @Test
    public void testOctanePartialException() {

        EntityModel entityModel1 = new EntityModel();
        entityModel1.setValue(new StringFieldModel("name", "potato1"));

        //Field is not editable, entityModel1 should explode
        entityModel1.setValue(new LongFieldModel("version_stamp", 111L));

        EntityModel entityModel2 = new EntityModel();
        entityModel2.setValue(new StringFieldModel("name", "potato2"));

        //Should throw a partial exception, entityModel1 should fail to save
        try {
            entityList.create().entities(Arrays.asList(entityModel1, entityModel2)).execute();
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof OctanePartialException);

            //One should pass, one should fail
            assertEquals(1, ((OctanePartialException) ex).getEntitiesModels().size());
            return;
        }
        Assert.fail("Should have thrown an " + OctanePartialException.class.getName());
    }

    /**
     *  Update a single entity with an invalid field
     */
    @Test
    public void testOctaneException() {

        EntityModel entityModel = new EntityModel();
        entityModel.setValue(new StringFieldModel("name", "potato"));

        Collection<EntityModel> entityModels = entityList.create().entities(Collections.singletonList(entityModel)).execute();

        if(entityModels.size() != 1) {
            Assert.fail("Failed to create entity model for test");
        }

        entityModel = entityModels.iterator().next();

        //Field is not editable, should explode
        entityModel.setValue(new LongFieldModel("version_stamp", 111L));

        //Should throw an octane exception
        try {
            entityList.at(entityModel.getId()).update().entity(entityModel).execute();
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof OctaneException);

            OctaneException octaneException = (OctaneException) ex;
            assertEquals("platform.modify_non_editable_field", octaneException.getError().getValue("error_code").getValue());
            assertEquals(403L, octaneException.getError().getValue(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME).getValue());
            return;
        }

        Assert.fail("Should have thrown an " + OctaneException.class.getName());
    }

}

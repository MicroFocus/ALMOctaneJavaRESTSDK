/**
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
package com.hpe.adm.nga.sdk.model;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestEntityUtil {

    private FieldModel fieldModelOne;
    private FieldModel fieldModelTwo;

    @Test
    public void testLongFieldModelEquals() {
        fieldModelOne = new LongFieldModel("id", 1L);
        fieldModelTwo = new LongFieldModel("id", 1L);
        assertTrue(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testBooleanFieldModelEquals() {
        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new BooleanFieldModel("field", false);
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testStringFieldModelEquals() {
        fieldModelOne = new StringFieldModel("field", "string");
        fieldModelTwo = new StringFieldModel("field", "string");
        assertTrue(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testDifferentFieldModelEquals() {
        fieldModelOne = new StringFieldModel("fieldOne", "string");
        fieldModelTwo = new StringFieldModel("fieldTwo", "string");
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testLongStringFieldModelEquals() {
        fieldModelOne = new LongFieldModel("id", 1L);
        fieldModelTwo = new StringFieldModel("id", "1");
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testFloatLongFieldModelEquals() {
        fieldModelOne = new FloatFieldModel("id", 1.0F);
        fieldModelTwo = new LongFieldModel("id", 1L);
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testBooleanStringFieldModelEquals() {
        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new StringFieldModel("field", "true");
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testDateFieldModelEquals() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        fieldModelOne = new DateFieldModel("field", dateTime);
        fieldModelTwo = new DateFieldModel("field", dateTime);
        assertTrue(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new DateFieldModel("field", dateTime);
        fieldModelTwo = new DateFieldModel("field", dateTime.plusHours(1L));
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));
    }

    @Test
    public void testEntityModelEqualsByIdAndType() {
        EntityModel entityModelOne = new EntityModel();
        EntityModel entityModelTwo = new EntityModel();

        assertTrue(EntityUtil.areEqualByIdAndType(entityModelOne, entityModelTwo));

        entityModelOne.setValue(new StringFieldModel("id", "ID_HERE"));
        entityModelTwo.setValue(new StringFieldModel("id", "ID_HERE"));

        assertTrue(EntityUtil.areEqualByIdAndType(entityModelOne, entityModelTwo));

        entityModelOne.setValue(new StringFieldModel("type", "work_item"));
        entityModelTwo.setValue(new StringFieldModel("type", "work_item"));

        assertTrue(EntityUtil.areEqualByIdAndType(entityModelOne, entityModelTwo));

        entityModelOne.setValue(new StringFieldModel("type", "test"));

        assertFalse(EntityUtil.areEqualByIdAndType(entityModelOne, entityModelTwo));

        entityModelOne.setValue(new StringFieldModel("type", "work_item"));
        entityModelOne.setValue(new StringFieldModel("id", "NEW_ID"));

        assertFalse(EntityUtil.areEqualByIdAndType(entityModelOne, entityModelTwo));
    }

    @Test
    public void testEntityModelEqualsByContent() {
        EntityModel entityModelOne = new EntityModel();
        EntityModel entityModelTwo = new EntityModel();

        assertTrue(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        entityModelOne.setValue(new StringFieldModel("id", "ID_HERE"));
        assertFalse(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        entityModelTwo.setValue(new StringFieldModel("id", "ID_HERE"));
        assertTrue(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        entityModelOne.setValue(new LongFieldModel("newField", 1337L));
        assertFalse(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        entityModelTwo.setValue(new LongFieldModel("newField", 1337L));
        assertTrue(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        entityModelTwo.setValue(new LongFieldModel("newField", 1336L));
        assertFalse(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        entityModelTwo.setValue(new LongFieldModel("newField", 1337L));
        assertTrue(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        entityModelTwo.setValue(new DateFieldModel("newField", ZonedDateTime.now()));
        assertFalse(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

    }

    @Test
    public void testReferenceFieldModelEqualsByContent() {
        EntityModel entityModelOne = new EntityModel();
        EntityModel refEntityModelOne = new EntityModel();
        refEntityModelOne.setValue(new LongFieldModel("newField", 1337L));
        entityModelOne.setValue(new ReferenceFieldModel("refEntity", refEntityModelOne));

        EntityModel entityModelTwo = new EntityModel();
        EntityModel refEntityModelTwo = new EntityModel();
        refEntityModelTwo.setValue(new LongFieldModel("newField", 1337L));
        entityModelTwo.setValue(new ReferenceFieldModel("refEntity", refEntityModelTwo));

        assertTrue(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        //we have to go deeper
        EntityModel refRefEntityModelOne = new EntityModel();
        refRefEntityModelOne.setValue(new BooleanFieldModel("potato", true));
        refEntityModelOne.setValue(new ReferenceFieldModel("refRefEntity", refRefEntityModelOne));
        assertFalse(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));

        refEntityModelTwo.setValue(new ReferenceFieldModel("refRefEntity", refRefEntityModelOne));
        assertTrue(EntityUtil.areEqualByContent(entityModelOne, entityModelTwo));
    }

}

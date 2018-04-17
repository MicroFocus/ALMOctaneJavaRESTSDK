package com.hpe.adm.nga.sdk.model;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestEntityUtil {

    @Test
    @SuppressWarnings("rawtypes")
    public void testAreFieldModelsEqualByContent() {

        FieldModel fieldModelOne;
        FieldModel fieldModelTwo;

        fieldModelOne = new LongFieldModel("id", 1L);
        fieldModelTwo = new StringFieldModel("id", "1");
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new LongFieldModel("id", 1L);
        fieldModelTwo = new LongFieldModel("id", 1L);
        assertTrue(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new FloatFieldModel("id", 1.0F);
        fieldModelTwo = new LongFieldModel("id", 1L);
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new StringFieldModel("field", "true");
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new BooleanFieldModel("field", false);
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new BooleanFieldModel("field", true);
        assertTrue(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new StringFieldModel("field", "string");
        fieldModelTwo = new StringFieldModel("field", "string");
        assertTrue(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        ZonedDateTime dateTime = ZonedDateTime.now();
        fieldModelOne = new DateFieldModel("field", dateTime);
        fieldModelTwo = new DateFieldModel("field", dateTime);
        assertTrue(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new DateFieldModel("field", dateTime);
        fieldModelTwo = new DateFieldModel("field", dateTime.plusHours(1L));
        assertFalse(EntityUtil.areFieldModelsEqualByContent(fieldModelOne, fieldModelTwo));

        fieldModelOne = new StringFieldModel("fieldOne", "string");
        fieldModelTwo = new StringFieldModel("fieldTwo", "string");
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
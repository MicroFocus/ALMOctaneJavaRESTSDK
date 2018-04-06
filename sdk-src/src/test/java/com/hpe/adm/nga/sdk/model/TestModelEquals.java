package com.hpe.adm.nga.sdk.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class TestModelEquals {

    @Test
    @SuppressWarnings("rawtypes")
    public void testEqualsSimpleFieldModels() {

        FieldModel fieldModelOne;
        FieldModel fieldModelTwo;

        fieldModelOne = new LongFieldModel("id", 1L);
        fieldModelTwo = new StringFieldModel("id", "1");
        assertNotEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new LongFieldModel("id", 1L);
        fieldModelTwo = new LongFieldModel("id", 1L);
        assertEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new FloatFieldModel("id", 1.0F);
        fieldModelTwo = new LongFieldModel("id", 1L);
        assertNotEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new StringFieldModel("field", "true");
        assertNotEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new BooleanFieldModel("field", false);
        assertNotEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new BooleanFieldModel("field", true);
        fieldModelTwo = new BooleanFieldModel("field", true);
        assertEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new StringFieldModel("field", "string");
        fieldModelTwo = new StringFieldModel("field", "string");
        assertEquals(fieldModelOne, fieldModelTwo);

        ZonedDateTime dateTime = ZonedDateTime.now();
        fieldModelOne = new DateFieldModel("field", dateTime);
        fieldModelTwo = new DateFieldModel("field", dateTime);
        assertEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new DateFieldModel("field", dateTime);
        fieldModelTwo = new DateFieldModel("field", dateTime.plusHours(1L));
        assertNotEquals(fieldModelOne, fieldModelTwo);

        fieldModelOne = new StringFieldModel("fieldOne", "string");
        fieldModelTwo = new StringFieldModel("fieldTwo", "string");
        assertNotEquals(fieldModelOne, fieldModelTwo);
    }

    @Test
    public void testEqualsReferenceFieldModels() {

        EntityModel entityModelOne = new EntityModel();
        EntityModel relatedEntityModelOne = new EntityModel();

        entityModelOne.setValue(new LongFieldModel("id", 1L));
        entityModelOne.setValue(new StringFieldModel("someField", "someValue"));
        relatedEntityModelOne = new EntityModel();
        relatedEntityModelOne.setValue(new LongFieldModel("id", 1L));
        entityModelOne.setValue(new ReferenceFieldModel("refField", relatedEntityModelOne));

        EntityModel entityModelTwo = new EntityModel();
        EntityModel relatedEntityModelTwo = new EntityModel();

        entityModelTwo.setValue(new LongFieldModel("id", 1L));
        entityModelTwo.setValue(new StringFieldModel("someField", "someValue"));
        relatedEntityModelTwo = new EntityModel();
        relatedEntityModelTwo.setValue(new LongFieldModel("id", 1L));
        entityModelTwo.setValue(new ReferenceFieldModel("refField", relatedEntityModelTwo));

        assertEquals(entityModelOne, entityModelTwo);

        // go deeper
        relatedEntityModelOne.setValue(new ReferenceFieldModel("refField", entityModelOne));
        relatedEntityModelTwo.setValue(new ReferenceFieldModel("refField", entityModelOne));

        assertEquals(entityModelOne, entityModelTwo);

        relatedEntityModelOne.setValue(new ReferenceFieldModel("oneMoreRefField", entityModelOne));
        assertNotEquals(entityModelOne, entityModelTwo);
    }

}
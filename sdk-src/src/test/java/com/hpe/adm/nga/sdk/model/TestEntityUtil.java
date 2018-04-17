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

}

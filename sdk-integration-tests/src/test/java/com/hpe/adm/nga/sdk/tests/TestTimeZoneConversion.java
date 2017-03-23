package com.hpe.adm.nga.sdk.tests;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

/**
 * Created by savencu on 3/23/2017.
 */
public class TestTimeZoneConversion extends TestBase {
    @Before
    public void initialize() {
        entityName = "defects";
        entityList = octane.entityList(entityName);
    }

    @Test
    public void testTimeZone() throws Exception {
        int TIME_DIFFERENCE = 0;
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, entityName);
        Collection<EntityModel> createdEntities = DataGenerator.getAllDataForEntities(octane.entityList(entityName).create().entities(generatedEntity).execute(), octane, entityName);
        EntityModel entityModel = createdEntities.iterator().next();

        ZonedDateTime serverZuluTimeDate = (ZonedDateTime) entityModel.getValue("creation_time").getValue();
        ZonedDateTime convertedLocalDateTime = serverZuluTimeDate.withZoneSameInstant(ZoneId.systemDefault());

        ZonedDateTime mockLocalDateTime = ZonedDateTime.now();
        ZonedDateTime mockZuluDateTime = mockLocalDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        Assert.assertEquals(TIME_DIFFERENCE, ChronoUnit.MINUTES.between(serverZuluTimeDate, mockZuluDateTime));
        Assert.assertEquals(TIME_DIFFERENCE, ChronoUnit.MINUTES.between(convertedLocalDateTime, mockLocalDateTime));

        Assert.assertEquals(TIME_DIFFERENCE, ChronoUnit.HOURS.between(serverZuluTimeDate, mockZuluDateTime));
        Assert.assertEquals(TIME_DIFFERENCE, ChronoUnit.HOURS.between(convertedLocalDateTime, mockLocalDateTime));
        System.out.println(serverZuluTimeDate.withZoneSameInstant(ZoneOffset.ofHours(1)));
        System.out.println(serverZuluTimeDate.withZoneSameInstant(ZoneId.of("America/Los_Angeles")));
    }
}

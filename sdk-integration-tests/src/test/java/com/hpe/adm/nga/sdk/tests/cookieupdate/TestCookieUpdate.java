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
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        int counter = 0;
        while (counter < 10) {
            EntityModel getEntity = entityList.at(entityId).get().execute();
            Assert.assertTrue(CommonUtils.isEntityAInEntityB(generatedEntity.iterator().next(), getEntity));
            sleepTime(10);
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
}

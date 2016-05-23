package test.java.com.hpe.adm.nga.sdk.integration.tests.filtering;

import main.java.com.hpe.adm.nga.sdk.model.EntityModel;
import test.java.com.hpe.adm.nga.sdk.integration.tests.base.TestBase;
import test.java.com.hpe.adm.nga.sdk.integration.utils.CommonUtils;
import test.java.com.hpe.adm.nga.sdk.integration.utils.generator.DataGenerator;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * Created by perach on 08/05/2016.
 */
public class Limit extends TestBase {

    public Limit() {
        entityName = "releases";
    }

    @Test
    public void testLimit() throws Exception {

        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
        entityList.create().entities(generatedEntity).execute();

        Collection<EntityModel> getAllEntities = entityList.get().execute();
        int totalCount = getAllEntities.size();

        if (totalCount > 1) {

            Collection<EntityModel> getLimitEntities = entityList.get().limit(totalCount - 1).execute();
            int limit = getLimitEntities.size();

            Assert.assertTrue(limit + 1 == totalCount);
        }

    }

}

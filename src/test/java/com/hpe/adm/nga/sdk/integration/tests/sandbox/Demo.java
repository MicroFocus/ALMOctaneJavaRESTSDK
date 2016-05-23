package test.java.com.hpe.adm.nga.sdk.integration.tests.sandbox;

import main.java.com.hpe.adm.nga.sdk.model.EntityModel;
import test.java.com.hpe.adm.nga.sdk.integration.tests.base.TestBase;
import test.java.com.hpe.adm.nga.sdk.integration.utils.generator.DataGenerator;

import org.junit.Test;



import java.util.Collection;

/**
 * Created by Guy Guetta on 03/05/2016.
 */
public class Demo extends TestBase {

    public Demo() {
        entityName = "defect";
    }

    @Test
    public void demoTest() throws Exception {
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName);

    }
}

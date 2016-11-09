package com.hpe.adm.nga.sdk.tests.sandbox;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
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
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, entityName);

    }
}

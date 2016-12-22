package com.hpe.adm.nga.sdk.tests.authentication;

import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * Created by leufl on 14/11/2016.
 */
public class TestAuthentication extends TestBase {

    @Test
    public void testSignOut() throws Exception {
        octane.signOut();
        try {
            Collection<EntityModel> defectModel = octane.entityList("stories").get().execute();
            Assert.fail("Sign out failed.");
        } catch (OctaneException e) { }
    }
}

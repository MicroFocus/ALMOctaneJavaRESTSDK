package com.hpe.adm.nga.sdk.tests.siteadmin.server;

import com.hpe.adm.nga.sdk.siteadmin.version.Version;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

public class TestGetServerVersion extends TestBase {
    @Test
    public void testGetServerVersion() {
        final Version serverVersion = siteAdmin.getServer().getServerVersion().execute();
        Assert.assertNotNull(serverVersion);
        Assert.assertFalse(serverVersion.getVersion().isEmpty());
    }
}

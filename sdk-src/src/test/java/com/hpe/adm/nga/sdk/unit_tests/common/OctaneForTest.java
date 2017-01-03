package com.hpe.adm.nga.sdk.unit_tests.common;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * Created by brucesp on 21-Dec-16.
 */
class OctaneForTest extends Octane{
    OctaneForTest(OctaneHttpClient octaneHttpClient, String domain, String sharedSpaceId, long workId) {
        super(octaneHttpClient, domain, sharedSpaceId, workId);
    }
}

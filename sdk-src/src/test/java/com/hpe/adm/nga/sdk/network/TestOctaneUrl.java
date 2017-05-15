package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by brucesp on 15-May-17.
 */
public class TestOctaneUrl {

    /**
     * Test correct build of URL with field specification, limits, offset and orderBy.
     * The method invokes internal protected method with retrieved private parameters.
     */
    @Test
    public void testUrlBuilder() {
        final String expectedResult = CommonMethods.getDomain() + "?offset=1&limit=10&order_by=-version_stamp&fields=version_stamp,item_type";
        OctaneUrl octaneUrl = new OctaneUrl(CommonMethods.getDomain());
        octaneUrl.addFieldsParam("version_stamp", "item_type");
        octaneUrl.setLimitParam(10);
        octaneUrl.setOffsetParam(1);
        octaneUrl.setOrderByParam("version_stamp", false);

        assertEquals(expectedResult, octaneUrl.toString());
    }
}

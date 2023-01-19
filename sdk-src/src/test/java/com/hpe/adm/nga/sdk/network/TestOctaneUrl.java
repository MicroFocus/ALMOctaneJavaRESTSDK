/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link OctaneUrl}
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

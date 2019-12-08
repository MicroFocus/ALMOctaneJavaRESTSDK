/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package com.hpe.adm.nga.sdk.query;

import org.junit.Assert;
import org.junit.Test;

import static com.hpe.adm.nga.sdk.query.QueryMethod.COMPARISON_OPERATOR_BETWEEN;
import static com.hpe.adm.nga.sdk.query.QueryMethod.COMPARISON_OPERATOR_IN;

public class TestQueryMethod {

    @Test
    public void testInString() {
        final String queryResult = QueryMethod.In.getAction().apply("testField", new String[]{"1", "2", "3"});
        Assert.assertEquals("(testField " + COMPARISON_OPERATOR_IN + " '1','2','3')", queryResult);
    }

    @Test
    public void testInNumber() {
        final String queryResult = QueryMethod.In.getAction().apply("testField", new Long[]{1L, 2L, 3L});
        Assert.assertEquals("(testField " + COMPARISON_OPERATOR_IN + " 1,2,3)", queryResult);
    }

    @Test
    public void testInNull() {
        final String queryResult = QueryMethod.In.getAction().apply("testField", null);
        Assert.assertEquals("", queryResult);
    }

    @Test
    public void testInEmpty() {
        final String queryResult = QueryMethod.In.getAction().apply("testField", new String[]{""});
        Assert.assertEquals("(testField " + COMPARISON_OPERATOR_IN + " '')", queryResult);
    }

    @Test
    public void testBetweenNumber() {
        final String queryResult = QueryMethod.Between.getAction().apply("testField", new QueryMethod.Between("1000", "1020"));
        Assert.assertEquals("(testField " + COMPARISON_OPERATOR_BETWEEN + " '1000' ...'1020')", queryResult);
    }

    @Test
    public void testBetweenDate() {
        final String queryResult = QueryMethod.Between.getAction().apply("testField", new QueryMethod.Between("1000", "1020"));
        Assert.assertEquals("(testField " + COMPARISON_OPERATOR_BETWEEN + " '1000' ...'1020')", queryResult);
    }

}

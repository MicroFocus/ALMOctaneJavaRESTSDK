/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package com.hpe.adm.nga.sdk.model;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.Query.QueryBuilder;
import com.hpe.adm.nga.sdk.query.QueryMethod;
import org.junit.BeforeClass;
import org.junit.Test;
public class TestQuery {
	private static final String DATE_TIME_ISO_FORMAT 		= "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String DATE_TIME_UTC_ZONE_NAME 	= "UTC";
	private String expectedResult;
	private QueryBuilder queryBuilder;
	private static Date now;
	private static SimpleDateFormat dateFormat;
	
	@BeforeClass
	public static void initialize(){	
		now = new Date();
		dateFormat = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
		TimeZone utc = TimeZone.getTimeZone(DATE_TIME_UTC_ZONE_NAME);
		dateFormat.setTimeZone(utc);		
	}
	
	@Test
	public void testEquality() {
		expectedResult = "(id EQ 5)";
        queryBuilder = Query.statement("id", QueryMethod.EqualTo, 5);
		assertEquals(expectedResult, queryBuilder.build().getQueryString());
	}
	
	@Test
	public void testStringEquality(){
		expectedResult = "(name EQ 'test')";
        queryBuilder = Query.statement("name", QueryMethod.EqualTo, "test");
		assertEquals(expectedResult, queryBuilder.build().getQueryString());
	}
	
	@Test
	public void testDateFormat(){
		expectedResult = "(createn_time LT '" + dateFormat.format(now) + "')";
		queryBuilder = Query.statement("createn_time", QueryMethod.LessThan, now);
		assertEquals(expectedResult, queryBuilder.build().getQueryString());
	}
	
	@Test
	public void testComplexStatementOr(){
		expectedResult = "(creation_time LT '" + dateFormat.format(now) + "')||(id EQ '5028')||(id EQ '5015')";
		queryBuilder = Query.statement("creation_time", QueryMethod.LessThan, now)
                                .or("id", QueryMethod.EqualTo, "5028")
                                .or("id", QueryMethod.EqualTo, "5015");
		assertEquals(expectedResult, queryBuilder.build().getQueryString());
	}

	@Test
	public void testComplexStatementAndNegate(){
		expectedResult = "!(id GE '5028');!(name EQ '5028')";
        queryBuilder = Query.not("id", QueryMethod.GreaterThanOrEqualTo, "5028").andNot("name", QueryMethod.EqualTo, "5028");
		assertEquals(expectedResult, queryBuilder.build().getQueryString());
	}

    @Test
    public void testQueryBuilderComposition(){
        expectedResult = "(id GE '5028')||!(name EQ '5028')";
        QueryBuilder qb = Query.not("name", QueryMethod.EqualTo, "5028");
        queryBuilder = Query.statement("id", QueryMethod.GreaterThanOrEqualTo, "5028").or(qb);
        assertEquals(expectedResult, queryBuilder.build().getQueryString());
    }
}

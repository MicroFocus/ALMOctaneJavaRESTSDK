package com.hpe.adm.nga.sdk.unit_tests.model;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.Query.QueryBuilder;
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
        queryBuilder = new Query.QueryBuilder("id", Query::equalTo, 5);
		assertEquals(expectedResult, queryBuilder.getQueryString());
	}
	
	@Test
	public void testStringEquality(){
		expectedResult = "(name EQ 'test')";
        queryBuilder = new Query.QueryBuilder("name", Query::equalTo, "test");
		assertEquals(expectedResult, queryBuilder.getQueryString());
	}
	
	@Test
	public void testDateFormat(){
		expectedResult = "(createn_time LT '" + dateFormat.format(now) + "')";
		queryBuilder = new Query.QueryBuilder("createn_time", Query::lessThan, now);
		assertEquals(expectedResult, queryBuilder.getQueryString());
	}
	
	@Test
	public void testComplexStatementOr(){
		expectedResult = "(creation_time LT '" + dateFormat.format(now) + "')||(id EQ '5028')||(id EQ '5015')";
		queryBuilder = new Query.QueryBuilder("creation_time", Query::lessThan, now)
                                .or("id", Query::equalTo, "5028")
                                .or("id", Query::equalTo, "5015");
		assertEquals(expectedResult, queryBuilder.getQueryString());
	}

	@Test
	public void testComplexStatementAndNegate(){
		expectedResult = "!(id GE '5028');!(name EQ '5028')";
        queryBuilder = new Query.QueryBuilder("id", Query::greaterThanOrEqualTo, "5028", true).and("name", Query::equalTo, "5028", true);
		assertEquals(expectedResult, queryBuilder.getQueryString());
	}

    @Test
    public void testQueryBuilderComposition(){
        expectedResult = "(id GE '5028')||!(name EQ '5028')";
        QueryBuilder qb1 = new Query.QueryBuilder("id", Query::greaterThanOrEqualTo, "5028");
        QueryBuilder qb2 = new Query.QueryBuilder("name", Query::equalTo, "5028", true);
        queryBuilder = qb1.or(qb2);
        assertEquals(expectedResult, queryBuilder.getQueryString());
    }
}

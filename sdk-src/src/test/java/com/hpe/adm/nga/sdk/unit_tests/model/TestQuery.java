package com.hpe.adm.nga.sdk.unit_tests.model;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hpe.adm.nga.sdk.Query;

public class TestQuery {
	private static final String DATE_TIME_ISO_FORMAT 		= "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String DATE_TIME_UTC_ZONE_NAME 	= "UTC";
	private String expectedResult;
	private Query queryBuild;
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
		queryBuild = new Query.Field("id").equalTo(5).build();
		assertEquals(expectedResult, queryBuild.getQueryString());
	}
	
	@Test
	public void testStringEquality(){
		expectedResult = "(name EQ 'test')";
		queryBuild = new Query.Field("name").equalTo("test").build();
		assertEquals(expectedResult, queryBuild.getQueryString());
	}
	
	@Test
	public void testDateFormat(){			
		
		expectedResult = "(createn_time LT '" + dateFormat.format(now) + "')";
		queryBuild = new Query.Field("createn_time").lessThan(now).build();
		assertEquals(expectedResult, queryBuild.getQueryString());
	}
	
	@Test
	public void testComplexStatementOr(){
		expectedResult = "(creation_time LT '" + dateFormat.format(now) + "')||(id EQ '5028')||(id EQ '5015')";
		queryBuild = new Query.Field("creation_time").lessThan(now).or().field("id").equalTo("5028").or().field("id").equalTo("5015").build();
		assertEquals(expectedResult, queryBuild.getQueryString());
	}

	@Test
	public void testComplexStatementAndNegate(){
		expectedResult = "!(id GE '5028');!(name EQ '5028')";
		queryBuild = (new Query()).field("id").not().greaterOrEqualThan("5028").and().field("name").not().equalTo("5028").build();
		assertEquals(expectedResult, queryBuild.getQueryString());
	}
}

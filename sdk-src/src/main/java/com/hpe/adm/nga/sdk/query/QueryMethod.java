/*
 *
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
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
 */
package com.hpe.adm.nga.sdk.query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.BiFunction;

/**
 *
 * Created by busi on 04/01/2017.
 */


public enum QueryMethod {

    EqualTo(QueryMethod::equalTo),
    LessThan(QueryMethod::lessThan),
    GreaterThan(QueryMethod::greaterThan),
    GreaterThanOrEqualTo(QueryMethod::greaterThanOrEqualTo),
    LessThanOrEqualTo(QueryMethod::lessThanOrEqualTo);

    // constant
    private static final String DATE_TIME_ISO_FORMAT 	            = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DATE_TIME_UTC_ZONE_NAME 	        = "UTC";

    private static final String COMPARISON_OPERATOR_EQUALS 			= "EQ";
    private static final String COMPARISON_OPERATOR_LESS			= "LT";
    private static final String COMPARISON_OPERATOR_GREATER 		= "GT";
    private static final String COMPARISON_OPERATOR_LESS_EQUALS  	= "LE";
    private static final String COMPARISON_OPERATOR_GREATER_EQUALS 	= "GE";

    private final BiFunction<String, Object, String> function;
    QueryMethod(BiFunction<String, Object, String> f){
        this.function = f;
    }

    public BiFunction<String, Object, String> getAction(){
        return this.function;
    }


    /**
     * Generates a query string for "equal to" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    private static String equalTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_EQUALS + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "less than" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    private static String lessThan(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_LESS + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "greater than" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    private static String greaterThan(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_GREATER + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "less than or equal to" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    private static String lessThanOrEqualTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_LESS_EQUALS + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "greater than or equal to" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    private static String greaterThanOrEqualTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_GREATER_EQUALS + " " + toString(value) + ")";
    }


    /**
     * Generates a string representation of a given Object
     * @param value - Object to convert
     * @return string representation
     */
    private static String toString(Object value) {
        if( value == null) {
            return "{null}";
        } else if (value.getClass() == Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
            TimeZone utc = TimeZone.getTimeZone(DATE_TIME_UTC_ZONE_NAME);
            sdf.setTimeZone(utc);
            return "'" + sdf.format(value) + "'";
        } else if (value.getClass() == String.class) {
            return "'" +  value.toString() + "'";
        } else if (value.getClass() == Query.QueryBuilder.class) {
            return "{" + ((Query.QueryBuilder) value).build().getQueryString() + "}";
        } else {
            return value.toString();
        }
    }



}

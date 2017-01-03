package com.hpe.adm.nga.sdk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.BiFunction;

/**
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
 *
 * Created by leufl on 10/10/2016.
 */
public class Query {

    private String queryString = "";

    // constant
    private static final String DATE_TIME_ISO_FORMAT 	            = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DATE_TIME_UTC_ZONE_NAME 	        = "UTC";

    private static final String COMPARISON_OPERATOR_EQUALS 			= "EQ";
    private static final String COMPARISON_OPERATOR_LESS			= "LT";
    private static final String COMPARISON_OPERATOR_GREATER 		= "GT";
    private static final String COMPARISON_OPERATOR_LESS_EQUALS  	= "LE";
    private static final String COMPARISON_OPERATOR_GREATER_EQUALS 	= "GE";

    public Query() {}

    /**
     * Constructor
     * @param builder - the query builder to use for building the query
     */
    private Query(QueryBuilder builder) {
        queryString = builder.queryString;
    }

    /**
     * Accessor method of query string
     * @return query string
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * Mutator method of query string
     * @param queryString - query string
     */
    private void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * Appends new query to the existing one
     * @param queryString - query string
     */
    private void addQueryString(String queryString) {
        this.queryString += queryString;
    }

    /**
     * Generates a query string for "equal to" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    public static String equalTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_EQUALS + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "less than" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    public static String lessThan(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_LESS + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "greater than" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    public static String greaterThan(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_GREATER + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "less than or equal to" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    public static String lessThanOrEqualTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_LESS_EQUALS + " " + toString(value) + ")";
    }

    /**
     * Generates a query string for "greater than or equal to" comparison
     * @param field - field name
     * @param value - value to compare the field with
     * @return query string
     */
    public static String greaterThanOrEqualTo(String field, Object value) {
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
        } else if (value.getClass() == QueryBuilder.class) {
            return  "{" + ((QueryBuilder)value).getQueryString() +  "}" ;
        } else {
            return value.toString();
        }
    }

    public static class QueryBuilder {

        String queryString = "";

        /**
         * QueryBuilder constructor
         * @param fieldName - field name
         * @param function - comparison function to use
         * @param fieldValue - value to compare with
         */
        public QueryBuilder(String fieldName, BiFunction<String, Object, String> function, Object fieldValue) {
            this(fieldName, function, fieldValue, false);
        }

        /**
         * QueryBuilder constructor
         * @param fieldName - field name
         * @param function - comparison function to use
         * @param fieldValue - value to compare with
         * @param isNegate - a boolean value indicating if the function should be negated or not
         */
        public QueryBuilder(String fieldName, BiFunction<String, Object, String> function, Object fieldValue, boolean isNegate) {
            queryString = function.apply(fieldName, fieldValue);
            if (isNegate)
                queryString = negate(queryString);
        }

        /**
         * Generates a builder by applying the logical "and" operator between the current builder and the resulting operation of the input values.
         * @param fieldName - field name
         * @param function - comparison function to use
         * @param fieldValue - value to compare with
         * @return resulting builder
         */
        public QueryBuilder and(String fieldName, BiFunction<String, Object, String> function, Object fieldValue) {
            return and(fieldName, function, fieldValue, false);
        }

        /**
         * Generates a builder by applying the logical "and" operator between the current builder and the resulting operation of the input values.
         * @param fieldName - field name
         * @param function - comparison function to use
         * @param fieldValue - value to compare with
         * @param isNegate - a boolean value indicating if the function should be negated or not
         * @return resulting builder
         */
        public QueryBuilder and(String fieldName, BiFunction<String, Object, String> function, Object fieldValue, boolean isNegate) {
            String rightQueryString = function.apply(fieldName, fieldValue);
            if (isNegate)
                rightQueryString = negate(rightQueryString);
            queryString += ";" + rightQueryString;
            return this;
        }

        /**
         * Generates a builder by applying the logical "and" operator between the current builder and the input builder.
         * @param qb - query builder
         * @return resulting builder
         */
        public QueryBuilder and(QueryBuilder qb) {
            queryString += ";" + qb.getQueryString();
            return this;
        }

        /**
         * Generates a builder by applying the logical "or" operator between the current builder and the resulting operation of the input values.
         * @param fieldName - field name
         * @param function - comparison function to use
         * @param fieldValue - value to compare with
         * @return resulting builder
         */
        public QueryBuilder or(String fieldName, BiFunction<String, Object, String> function, Object fieldValue) {
            return or(fieldName, function, fieldValue, false);
        }

        /**
         * Generates a builder by applying the logical "or" operator between the current builder and the resulting operation of the input values.
         * @param fieldName - field name
         * @param function - comparison function to use
         * @param fieldValue - value to compare with
         * @param isNegate - a boolean value indicating if the function should be negated or not
         * @return resulting builder
         */
        public QueryBuilder or(String fieldName, BiFunction<String, Object, String> function, Object fieldValue, boolean isNegate) {
            String rightQueryString = function.apply(fieldName, fieldValue);
            if (isNegate)
                rightQueryString = negate(rightQueryString);
            queryString += "||" + rightQueryString;
            return this;
        }

        /**
         * Generates a builder by applying the logical "or" operator between the current builder and the input builder.
         * @param qb - query builder
         * @return resulting builder
         */
        public QueryBuilder or(QueryBuilder qb) {
            queryString += "||" + qb.getQueryString();
            return this;
        }

        /**
         * Accessor method for the query builder string.
         * @return query builder's string
         */
        public String getQueryString() {
            return queryString;
        }

        /**
         * Builds a query from the current builder
         * @return builded query
         */
        public Query build() {
            return new Query(this);
        }

        /**
         * Negates the given query string
         * @param queryString - input query string
         * @return resulting string after negation
         */
        private String negate(String queryString) {
            return "!" + queryString;
        }
    }
}

package com.hpe.adm.nga.sdk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.BiFunction;

/**
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

//    /**
//     * Mutator method of query string
//     * @param queryString - query string
//     */
//    private void setQueryString(String queryString) {
//        this.queryString = queryString;
//    }
//
//    /**
//     * Appends new query to the existing one
//     * @param queryString - query string
//     */
//    private void addQueryString(String queryString) {
//        this.queryString += queryString;
//    }

    public static String equalTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_EQUALS + " " + toString(value) + ")";
    }

    public static String lessThan(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_LESS + " " + toString(value) + ")";
    }

    public static String greaterThan(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_GREATER + " " + toString(value) + ")";
    }

    public static String lessThanOrEqualTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_LESS_EQUALS + " " + toString(value) + ")";
    }

    public static String greaterThanOrEqualTo(String field, Object value) {
        return "(" + field + " " + COMPARISON_OPERATOR_GREATER_EQUALS + " " + toString(value) + ")";
    }

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

        public QueryBuilder(String fieldName, BiFunction<String, Object, String> function, Object fieldValue) {
            this(fieldName, function, fieldValue, false);
        }

        public QueryBuilder(String fieldName, BiFunction<String, Object, String> function, Object fieldValue, boolean isNegate) {
            queryString = function.apply(fieldName, fieldValue);
            if (isNegate)
                queryString = negate(queryString);
        }

        public QueryBuilder and(String fieldName, BiFunction<String, Object, String> function, Object fieldValue) {
            return and(fieldName, function, fieldValue, false);
        }

        public QueryBuilder and(String fieldName, BiFunction<String, Object, String> function, Object fieldValue, boolean isNegate) {
            String rightQueryString = function.apply(fieldName, fieldValue);
            if (isNegate)
                rightQueryString = negate(rightQueryString);
            queryString += ";" + rightQueryString;
            return this;
        }

        public QueryBuilder or(String fieldName, BiFunction<String, Object, String> function, Object fieldValue) {
            return or(fieldName, function, fieldValue, false);
        }

        public QueryBuilder or(String fieldName, BiFunction<String, Object, String> function, Object fieldValue, boolean isNegate) {
            String rightQueryString = function.apply(fieldName, fieldValue);
            if (isNegate)
                rightQueryString = negate(rightQueryString);
            queryString += "||" + rightQueryString;
            return this;
        }

        public String getQueryString() {
            return queryString;
        }

        String negate(String queryString) {
            return "!(" + queryString + ")";
        }

        public Query build() {
            return new Query(this);
        }
    }
}

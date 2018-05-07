/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.query;

/**
 *
 * <p>
 *   The class to build complex queries.  An instance of this class is created using the {@link #statement(String, QueryMethod, Object)} or
 *   {@link #not(String, QueryMethod, Object)} methods.
 *   </p>
 *   <p>
 *   See the REST API documentation for information as to how to build queries.  This API supports building queries in a simplified Builder pattern
 *   </p>
 *   <p>
 *       Each query statement consists of the field name, query method and the object of the query.  The object can be a simple primitive or a further {@code Query} object
 *   </p>
 *   <p>
 *       For example:
 *       <br>
 *       To build a query such as {@code name eq 'entity_name'} the following code is used:
 *       <br>{@code Query.statement("name", QueryMethod.EqualTo, "entity_name");}<br>
 *       <br>
 *       To negate this statement such as {@code !name eq 'entity_name'} the following code is used:
 *       <br>{@code Query.not("name", QueryMethod.EqualTo, "entity_name");}<br>
 *   </p>
 *   <p>
 *       These statements produce a {@code QueryBuilder} object.  By calling the {@link QueryBuilder#build()} method this creates a {@code Query object} which can then
 *       be used in conjunction with entity or other contexts
 *   </p>
 *   <p>
 *       {@code Query} objects can be chained together (and/or) by using the correct methods such as {@link QueryBuilder#and(QueryBuilder)} or
 *       {@link QueryBuilder#orNot(String, QueryMethod, Object)}
 *       <br>
 *       {@link QueryBuilder#build()} should be called once the entire query has been built
 *   </p>
 */
public class Query {

    protected String queryString = "";

    protected Query() {}

    /**
     * Negates the given query string
     * @param queryString - input query string
     * @return resulting string after negation
     */
    private static String negate(String queryString) {
        return "!" + queryString;
    }

    /**
     * QueryBuilder Statement
     * @param fieldName - field name
     * @param method - comparison function to use
     * @param fieldValue - value to compare with
     * @return The new object that can be used to build the query
     */
    public static QueryBuilder statement(String fieldName, QueryMethod method, Object fieldValue) {
        return new QueryBuilder(method.getAction().apply(fieldName, fieldValue));
    }

    /**
     * QueryBuilder not
     * @param fieldName - field name
     * @param method - comparison function to use
     * @param fieldValue - value to compare with
     * @return The new object that can be used to build the query
     */
    public static QueryBuilder not(String fieldName, QueryMethod method, Object fieldValue) {
        return new QueryBuilder(negate(method.getAction().apply(fieldName, fieldValue)));
    }

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
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return queryString;
    }

    /**
     * Object that is used to build {@link Query} objects.
     * @see Query for more information
     */
    public static class QueryBuilder {

        private String queryString = "";

        private QueryBuilder(String queryString) {
            this.queryString = queryString;
        }

        /**
         * Accessor method for the query builder string.
         * @return query builder's string
         */
        private String getQueryString() {
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
         * Generates a builder by applying the logical "and" operator between the current builder and the resulting operation of the input values.
         * @param fieldName - field name
         * @param method - comparison function to use
         * @param fieldValue - value to compare with
         * @return resulting builder
         */
        public QueryBuilder and(String fieldName, QueryMethod method, Object fieldValue) {
            String rightQueryString = method.getAction().apply(fieldName, fieldValue);
            queryString += ";" + rightQueryString;
            return this;
        }

        /**
         * Generates a builder by applying the logical "and" operator between the current builder and the resulting operation of the input values after negation.
         * @param fieldName - field name
         * @param method - comparison function to use
         * @param fieldValue - value to compare with
         * @return resulting builder
         */
        public QueryBuilder andNot(String fieldName, QueryMethod method, Object fieldValue) {
            queryString += ";" + negate(method.getAction().apply(fieldName, fieldValue));
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
         * Generates a builder by applying the logical "and" operator between the current builder and the input builder after negation.
         * @param qb - query builder
         * @return resulting builder
         */
        public QueryBuilder andNot(QueryBuilder qb) {
            queryString += ";" + negate(qb.getQueryString());
            return this;
        }

        /**
         * Generates a builder by applying the logical "or" operator between the current builder and the resulting operation of the input values.
         * @param fieldName - field name
         * @param method - comparison function to use
         * @param fieldValue - value to compare with
         * @return resulting builder
         */
        public QueryBuilder or(String fieldName, QueryMethod method, Object fieldValue) {
            String rightQueryString = method.getAction().apply(fieldName, fieldValue);
            queryString += "||" + rightQueryString;
            return this;
        }

        /**
         * Generates a builder by applying the logical "or" operator between the current builder and the resulting operation of the input values after negation.
         * @param fieldName - field name
         * @param method - comparison function to use
         * @param fieldValue - value to compare with
         * @return resulting builder
         */
        public QueryBuilder orNot(String fieldName, QueryMethod method, Object fieldValue) {
            queryString += "||" + negate(method.getAction().apply(fieldName, fieldValue));
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
         * Generates a builder by applying the logical "or" operator between the current builder and the input builder after negation.
         * @param qb - query builder
         * @return resulting builder
         */
        public QueryBuilder orNot(QueryBuilder qb) {
            queryString += "||" + negate(qb.getQueryString());
            return this;
        }

        /**
         * Wraps current builder content into a parenthesis
         * @return with parentheses
         */
        private QueryBuilder parenthesis() {
            queryString = "(" + queryString + ")";
            return this;
        }

        /**
         * Wraps current builder content into a parenthesis
         * @param queryBuilder {@link QueryBuilder} to wrap into parenthesis
         * @return enclose the inner query of the QueryBuilder in parenthesis
         */
        public static QueryBuilder parenthesis(QueryBuilder queryBuilder) {
            return queryBuilder.parenthesis();
        }
    }
}
package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.query.Query;

/**
 * Variant of the Query object that allows you to bypass using a query builder
 * and just write the query string directly. <br>
 * Can be useful when you already have the query built, for example,
 * it was fetched from the Octane server from some business rule.
 */
public class StringQuery extends Query {

    private StringQuery() {
        super();
    }

    /**
     * Create a query object from a string directly, not validated client side
     * @param queryString string to put as the request's "query" query param value
     * @return com.hpe.adm.nga.sdk.extension.StringQuery to use in an OctaneRequest
     */
    public static StringQuery fromString(String queryString){
        StringQuery query = new StringQuery();
        query.queryString = queryString;
        return query;
    }

}
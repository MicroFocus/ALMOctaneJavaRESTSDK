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

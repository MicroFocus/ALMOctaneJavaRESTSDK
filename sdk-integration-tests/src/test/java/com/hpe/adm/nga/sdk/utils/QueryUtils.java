package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;

import java.util.List;

/**
 *
 * Created by Guy Guetta on 21/04/2016.
 */
public class QueryUtils {
    public static Query getQueryForIds(List<String> entityIds) {

        Query.QueryBuilder queryBuilder = null;

        for (int i = 0; i < entityIds.size(); i++) {
            if (queryBuilder == null) {
                queryBuilder = Query.statement("id", QueryMethod.EqualTo, entityIds.get(i));
            } else {
                queryBuilder.or("id", QueryMethod.EqualTo, entityIds.get(i));
            }
        }
        return queryBuilder.build();
    }
}

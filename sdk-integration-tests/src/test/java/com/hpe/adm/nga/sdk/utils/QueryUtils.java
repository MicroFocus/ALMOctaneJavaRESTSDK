package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.Query.QueryBuilder;

import java.util.List;

/**
 * Created by Guy Guetta on 21/04/2016.
 */
public class QueryUtils {
    public static Query getQueryForIds(List<Integer> entityIds) {
        Query query = new Query();

        QueryBuilder queryBuilder = null;

        for (int i = 0; i < entityIds.size(); i++) {
            if (queryBuilder == null) {
                queryBuilder = new Query.QueryBuilder("id", Query::equalTo, entityIds.get(i));
            } else {
                queryBuilder.or("id", Query::equalTo, entityIds.get(i));
            }
        }
        return queryBuilder.build();
    }
}

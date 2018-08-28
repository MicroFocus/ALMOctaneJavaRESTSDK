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

package com.microfocus.adm.nga.sdk.network;


import com.microfocus.adm.nga.sdk.query.Query;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for building URLs for the implementation of the {@link OctaneHttpClient}
 */
public final class OctaneUrl {

    private static final String LIMIT_PARAM_NAME = "limit";
    private static final String OFFSET_PARAM_NAME = "offset";
    private static final String FIELDS_PARAM_NAME = "fields";
    private static final String ORDER_BY_PARAM_NAME = "order_by";
    private static final String QUERY_PARAM_NAME = "query";

    private static final String PATH_SEPARATOR = "/";

    private String baseUrl;
    private Map<String, String> queryParams = new HashMap<>();
    private List<String> paths = new ArrayList<>();

    /**
     * Create a url starting from the provided base
     * @param baseUrl scheme:[//[user[:password]@]host[:port]]
     */
    OctaneUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    /**
     * Add a path or more paths to the url, paths are concatenated into the final url
     * @param paths string or a list of strings representing the path from the base url
     */
    void addPaths(String paths){
        String[] subPaths =  paths.split(PATH_SEPARATOR);
        this.paths.addAll(Arrays.asList(subPaths));
    }

    /**
     * Return the current paths
     * @return list of strings representing the path of the current url
     */
    public List<String> getPaths(){
        return paths;
    }

    /**
     * Check whether the query param with {@code paramName} has already been set via {@link #setParam(String, String)}
     * @param paramName name of the parameter
     * @return true if the param was set, false otherwise
     */
    public boolean hasParam(String paramName){
        return queryParams.containsKey(paramName);
    }

    /**
     * Set query param
     * @param paramName name of the parameter
     * @param paramValue value of the parameter
     */
    public void setParam(String paramName, String paramValue){
        queryParams.put(paramName, paramValue);
    }

    /**
     * Get the value of the query param
     * @param paramName name of the parameter
     * @return value of the parameter
     */
    public String getParam(String paramName){
        return queryParams.get(paramName);
    }


    /**
     * Set hte value of the "fields" param,
     * @param fields list of fields of the Entity to be retrieved
     */
    public void addFieldsParam(String... fields) {
        String fieldsString = Arrays.stream(fields).collect(Collectors.joining(","));
        if(hasParam(OctaneUrl.FIELDS_PARAM_NAME)){
            getParam(OctaneUrl.FIELDS_PARAM_NAME + "," + fieldsString);
        } else {
            setParam(OctaneUrl.FIELDS_PARAM_NAME, fieldsString);
        }
    }

    /**
     * Set query param named "limit", for the Octane API it controls max number of results returned from the data-set, <br>
     * can be used for pagination together with {@link #setOffsetParam(int)}
     * @param limit value of limit
     */
    public void setLimitParam(int limit) {
        setParam(OctaneUrl.LIMIT_PARAM_NAME, String.valueOf(limit));
    }

    /**
     * Set query param named "offset", for the Octane API it controls the offset from the fist entity of the data-set, <br>
     * can be used for pagination together with {@link #setLimitParam(int)}
     * @param offset value of offset
     */
    public void setOffsetParam(int offset) {
        setParam(OctaneUrl.OFFSET_PARAM_NAME, String.valueOf(offset));
    }

    /**
     * Set the value of the "order_by" param
     * @param orderBy name of a field of an {@link com.microfocus.adm.nga.sdk.model.EntityModel}, to sort by
     * @param asc true for ascending, false for descending
     */
    public void setOrderByParam(String orderBy, boolean asc) {
        String ascString = asc ? "" : "-";
        String orderByString = ascString + orderBy;
        setParam(OctaneUrl.ORDER_BY_PARAM_NAME, orderByString);
    }

    /**
     * Set a param named "query" that is used to filter data
     * @param query {@link Query} object, build by {@link com.microfocus.adm.nga.sdk.query.Query.QueryBuilder}
     */
    public void setDqlQueryParam(Query query) {
        setParam(OctaneUrl.QUERY_PARAM_NAME, '"' + query.getQueryString() + '"');
    }

    /**
     * Concatenate the query params for the url builder
     * @return String of form: queryParamName1=queryParamValue1&queryParamName2=queryParamValue2
     */
    private String createQueryString(){
        return queryParams
                .keySet()
                .stream()
                .map(key -> key + "=" + queryParams.get(key))
                .collect(Collectors.joining("&"));
    }

    /**
     * Build the url string from the state of this object
     * @return URL string containing base url with paths and query params
     */
    @Override
    public String toString() {
        String url = baseUrl;

        //Append paths
        if(getPaths().size()>0){
            if (!url.endsWith("/")) {
                url += "/";
            }
            url += getPaths().stream().collect(Collectors.joining(PATH_SEPARATOR));
        }

        //Append query params
        url = queryParams.size() > 0 ? url + "?" + createQueryString()  : url;

        return url;
    }
}
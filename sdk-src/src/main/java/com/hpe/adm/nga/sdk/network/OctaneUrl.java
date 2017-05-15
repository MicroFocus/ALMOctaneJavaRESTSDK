package com.hpe.adm.nga.sdk.network;


import com.hpe.adm.nga.sdk.query.Query;

import java.util.*;
import java.util.stream.Collectors;

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

    OctaneUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    void addPath(String path){
        String[] subPaths =  path.split(PATH_SEPARATOR);
        paths.addAll(Arrays.asList(subPaths));
    }

    private List<String> getPaths(){
        return paths;
    }

    private boolean hasParam(String paramName){
        return queryParams.containsKey(paramName);
    }

    public void setParam(String paramName, String paramValue){
        queryParams.put(paramName, paramValue);
    }

    private String getParam(String paramName){
        return queryParams.get(paramName);
    }

    private String createQueryString(){
        return queryParams
                .keySet()
                .stream()
                .map(key -> key + "=" + queryParams.get(key))
                .collect(Collectors.joining("&"));
    }

    public void addFieldsParam(String... fields) {
        String fieldsString = Arrays.stream(fields).collect(Collectors.joining(","));
        if(hasParam(OctaneUrl.FIELDS_PARAM_NAME)){
            getParam(OctaneUrl.FIELDS_PARAM_NAME + "," + fieldsString);
        } else {
            setParam(OctaneUrl.FIELDS_PARAM_NAME, fieldsString);
        }
    }

    public void setLimitParam(int limit) {
        setParam(OctaneUrl.LIMIT_PARAM_NAME, String.valueOf(limit));
    }

    public void setOffsetParam(int offset) {
        setParam(OctaneUrl.OFFSET_PARAM_NAME, String.valueOf(offset));
    }

    public void setOrderByParam(String orderBy, boolean asc) {
        String ascString = asc ? "" : "-";
        String orderByString = ascString + orderBy;
        setParam(OctaneUrl.ORDER_BY_PARAM_NAME, orderByString);
    }

    /**
     * Set a param named "query" that is used to filter data
     * @param query
     */
    public void setDqlQueryParam(Query query) {
        setParam(OctaneUrl.QUERY_PARAM_NAME, '"' + query.getQueryString() + '"');
    }

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
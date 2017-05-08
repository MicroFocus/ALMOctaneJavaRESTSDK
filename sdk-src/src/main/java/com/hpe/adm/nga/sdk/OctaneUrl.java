package com.hpe.adm.nga.sdk;


import java.util.*;
import java.util.stream.Collectors;

public class OctaneUrl {

    public static final String LIMIT_PARAM_NAME = "limit";
    public static final String OFFSET_PARAM_NAME = "offset";
    public static final String FIELDS_PARAM_NAME = "fields";
    public static final String ORDER_BY_PARAM_NAME = "order_by";
    public static final String QUERY_PARAM_NAME = "query";

    private static final String PATH_SEPARATOR = "/";

    private String baseUrl;
    private Map<String, String> queryParams = new HashMap<>();
    private List<String> paths = new ArrayList<>();

    public OctaneUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public void addPath(String path){
        String[] subPaths =  path.split(PATH_SEPARATOR);
        paths.addAll(Arrays.asList(subPaths));
    }

    public List<String> getPaths(){
        return paths;
    }

    public boolean hasParam(String paramName){
        return queryParams.containsKey(paramName);
    }

    public void setParam(String paramName, String paramValue){
        queryParams.put(paramName, paramValue);
    }

    public String getParam(String paramName){
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
            url += getPaths().stream().collect(Collectors.joining(PATH_SEPARATOR));
        }

        //Append query params
        url = queryParams.size() > 0 ? url + "?" + createQueryString()  : url;

        return url;
    }
}
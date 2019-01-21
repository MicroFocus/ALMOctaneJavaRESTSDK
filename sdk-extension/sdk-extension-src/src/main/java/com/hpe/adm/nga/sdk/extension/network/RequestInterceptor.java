package com.hpe.adm.nga.sdk.extension.network;

import java.util.Map;

/**
 * Interceptor for http requests
 * Methods are called before request is passed to the {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
 */
public interface RequestInterceptor {

    /**
     * Get and change the url before the request is executed
     * @param url original url
     * @return new url that is passed {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
     */
    default String url(String url){
        return url;
    }

    /**
     * Get and change the content before the request is executed
     * @param content original content of the request
     * @return new content that is passed {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
     */
    default String content(String content){ return content; }

    /**
     * Get and change the headers before the request is executed
     * @param headers headers of the original request
     * @return new headers that are passed {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
     */
    default Map<String, Object> headers(Map<String, Object> headers){
        return headers;
    }
}
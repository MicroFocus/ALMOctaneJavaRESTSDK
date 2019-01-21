package com.hpe.adm.nga.sdk.extension.network;

import java.util.Map;

/**
 * Interceptor for http response
 * Methods after the response is received by the {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
 */
public interface ResponseInterceptor {

    /**
     * Get and change the headers after the request is executed
     * @param headers headers of the original response
     * @return new headers that are passed {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient} implementation
     */
    default Map<String, Object> headers(Map<String, Object> headers){
        return headers;
    }

}
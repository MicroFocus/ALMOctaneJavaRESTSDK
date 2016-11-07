package com.hpe.adm.nga.sdk.network;

/**
 * Exception thrown when an error status code is detected in an HTTP response.
 *
 * Created by leufl on 7/11/2016.
 */

public class HttpResponseException extends com.google.api.client.http.HttpResponseException {

    /**
     * Constructor that constructs a detail message from the given HTTP response that includes the status code, status message and HTTP response content.
     * Callers of this constructor should call HttpResponse.disconnect() after HttpResponseException is instantiated.
     * @param response - HTTP response
     */
    public HttpResponseException(com.google.api.client.http.HttpResponse response) {
        super(response);
    }
}

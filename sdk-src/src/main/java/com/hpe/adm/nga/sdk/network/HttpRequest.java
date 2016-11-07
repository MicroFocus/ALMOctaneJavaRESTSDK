package com.hpe.adm.nga.sdk.network;

import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;

import java.io.IOException;

/**
 * HTTP request.
 *
 * Created by leufl on 2/11/2016.
 */
public class HttpRequest {
    com.google.api.client.http.HttpRequest request;

    /**
     * Creates a HTTP request.
     * @param request
     */
    public HttpRequest(com.google.api.client.http.HttpRequest request) {
        this.request = request;
    }

    /**
     * Sets the authorization header as specified in Basic Authentication Scheme.
     * @param username
     * @param password
     */
    public void setBasicAuthentication(String username, String password) {
        request.getHeaders().setBasicAuthentication(username, password);
    }

    /**
     * Sets the authorization header as specified in Client Authentication Scheme.
     * @param clientId
     * @param clientSecret
     */
    public void setClientAuthentication(String clientId, String clientSecret) {
        final GenericData genericData = new GenericData();
        genericData.put("client_id", clientId);
        genericData.put("client_secret", clientSecret);
        setContent(genericData);
    }

    /**
     * Sets the authorization header as specified in User Authentication Scheme.
     * @param username
     * @param password
     */
    public void setUserAuthentication(String username, String password) {
        GenericData genericData = new GenericData();
        genericData.put("user", username);
        genericData.put("password", password);
        setContent(genericData);
    }

    /**
     * Sets request content.
     * @param genericData
     * @return
     */
    public com.google.api.client.http.HttpRequest setContent(GenericData genericData) {
        return request.setContent(new JsonHttpContent(new JacksonFactory(), genericData));
    }

//    /**
//     * Sets the HTTP request content.
//     * @param genericData - key name/value data
//     * @return
//     */
//    public com.google.api.client.http.HttpRequest setContent(GenericData genericData) {
//        return request.setContent(new JsonHttpContent(new JacksonFactory(), genericData));
//    }

    /**
     * Sets the HTTP request content or null for none.
     * @param response
     * @return
     */
    public com.google.api.client.http.HttpRequest setContent(HttpResponse response) {
        return request.setContent(ByteArrayContent.fromString(null, response.toString()));
    }

    /**
     * Sets the HTTP unsuccessful (non-2XX) response handler or null for none.
     * @param unsuccessfulResponseHandler
     * @return
     */
    public com.google.api.client.http.HttpRequest setUnsuccessfulResponseHandler(HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler) {
        return request.setUnsuccessfulResponseHandler(unsuccessfulResponseHandler);
    }

    /**
     * @return - Returns the HTTP request method or null for none.
     */
    public String getRequestMethod() {
        return request.getRequestMethod();
    }

    /**
     * @return - Returns the HTTP request URL.
     */
    public GenericUrl getUrl() {
        return request.getUrl();
    }

    /**
     * @return - Returns the HTTP request headers.
     */
    public HttpHeaders getHeaders() {
        return request.getHeaders();
    }

    /**
     * @return - Returns the HTTP response headers.
     */
    public HttpHeaders getResponseHeaders() {
        return request.getResponseHeaders();
    }

    /**
     * Execute the HTTP request and returns the HTTP response.
     * @return - HTTP response for an HTTP success response (or HTTP error response if getThrowExceptionOnExecuteError() is false)
     * @throws HttpResponseException - for an HTTP error response (only if getThrowExceptionOnExecuteError() is true)
     * @throws IOException
     */
    public HttpResponse execute() throws IOException {
        return new HttpResponse(request.execute());
    }
}

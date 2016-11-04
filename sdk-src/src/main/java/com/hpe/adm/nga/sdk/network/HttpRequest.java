package com.hpe.adm.nga.sdk.network;

import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;

import java.io.IOException;

/**
 * Created by leufl on 2/11/2016.
 */
public class HttpRequest {
    com.google.api.client.http.HttpRequest request;

    public HttpRequest(com.google.api.client.http.HttpRequest request) {
        this.request = request;
    }

    public void setBasicAuthentication(String username, String password) {
        request.getHeaders().setBasicAuthentication(username, password);
    }

    public com.google.api.client.http.HttpRequest setContent(GenericData genericData) {
        return request.setContent(new JsonHttpContent(new JacksonFactory(), genericData));
    }

    public com.google.api.client.http.HttpRequest setContent(HttpResponse response) {
        return request.setContent(ByteArrayContent.fromString(null, response.toString()));
    }

    public com.google.api.client.http.HttpRequest setUnsuccessfulResponseHandler(HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler) {
        return request.setUnsuccessfulResponseHandler(unsuccessfulResponseHandler);
    }

    public String getRequestMethod() {
        return request.getRequestMethod();
    }

    public GenericUrl getUrl() {
        return request.getUrl();
    }

    public HttpHeaders getHeaders() {
        return request.getHeaders();
    }

    public HttpHeaders getResponseHeaders() {
        return request.getResponseHeaders();
    }

    public HttpResponse execute() throws IOException {
        return request.execute();
    }
}

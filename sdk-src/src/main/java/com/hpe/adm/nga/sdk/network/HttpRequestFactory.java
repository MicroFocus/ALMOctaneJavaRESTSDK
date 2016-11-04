package com.hpe.adm.nga.sdk.network;

import com.google.api.client.http.*;

import java.io.IOException;

/**
 * Created by leufl on 2/11/2016.
 */
public class HttpRequestFactory {
    private com.google.api.client.http.HttpRequestFactory requestFactory;

    public HttpRequestFactory(com.google.api.client.http.HttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public void initialize(com.google.api.client.http.HttpRequest var1) throws IOException {
        requestFactory.getInitializer().initialize(var1);
    }

    public HttpRequest buildRequest(String requestMethod, GenericUrl url, HttpContent content) throws IOException {
        return new HttpRequest(requestFactory.buildRequest(requestMethod, url, content));
    }

    public HttpRequest buildDeleteRequest(GenericUrl url) throws IOException {
        return new HttpRequest(requestFactory.buildDeleteRequest(url));
    }

    public HttpRequest buildGetRequest(GenericUrl url) throws IOException {
        return new HttpRequest(requestFactory.buildGetRequest(url));
    }

    public HttpRequest buildPostRequest(GenericUrl url, HttpContent content) throws IOException {
        return new HttpRequest(requestFactory.buildPostRequest(url, content));
    }

    public HttpRequest buildPutRequest(GenericUrl url, HttpContent content) throws IOException {
        return new HttpRequest(requestFactory.buildPutRequest(url, content));
    }

    public HttpRequest buildPatchRequest(GenericUrl url, HttpContent content) throws IOException {
        return new HttpRequest(requestFactory.buildPatchRequest(url, content));
    }

    public HttpRequest buildHeadRequest(GenericUrl url) throws IOException {
        return new HttpRequest(requestFactory.buildHeadRequest(url));
    }
}

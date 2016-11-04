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

    public HttpRequest buildRequest(String requestMethod, String url, HttpContent content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildRequest(requestMethod, domain, content));
    }

    public HttpRequest buildDeleteRequest(String url) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildDeleteRequest(domain));
    }

    public HttpRequest buildGetRequest(String url) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildGetRequest(domain));
    }

    public HttpRequest buildPostRequest(String url, String content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPostRequest(domain, ByteArrayContent.fromString(null, content)));
    }

    public HttpRequest buildPostRequest(String url, HttpContent content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPostRequest(domain, content));
    }

    public HttpRequest buildPutRequest(String url, String content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPutRequest(domain, ByteArrayContent.fromString(null, content)));
    }

    public HttpRequest buildPatchRequest(String url, String content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPatchRequest(domain, ByteArrayContent.fromString(null, content)));
    }

    public HttpRequest buildHeadRequest(String url) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildHeadRequest(domain));
    }
}

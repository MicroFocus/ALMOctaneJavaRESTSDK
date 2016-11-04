package com.hpe.adm.nga.sdk.network;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpStatusCodes;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by leufl on 2/11/2016.
 */
public class HttpResponse {
    private com.google.api.client.http.HttpResponse response;

    public HttpResponse(com.google.api.client.http.HttpResponse response) {
        this.response = response;
    }

    public boolean isSuccessStatusCode() {
        return response.isSuccessStatusCode();
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public String getStatusMessage() {
        return response.getStatusMessage();
    }

    public HttpHeaders getHeaders() {
        return response.getRequest().getResponseHeaders();
    }

    public String parseAsString() throws IOException {
        return response.parseAsString();
    }

    public InputStream getContent() throws IOException {
        return response.getContent();
    }
}

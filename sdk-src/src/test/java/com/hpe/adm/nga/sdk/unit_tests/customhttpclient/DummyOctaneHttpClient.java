package com.hpe.adm.nga.sdk.unit_tests.customhttpclient;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Dummy http client that always returns an empty set of entities
 */
public class DummyOctaneHttpClient implements OctaneHttpClient {

    private String stringConstructorArg;

    public DummyOctaneHttpClient(String stringConstructorArg){
        this.stringConstructorArg = stringConstructorArg;
    }

    @Override
    public boolean authenticate(Authentication authentication) {
        return true;
    }

    @Override
    public void signOut() {}

    @Override
    public OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest) {
        //Return empty response
        String returnJson = "{\"total_count\":0,\"data\":[],\"exceeds_total_count\":false}";
        InputStream stream = new ByteArrayInputStream(returnJson.getBytes(StandardCharsets.UTF_8));
        return new OctaneHttpResponse(202, returnJson, stream);
    }

}
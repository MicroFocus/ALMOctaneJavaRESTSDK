package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.authentication.SimpleBasicAuthentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;

public class TestTest {

    public static void main(String[] args) {
        final OctaneWrapper.BasicAuthenticationOctaneWrapperBuilder basicAuthenticationOctaneWrapperBuilder =
                new OctaneWrapper.Builder().authentication(new SimpleBasicAuthentication("", ""));
        basicAuthenticationOctaneWrapperBuilder.build().octane();
        final OctaneWrapper.ExplicitAuthenticationOctaneWrapperBuilder explicitAuthenticationOctaneWrapperBuilder =
                new OctaneWrapper.Builder().authentication(new SimpleUserAuthentication("", ""));
        final OctaneWrapper.ExplicitAuthenticationOctaneWrapper explicitAuthenticationOctaneWrapper = explicitAuthenticationOctaneWrapperBuilder.build();
        explicitAuthenticationOctaneWrapper.authenticate().execute();
        explicitAuthenticationOctaneWrapper.octane();
    }

}

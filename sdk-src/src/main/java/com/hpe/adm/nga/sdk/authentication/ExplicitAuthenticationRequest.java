package com.hpe.adm.nga.sdk.authentication;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;

import java.util.Date;

public final class ExplicitAuthenticationRequest {

    private static final String AUTH_URL = "/authentication/sign_in";

    private final OctaneHttpClient octaneHttpClient;
    private final String urlDomain;
    private final ExplicitAuthentication explicitAuthentication;
    private Date lastSuccessfulAuthTimestamp;

    public ExplicitAuthenticationRequest(final OctaneHttpClient octaneHttpClient,
                                         final String urlDomain,
                                         final ExplicitAuthentication explicitAuthentication) {
        this.octaneHttpClient = octaneHttpClient;
        this.urlDomain = urlDomain;
        this.explicitAuthentication = explicitAuthentication;
    }

    public boolean execute() {
        final OctaneHttpRequest.PostOctaneHttpRequest authenticateRequest = new OctaneHttpRequest.PostOctaneHttpRequest(
                urlDomain + AUTH_URL, OctaneHttpRequest.JSON_CONTENT_TYPE, explicitAuthentication.getAuthenticationString()
        );
        authenticateRequest.setSensitive(true);
        final boolean successStatusCode = octaneHttpClient.execute(authenticateRequest).isSuccessStatusCode();
        if (successStatusCode) {
            lastSuccessfulAuthTimestamp = new Date();
        }
        return successStatusCode;
    }

    public final Date getLastSuccessfulAuthTimestamp() {
        return lastSuccessfulAuthTimestamp;
    }
}

package com.hpe.adm.nga.sdk.authentication;

/**
 * Created by brucesp on 23/05/2016.
 */
public class SimpleClientAuthentication extends ClientAuthentication {

    private final String clientId;
    private final String clientSecret;

    public SimpleClientAuthentication(final String clientId, final String clientSecret, final String clientTypeHeader) {
        super(clientTypeHeader);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public SimpleClientAuthentication(final String clientId, final String clientSecret) {
        this(clientId, clientSecret, null);
    }

    protected String getClientId() {
        return clientId;
    }

    protected String getClientSecret() {
        return clientSecret;
    }
}

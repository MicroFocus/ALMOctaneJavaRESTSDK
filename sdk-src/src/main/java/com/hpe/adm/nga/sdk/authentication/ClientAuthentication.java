package com.hpe.adm.nga.sdk.authentication;

/**
 *
 * Created by brucesp on 19-Dec-16.
 */
abstract class ClientAuthentication implements Authentication {

    private static final String JSON_STRING = "{\"client_id\":\"%s\",\"client_secret\":\"%s\"}";

    private final String clientTypeHeader;

    ClientAuthentication(final String clientTypeHeader) {
        this.clientTypeHeader = clientTypeHeader;
    }

    @Override
    public final String getClientHeader() {
        return clientTypeHeader;
    }

    @Override
    public final String getAuthenticationString() {
        return String.format(JSON_STRING, getClientId(), getClientSecret());
    }

    abstract protected String getClientId();

    abstract protected String getClientSecret();
}

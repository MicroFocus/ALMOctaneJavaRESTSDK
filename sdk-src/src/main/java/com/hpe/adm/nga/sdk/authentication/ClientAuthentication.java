package com.hpe.adm.nga.sdk.authentication;

/**
 * Created by brucesp on 19-Dec-16.
 */
public abstract class ClientAuthentication implements Authentication {

    private static final String JSON_STRING = "{\"client_id\":\"%s\",\"client_secret\":\"%s\"}";

    @Override
    public final String getAuthenticationString() {
        return String.format(JSON_STRING, getClientId(), getClientSecret());
    }

    abstract protected String getClientId();

    abstract protected String getClientSecret();
}

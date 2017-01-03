package com.hpe.adm.nga.sdk.authentication;

/**
 * Created by brucesp on 19-Dec-16.
 */
abstract class UserAuthentication implements Authentication {

    private static final String JSON_STRING = "{\"user\":\"%s\",\"password\":\"%s\"}";

    private final String clientTypeHeader;

    UserAuthentication(final String clientTypeHeader) {
        this.clientTypeHeader = clientTypeHeader;
    }

    @Override
    public final String getClientHeader() {
        return clientTypeHeader;
    }

    @Override
    public final String getAuthenticationString() {
        return String.format(JSON_STRING, getUserName(), getPassword());
    }

    abstract protected String getUserName();
    abstract protected String getPassword();
}

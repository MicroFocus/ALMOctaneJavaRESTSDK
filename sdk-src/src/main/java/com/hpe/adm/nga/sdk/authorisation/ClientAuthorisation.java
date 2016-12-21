package com.hpe.adm.nga.sdk.authorisation;

/**
 * Created by brucesp on 19-Dec-16.
 */
public abstract class ClientAuthorisation implements Authorisation {

    private static final String JSON_STRING = "{\"client_id\":\"%s\",\"client_secret\":\"%s\"}";

    @Override
    public final String getAuthorisationString() {
        return String.format(JSON_STRING, getClientId(), getClientSecret());
    }

    abstract protected String getClientId();

    abstract protected String getClientSecret();
}

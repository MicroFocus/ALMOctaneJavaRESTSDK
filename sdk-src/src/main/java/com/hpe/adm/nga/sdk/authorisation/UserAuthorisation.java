package com.hpe.adm.nga.sdk.authorisation;

/**
 * Created by brucesp on 19-Dec-16.
 */
public abstract class UserAuthorisation implements Authorisation{

    private static final String JSON_STRING = "{\"user\":\"%s\",\"password\":\"%s\"}";

    @Override
    public final String getAuthorisationString() {
        return String.format(JSON_STRING, getUserName(), getPassword());
    }

    abstract protected String getUserName();
    abstract protected String getPassword();
}

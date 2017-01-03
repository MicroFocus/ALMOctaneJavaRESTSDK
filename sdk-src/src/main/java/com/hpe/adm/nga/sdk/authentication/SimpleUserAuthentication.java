package com.hpe.adm.nga.sdk.authentication;

/**
 * Created by brucesp on 23/05/2016.
 */
public class SimpleUserAuthentication extends UserAuthentication {

    private final String userName;
    private final String password;

    public SimpleUserAuthentication(final String userName, final String password, final String clientTypeHeader) {
        super(clientTypeHeader);
        this.userName = userName;
        this.password = password;
    }

    public SimpleUserAuthentication(final String userName, final String password) {
        this(userName, password, null);
    }

    protected String getUserName() {
        return userName;
    }

    protected String getPassword() {
        return password;
    }
}

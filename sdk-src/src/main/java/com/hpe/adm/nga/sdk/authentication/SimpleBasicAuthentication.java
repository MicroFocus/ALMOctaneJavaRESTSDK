package com.hpe.adm.nga.sdk.authentication;

import com.hpe.adm.nga.sdk.APIMode;

public class SimpleBasicAuthentication extends BasicAuthentication {
    private final String userName;
    private final String password;

    /**
     * @param userName The user
     * @param password The password
     * @param apiMode  API Mode - can be nullable
     */
    public SimpleBasicAuthentication(final String userName, final String password, final APIMode apiMode) {
        super(apiMode);
        this.userName = userName;
        this.password = password;
    }

    /**
     * @param userName The user
     * @param password The password
     */
    public SimpleBasicAuthentication(final String userName, final String password) {
        this(userName, password, null);
    }

    public String getAuthenticationId() {
        return userName;
    }

    public String getAuthenticationSecret() {
        return password;
    }
}

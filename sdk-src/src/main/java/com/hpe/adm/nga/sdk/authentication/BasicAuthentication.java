package com.hpe.adm.nga.sdk.authentication;

import com.hpe.adm.nga.sdk.APIMode;

public abstract class BasicAuthentication extends Authentication {

    BasicAuthentication(final APIMode apiMode) {
        super(apiMode, true);
    }

    /**
     * The id that is used for the authentication
     *
     * @return client id or username
     */
    abstract public String getAuthenticationId();

    /**
     * The secret that is used for the authentication
     *
     * @return client secret or password
     */
    abstract public String getAuthenticationSecret();
}

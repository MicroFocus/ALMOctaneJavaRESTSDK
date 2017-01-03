package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.authentication.Authentication;

/**
 * HTTP Client
 *
 * Created by leufl on 2/11/2016.
 */
public interface OctaneHttpClient {

    //Constants
    String OAUTH_AUTH_URL = "/authentication/sign_in";
    String OAUTH_SIGNOUT_URL = "/authentication/sign_out";
    String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
    String HPE_CLIENT_TYPE = "HPECLIENTTYPE";

    /**
     * @return - Returns true if the authentication succeeded, false otherwise.
     */
    boolean authenticate(Authentication authentication);

    /**
     * signs out and removes cookies
     */
    void signOut();

    OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest);
}

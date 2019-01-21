package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.authentication.Authentication;

/**
 *
 * HTTP Client
 *
 * Created by leufl on 2/11/2016.
 */
public interface OctaneHttpClient {

    //Constants
    String OAUTH_AUTH_URL = "/authentication/sign_in";
    String OAUTH_SIGNOUT_URL = "/authentication/sign_out";
    String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
    String OCTANE_USER_COOKIE_KEY = "OCTANE_USER";
    String HPE_CLIENT_TYPE = "HPECLIENTTYPE";

    /**
     * Authenticate with the Octane server using an implementation of the {@link Authentication} class
     * @param authentication implementation of {@link Authentication}
     * @return true if the authentication was successful, false otherwise
     */
    boolean authenticate(Authentication authentication);

    /**
     * Signs out and removes cookies
     */
    void signOut();

    OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest);
}

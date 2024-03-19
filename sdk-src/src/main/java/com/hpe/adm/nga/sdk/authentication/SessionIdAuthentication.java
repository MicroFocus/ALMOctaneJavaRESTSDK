package com.hpe.adm.nga.sdk.authentication;

import com.hpe.adm.nga.sdk.APIMode;

/**
 * Authentication using the LWSSO cookie directly
 */
public class SessionIdAuthentication extends Authentication {

    private final String sessionID;

    /**
     * The mode to use or null if none is needed
     *
     * @param sessionID             The session ID
     * @param apiMode               The mode
     */
    SessionIdAuthentication(String sessionID, APIMode apiMode) {
        super(apiMode, false, true);
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }
}

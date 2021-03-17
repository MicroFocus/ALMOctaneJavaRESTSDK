package com.hpe.adm.nga.sdk.authentication;

import com.hpe.adm.nga.sdk.APIMode;

import java.util.Optional;

public abstract class AuthenticationWithAPIMode {


    private final APIMode apiMode;

    /**
     * The mode to use or null if none is needed
     *
     * @param apiMode The mode
     */
    AuthenticationWithAPIMode(final APIMode apiMode) {
        this.apiMode = apiMode;
    }

    /**
     * Returns the API Mode header that is added to all calls to the REST API.  This usually refers to whether this should use
     * the technical preview or not (however other non-documented modes are possible) See the REST API documentation for
     * further information
     * This will only be used if it is non-empty
     *
     * @return The API mode header.  If none is sent then this should be null
     */
    public final Optional<APIMode> getAPIMode() {
        return Optional.ofNullable(apiMode);
    }
}

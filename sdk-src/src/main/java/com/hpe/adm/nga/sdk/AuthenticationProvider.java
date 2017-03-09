package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.authentication.Authentication;

/**
 * Interface for providing the Octane SDK with an {@link Authentication} object
 * This provider is called internally whenever the SDK needs to perform authentication with the Octane server.
 * The SDK will not keep the {@link Authentication} object in-memory for longer than needed (to perform the authentication)
 */
public interface AuthenticationProvider {
    Authentication getAuthentication();
}
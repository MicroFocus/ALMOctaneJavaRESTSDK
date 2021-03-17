package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.authentication.ExplicitAuthenticationRequest;
import com.hpe.adm.nga.sdk.exception.OctaneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AutomaticReAuthenticationRequestInterceptor implements OctaneRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AutomaticReAuthenticationRequestInterceptor.class.getName());
    private static final int HTTP_REQUEST_RETRY_COUNT = 1;

    private final ExplicitAuthenticationRequest explicitAuthenticationRequest;
    private final OctaneHttpClient octaneHttpClient;

    public AutomaticReAuthenticationRequestInterceptor(final OctaneHttpClient octaneHttpClient,
                                                       final ExplicitAuthenticationRequest explicitAuthenticationRequest) {
        this.octaneHttpClient = octaneHttpClient;
        this.explicitAuthenticationRequest = explicitAuthenticationRequest;
    }

    @Override
    public OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest) {
        for (int loop = 0; loop <= HTTP_REQUEST_RETRY_COUNT; loop++) {
            try {
                return octaneHttpClient.execute(octaneHttpRequest);
            } catch (OctaneAuthenticateTimeOutException exception) {
                if (loop == HTTP_REQUEST_RETRY_COUNT) {
                    logger.warn("Re-authentication did not work.  Failing!");
                    throw exception;
                }
                final Date currentTimestamp = new Date();

                // The same http client should not attempt re-auth from multiple threads
                synchronized (this) {
                    // If another thread already handled session timeout, skip the re-auth and just retry the request
                    if (explicitAuthenticationRequest.getLastSuccessfulAuthTimestamp().getTime() < currentTimestamp.getTime()) {
                        logger.debug("Auth token expired, trying to re-authenticate");
                        try {
                            if (!explicitAuthenticationRequest.execute()) {
                                logger.warn("Re-authentication failed");
                            }
                        } catch (OctaneException ex) {
                            logger.debug("Exception while retrying authentication: {}", ex.getMessage());
                        }
                    } else {
                        logger.debug("Auth token expired, but re-authentication was handled by another thread, will not re-authenticate");
                    }

                    logger.debug("Retrying request, retries left: {}", HTTP_REQUEST_RETRY_COUNT - loop);
                }
            }
        }

        throw new RuntimeException("Failed to execute request!");
    }
}
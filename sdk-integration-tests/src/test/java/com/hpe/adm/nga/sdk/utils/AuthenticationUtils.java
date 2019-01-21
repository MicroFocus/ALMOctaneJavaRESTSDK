package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;

/**
 * Created by brucesp on 06/06/2016.
 */
public class AuthenticationUtils {

    private static final String HPE_REST_API_TECH_PREVIEW = "HPE_REST_API_TECH_PREVIEW";

    public static Authentication getAuthentication() {

        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String authenticationType = configuration.getString("sdk.authenticationType");
        if (authenticationType == null || authenticationType.isEmpty() || authenticationType.equals("userpass")) {
            return new SimpleUserAuthentication(configuration.getString("sdk.username"), configuration.getString("sdk.password"), HPE_REST_API_TECH_PREVIEW);
        } else if (authenticationType.equals("client")) {
            return new SimpleClientAuthentication(configuration.getString("sdk.clientId"), configuration.getString("sdk.clientSecret"), HPE_REST_API_TECH_PREVIEW);
        } else {
            throw new IllegalArgumentException("Authentication not set!");
        }
    }
}

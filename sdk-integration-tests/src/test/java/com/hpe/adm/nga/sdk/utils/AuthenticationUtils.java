package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;

/**
 * Created by brucesp on 06/06/2016.
 */
public class AuthenticationUtils {

    public static Authentication getAuthentication() {

        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String authenticationType = configuration.getString("sdk.authenticationType");
        if (authenticationType == null || authenticationType.isEmpty() || authenticationType.equals("userpass")) {
            return new SimpleUserAuthentication(configuration.getString("sdk.username"), configuration.getString("sdk.password"));
        } else if (authenticationType.equals("client")) {
            return new SimpleClientAuthentication(configuration.getString("sdk.clientId"), configuration.getString("sdk.clientSecret"));
        } else {
            throw new IllegalArgumentException("Authentication not set!");
        }
    }
}

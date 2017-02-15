package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;

/**
 * Creates a simple context
 *
 * Created by brucesp on 03-Jan-17.
 */
public class CreateContextExample {

    public void createContext() {
        // two types of authentication
        // 1) API Ket
        Authentication clientAuthentication = new SimpleClientAuthentication("client_id", "client_secret");

        // 2) User/pass
        Authentication userPassAuthentication = new SimpleUserAuthentication("user", "password");

        // get instance of Octane Builder
        final Octane.Builder octaneBuilder = new Octane.Builder(clientAuthentication);

        // now we can add the server
        octaneBuilder.Server("http://server.com:3232");
        // the sharedspace
        octaneBuilder.sharedSpace(323213231);
        // the workspace
        octaneBuilder.workSpace(32313);

        // finally we build the context and get an Octane instance:

        Octane octane = octaneBuilder.build();
    }

}

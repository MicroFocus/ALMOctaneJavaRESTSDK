package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;

/**
 * Creates a simple context
 *
 * Created by brucesp on 03-Jan-17.
 */
public class CreateContextExample {

    public static void main (String [] args){
        new CreateContextExample().createContext();
    }

    public void createContext() {
        // two types of authentication
        // 1) API Key
        Authentication clientAuthentication = new SimpleClientAuthentication("clientId", "clientSecret");

        // 2) User/pass
        //Authentication userPassAuthentication = new SimpleUserAuthentication("user", "password");

        // get instance of Octane Builder
        final Octane.Builder octaneBuilder = new Octane.Builder(clientAuthentication);

        // now we can add the server
        octaneBuilder.Server("http://myd-vm10632.hpeswlab.net:8081");
        // the sharedspace
        octaneBuilder.sharedSpace(1001);
        // the workspace
        octaneBuilder.workSpace(1002);

        // finally we build the context and get an Octane instance:

        Octane octane = octaneBuilder.build();

       // octane.entityList("defects").get().limit(2).execute();
        octane.entityList("defects").get().execute();
    }

}

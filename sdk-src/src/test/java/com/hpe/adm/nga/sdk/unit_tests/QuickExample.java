package com.hpe.adm.nga.sdk.unit_tests;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authorisation.SimpleUserAuthorisation;

/**
 * Created by brucesp on 19-Dec-16.
 */
public class QuickExample {

    public static void main (String [] args) throws Exception {
        Octane octane = new Octane.Builder(new SimpleUserAuthorisation("sa@nga", "Welcome1")).build();
    }
}

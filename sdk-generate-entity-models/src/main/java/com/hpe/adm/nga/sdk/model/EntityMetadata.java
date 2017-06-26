package com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 26-Jun-17.
 */
public @interface EntityMetadata {
    enum AvailableMethods {GET, UPDATE, CREATE, DELETE}

    AvailableMethods[] availableMethods();
    String url();
}

package com.hpe.adm.nga.sdk.model;

/**
 * Some useful metadata for the generated class such as available HTTP methods and the
 * REST collection URL
 */
public @interface EntityMetadata {
    enum AvailableMethods {GET, UPDATE, CREATE, DELETE}

    /**
     * Available methods for the generated class
     * @return methods
     */
    AvailableMethods[] availableMethods();

    /**
     * The collection name
     * @return url
     */
    String url();
}

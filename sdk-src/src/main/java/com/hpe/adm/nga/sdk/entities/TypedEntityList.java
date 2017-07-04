package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * Created by brucesp on 27-Jun-17.
 */
public abstract class TypedEntityList {

    protected final OctaneHttpClient octaneHttpClient;
    protected final String baseDomain;

    public TypedEntityList(OctaneHttpClient octaneHttpClient, String baseDomain) {
        this.octaneHttpClient = octaneHttpClient;
        this.baseDomain = baseDomain;
    }
}

package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;

/**
 * Default implementation of the {@link OctaneClassFactory}, used by {@link OctaneClassFactory#getInstance()} when no system property is specified
 */
final class DefaultOctaneClassFactory implements OctaneClassFactory{

    private static final OctaneClassFactory instance = new DefaultOctaneClassFactory();

    private DefaultOctaneClassFactory(){}

    static OctaneClassFactory getInstance(){
        return instance;
    }

    @Override
    public OctaneHttpClient getOctaneHttpClient(String urlDomain) {
        return new GoogleHttpClient(urlDomain);
    }

    @Override
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new EntityList(octaneHttpClient, baseDomain +  entityName);
    }
}
package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;

import java.lang.reflect.InvocationTargetException;

/**
 * Default implementation of the {@link OctaneClassFactory}, used by {@link OctaneClassFactory#getSystemParamImplementation()} when no system property is specified
 */
final class DefaultOctaneClassFactory implements OctaneClassFactory{

    private static final OctaneClassFactory instance = new DefaultOctaneClassFactory();

    private DefaultOctaneClassFactory(){}

    static OctaneClassFactory getInstance(){
        return instance;
    }

    @Override
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new EntityList(octaneHttpClient, baseDomain +  entityName);
    }

    @Override
    public <T extends TypedEntityList> T getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, Class<T> entityListClass) {
        try {
            return entityListClass.getConstructor(OctaneHttpClient.class, String.class).newInstance(octaneHttpClient, baseDomain);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
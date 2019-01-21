package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.OctaneClassFactory;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.extension.entities.ExtendedEntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * Class factory for the extension
 */
public class ExtendedOctaneClassFactory implements OctaneClassFactory {

    private static ExtendedOctaneClassFactory instance = new ExtendedOctaneClassFactory();

    private ExtendedOctaneClassFactory(){}
    public static ExtendedOctaneClassFactory getInstance(){ return instance; }

    @Override
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new ExtendedEntityList(octaneHttpClient, baseDomain + entityName);
    }

    /**
     * This is not supported at the moment. Do not use
     * @param octaneHttpClient
     * @param baseDomain
     * @param enityListClass
     * @param <T>
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public <T extends TypedEntityList> T getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, Class<T> enityListClass) {
        throw new UnsupportedOperationException("Currently cannot get typed entities from the extension. Sorry :(");
    }
}
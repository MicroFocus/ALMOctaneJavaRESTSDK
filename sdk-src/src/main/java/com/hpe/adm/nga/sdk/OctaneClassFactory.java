package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * Created by brucesp on 15-May-17.
 */
public interface OctaneClassFactory {

    String OCTANE_HTTP_CLIENT_CLASS_NAME = "octaneHttpClientClassName";
    String OCTANE_CLASS_FACTORY_CLASS_NAME = "octaneClassFactoryClassName";

    OctaneHttpClient getOctaneHttpClient(String urlDomain);
    EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName);
}

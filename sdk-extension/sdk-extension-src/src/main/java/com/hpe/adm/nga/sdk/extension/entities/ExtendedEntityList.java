package com.hpe.adm.nga.sdk.extension.entities;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * Extension of the entity list, used to provide and {@link ExtendedGetEntities}
 */
public final class ExtendedEntityList extends EntityList {

    public ExtendedEntityList(OctaneHttpClient octaneHttpClient, String entityListDomain) {
        super(octaneHttpClient, entityListDomain);
    }

    /**
     * Overrides original get from {@link EntityList}
     * @return an extended version of the original {@link com.hpe.adm.nga.sdk.entities.get.GetEntities}
     */
    public ExtendedGetEntities get() {
        return new ExtendedGetEntities(octaneHttpClient, urlDomain);
    }

}
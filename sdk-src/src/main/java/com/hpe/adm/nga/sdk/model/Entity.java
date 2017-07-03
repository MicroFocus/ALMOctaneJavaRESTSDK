package com.hpe.adm.nga.sdk.model;

import javax.annotation.Nullable;

/**
 * Created by brucesp on 27-Jun-17.
 */
public interface Entity {

    /**
     * Returns the type of the entity.  All entities that are returned from the server have a type.
     * If a type has not been set this will return null
     * @return the type of the entity or null if not set
     */
    @Nullable
    String getType();

    /**
     * Returns the ID of the entity.  Most (if not all) entities have an ID.  However it is possible for the entity to not
     * have an ID even when returned from the server.
     * If the ID has not been set this will return null
     * @return the ID of the entity or null if not set
     */
    @Nullable
    String getId();
}

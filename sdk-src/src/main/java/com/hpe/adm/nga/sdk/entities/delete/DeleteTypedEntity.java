package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * The generic super class for the context of delete for typed entities.
 *
 * @param <T> The type of the entity model
 * @see DeleteEntity for the non typed version
 */
public abstract class DeleteTypedEntity<T extends TypedEntityModel> extends TypedEntityList.TypedEntityRequest<T> {
    private final OctaneRequest octaneRequest;

    protected DeleteTypedEntity(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain, final String entityId) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * Carries out the execution and returns the deleted entity
     *
     * @return The deleted entity

     */
    public final T execute()  {
        return getEntityInstance(DeleteHelper.getInstance().deleteEntityModel(octaneRequest));
    }
}

package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * The generic super class for the context of get for typed entities.
 *
 * @param <T> The type of the entity model
 * @param <E> The implementing subclass of this class
 * @see UpdateEntity for the non typed version
 */
public abstract class UpdateTypedEntity<T extends TypedEntityModel, E extends UpdateTypedEntity>
        extends TypedEntityList.TypedEntityRequest<T> {

    private T entityModel;
    private final OctaneRequest octaneRequest;

    protected UpdateTypedEntity(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain, final String entityId) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * Carries out the execution and returns the entity
     *
     * @return The entity

     */
    public final T execute() {
        return getEntityInstance(UpdateHelper.getInstance().updateEntityModel(entityModel.getWrappedEntityModel(), octaneRequest));
    }

    /**
     * set a new entity for updating
     *
     * @param entityModel The entity model to be updated
     * @return this
     */
    @SuppressWarnings("unchecked")
    public final E entity(final T entityModel) {
        this.entityModel = entityModel;
        return (E) this;
    }
}

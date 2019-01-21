package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * The generic super class for the context of get for typed entities.
 *
 * @param <T> The type of the entity model
 * @param <E> The implementing subclass of this class
 * @param <F> The type of {@link com.hpe.adm.nga.sdk.entities.TypedEntityList.AvailableFields}
 * @see GetEntity for the non typed version
 */
public abstract class GetTypedEntity<T extends TypedEntityModel, E extends GetTypedEntity, F extends TypedEntityList.AvailableFields>
        extends TypedEntityList.TypedEntityRequest<T> {

    private final OctaneRequest octaneRequest;

    protected GetTypedEntity(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain, final String entityId) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * Carries out the execution and returns the entity
     *
     * @return The entity

     */
    public final T execute()  {
        return getEntityInstance(GetHelper.getInstance().getEntityModel(octaneRequest));
    }

    /**
     * Set Fields Parameters
     *
     * @param fields An array or comma separated list of fields to be retrieved
     * @return this
     */
    @SuppressWarnings("unchecked")
    public final E addFields(final F... fields) {
        GetTypedHelper.addFields(octaneRequest, fields);
        return (E) this;
    }
}

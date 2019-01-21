package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.OctaneCollectionSupplier;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

import java.util.stream.Collectors;

/**
 * The generic super class for the context of delete for typed entities.
 *
 * @param <T> The type of the entity model
 * @param <E> The implementing subclass of this class
 * @see DeleteEntities for the non typed version
 */
public abstract class DeleteTypedEntities<T extends TypedEntityModel, E extends DeleteTypedEntities> extends TypedEntityList.TypedEntityRequest<T> {

    private final OctaneRequest octaneRequest;

    protected DeleteTypedEntities(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * Carries out the execution and returns a collection of deleted entities
     * @return The collection of typed entities

     */
    public final OctaneCollection<T> execute()  {
        final OctaneCollection<EntityModel> deletedEntities = DeleteHelper.getInstance().deleteEntityModels(octaneRequest);
        return deletedEntities
                .stream()
                .map(this::getEntityInstance)
                .collect(Collectors.toCollection(new OctaneCollectionSupplier<>(deletedEntities)));
    }

    /**
     * @param query The query to use
     * @return The object
     */
    @SuppressWarnings("unchecked")
    public final E query(Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return (E) this;
    }

}

package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by brucesp on 04-Jul-17.
 */
public abstract class UpdateTypedEntities <T extends TypedEntityModel, E extends UpdateTypedEntities>
        extends TypedEntityList.TypedEntityRequest<T>{

    private Collection<T> entityModels = null;
    private final OctaneRequest octaneRequest;

    protected UpdateTypedEntities(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    public final Collection<T> execute() throws RuntimeException {
        final List<EntityModel> convertedEntityModels = entityModels.stream().map(T::getWrappedEntityModel).collect(Collectors.toList());
        final Collection<EntityModel> updatedEntities = UpdateHelper.getInstance().updateEntityModels(convertedEntityModels, octaneRequest);
        return updatedEntities.stream().map(this::getEntityInstance).collect(Collectors.toList());
    }

    public final E entities(final Collection<T> entities) {
        entityModels = entities;
        return (E) this;
    }

    /**
     * @param query The query to use
     * @return The object
     */
    public final E query(final Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return (E) this;
    }

}

package com.hpe.adm.nga.sdk.entities.create;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by brucesp on 28-Jun-17.
 */
public abstract class CreateTypedEntities<T extends TypedEntityModel, E extends CreateTypedEntities> extends TypedEntityList.TypedEntityRequest<T> {
    private Collection<T> entityModels = null;
    private final OctaneRequest octaneRequest;

    protected CreateTypedEntities(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        super (typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    public final Collection<T> execute() throws RuntimeException {
        final List<EntityModel> convertedEntityModels = entityModels.stream().map(T::getWrappedEntityModel).collect(Collectors.toList());
        final Collection<EntityModel> createdEntities = CreateHelper.getInstance().createEntities(convertedEntityModels, octaneRequest);
        return createdEntities.stream().map(this::getEntityInstance).collect(Collectors.toList());
    }

    public final E entities(Collection<T> entities) {
        entityModels = entities;
        return (E) this;
    }
}

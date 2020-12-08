/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.entities.update;

import com.hpe.adm.nga.sdk.APIMode;
import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.OctaneCollectionSupplier;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The generic super class for the context of update for typed entities.
 *
 * @param <T> The type of the entity model
 * @param <E> The implementing subclass of this class
 * @see UpdateEntities for the non typed version
 */
public abstract class UpdateTypedEntities <T extends TypedEntityModel, E extends UpdateTypedEntities>
        extends TypedEntityList.TypedEntityRequest<T>{

    private Collection<T> entityModels = null;
    private final OctaneRequest octaneRequest;

    protected UpdateTypedEntities(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * Carries out the execution and returns the entities
     *
     * @return The entities

     */
    public final OctaneCollection<T> execute()  {
        final List<EntityModel> convertedEntityModels = entityModels.stream().map(T::getWrappedEntityModel).collect(Collectors.toList());
        final OctaneCollection<EntityModel> updatedEntities = UpdateHelper.getInstance().updateEntityModels(convertedEntityModels, octaneRequest);
        return updatedEntities
                .stream()
                .map(this::getEntityInstance)
                .collect(Collectors.toCollection(new OctaneCollectionSupplier<>(updatedEntities)));
    }

    /**
     * Carries out the execution and returns the entities, using a custom api mode
     *
     * @return The entities
     */
    public final OctaneCollection<T> execute(APIMode header)  {
        octaneRequest.addHeader(header);
        OctaneCollection<T> result = execute();
        octaneRequest.removeHeader(header);
        return result;
    }

    /**
     * Adds the entities to be sent to the server to be updated
     * @param entities A collection of entities
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public final E entities(final Collection<T> entities) {
        entityModels = entities;
        return (E) this;
    }

    /**
     * @param query The query to use
     * @return The object
     */
    @SuppressWarnings("unchecked")
    public final E query(final Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return (E) this;
    }

}

package com.hpe.adm.nga.sdk.entities.delete;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by brucesp on 04-Jul-17.
 */
public abstract class DeleteTypedEntities<T extends TypedEntityModel, E extends DeleteTypedEntities> extends TypedEntityList.TypedEntityRequest<T> {

    private final OctaneRequest octaneRequest;

    protected DeleteTypedEntities(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    public final Collection<T> execute() throws RuntimeException {
        final Collection<EntityModel> deletedEntities = DeleteHelper.getInstance().deleteEntityModels(octaneRequest);
        return deletedEntities.stream().map(this::getEntityInstance).collect(Collectors.toList());
    }

    /**
     * @param query The query to use
     * @return The object
     */
    public final E query(Query query) {
        octaneRequest.getOctaneUrl().setDqlQueryParam(query);
        return (E) this;
    }

}

package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.query.Query;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by brucesp on 26-Jun-17.
 */
public abstract class GetTypedEntities<T extends TypedEntityModel, E extends GetTypedEntities, F extends TypedEntityList.AvailableFields, G extends TypedEntityList.SortableFields>
        extends TypedEntityList.TypedEntityRequest<T> {

    private final OctaneRequest octaneRequest;

    protected GetTypedEntities(final Class<T> typedEntityModelClass, final OctaneHttpClient octaneHttpClient, final String baseDomain) {
        super(typedEntityModelClass);
        octaneRequest = new OctaneRequest(octaneHttpClient, baseDomain);
    }

    public final Collection<T> execute() throws RuntimeException {
        return GetHelper.getInstance().getEntityModels(octaneRequest).stream().map(this::getEntityInstance).collect(Collectors.toList());
    }

    public final E addFields(final F... fields) {
        GetTypedHelper.addFields(octaneRequest, fields);
        return (E) this;
    }

    /**
     * Add Limit parameter
     *
     * @param limit The entity limit
     * @return GetEntities Object with new limit parameter
     */
    public final E limit(final int limit) {
        octaneRequest.getOctaneUrl().setLimitParam(limit);
        return (E) this;
    }

    /**
     * Add offset parameter
     *
     * @param offset The entity limit offset
     * @return GetEntities Object with new offset parameter
     */
    public final E offset(final int offset) {
        octaneRequest.getOctaneUrl().setOffsetParam(offset);
        return (E) this;
    }

    /**
     * Add OrderBy parameters
     *
     * @param sortableField The string which determines how the entities should be ordered
     * @param asc           - true=ascending/false=descending
     * @return GetEntities Object with new OrderBy parameters
     */
    public final E addOrderBy(final G sortableField, boolean asc) {
        octaneRequest.getOctaneUrl().setOrderByParam(sortableField.getFieldName(), asc);
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

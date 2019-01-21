package com.hpe.adm.nga.sdk.model;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;

import java.util.function.Supplier;

/**
 * Implementation of the {@link Supplier} that returns a new {@link OctaneCollection}
 * @param <T> The type of entity that will be returned
 */
public final class OctaneCollectionSupplier<T extends Entity> implements Supplier<OctaneCollection<T>> {

    private final int totalCount;
    private final boolean exceedsTotalCount;

    /**
     * The original collection that will be converted.  It takes the parameters of the collection and transfers them
     * over
     * @param octaneCollection The original octane collection
     */
    public OctaneCollectionSupplier(final OctaneCollection<? extends Entity> octaneCollection){
        totalCount = octaneCollection.getTotalCount();
        exceedsTotalCount = octaneCollection.exceedsTotalCount();
    }

    @Override
    public OctaneCollection<T> get() {
        return new OctaneCollectionImpl<>(totalCount, exceedsTotalCount);
    }
}

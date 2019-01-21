package com.hpe.adm.nga.sdk.model;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;

import java.util.LinkedHashSet;

/**
 * Implementation of the {@link OctaneCollection}.  Implements a {@link java.util.Set}
 */
final class OctaneCollectionImpl<T extends Entity> extends LinkedHashSet<T> implements OctaneCollection<T> {

    private final int totalCount;
    private final boolean exceedsTotalCount;

    OctaneCollectionImpl(final int totalCount, final boolean exceedsTotalCount) {
        super();
        this.exceedsTotalCount = exceedsTotalCount;
        this.totalCount = totalCount;
    }

    OctaneCollectionImpl() {
        this(NO_TOTAL_COUNT_SET, false);
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public boolean exceedsTotalCount() {
        return exceedsTotalCount;
    }
}

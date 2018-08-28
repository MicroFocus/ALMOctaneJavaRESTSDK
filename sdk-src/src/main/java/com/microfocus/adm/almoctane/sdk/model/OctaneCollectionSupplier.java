/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microfocus.adm.almoctane.sdk.model;

import com.microfocus.adm.almoctane.sdk.entities.OctaneCollection;

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

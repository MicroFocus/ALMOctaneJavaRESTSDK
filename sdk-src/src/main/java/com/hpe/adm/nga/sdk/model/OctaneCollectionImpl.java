/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
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

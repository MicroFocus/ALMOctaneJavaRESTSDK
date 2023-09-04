/**
 * Copyright 2016-2023 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

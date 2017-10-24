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
package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.Entity;

import java.util.Collection;

/**
 * An extension of the {@link Collection} interface that incorporates important metadata about the collection that has
 * been returned.
 *
 *  These are the total_count and exceeds_total_count fields.  These are important when scrolling through paged data to
 *  know whether there is more data to fetch.  Exceeds total count signifies whether the requested data page size is larger
 *  than allowed by server constraints.
 *
 *  See the REST API documentation for more details
 */
public interface OctaneCollection<T extends Entity> extends Collection<T> {

    /**
     * Returned when the total count field has not been set
     */
    int NO_TOTAL_COUNT_SET = -1;

    /**
     * The total count of available entities that answer to the current query
     * @return total count or NO_TOTAL_COUNT_SET if not set
     */
    int getTotalCount();

    /**
     * Whether the requested number of entities exceeds the total available number of entities
     * @return exceeds total count (false if not set as well)
     */
    boolean exceedsTotalCount();

}

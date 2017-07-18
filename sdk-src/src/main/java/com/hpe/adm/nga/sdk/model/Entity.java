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
package com.hpe.adm.nga.sdk.model;

import javax.annotation.Nullable;

/**
 * An interface representing the basic Octane entity.
 */
public interface Entity {

    /**
     * Returns the type of the entity.  All entities that are returned from the server have a type.
     * If a type has not been set this will return null
     * @return the type of the entity or null if not set
     */
    @Nullable
    String getType();

    /**
     * Returns the ID of the entity.  Most (if not all) entities have an ID.  However it is possible for the entity to not
     * have an ID even when returned from the server.
     * If the ID has not been set this will return null
     * @return the ID of the entity or null if not set
     */
    @Nullable
    String getId();
}

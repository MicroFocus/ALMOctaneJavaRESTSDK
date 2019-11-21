/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * The abstract super class for getting entity lists for typed entities.  This does not inherit the {@link EntityList}
 * or share an interface due to the different ways that the context is created.  However the same functionality is available
 * in both versions
 * @see EntityList
 */
public abstract class TypedEntityList {

    /**
     * An interface that marks the fields in the entity that can be selected in queries and field selection
     */
    public interface AvailableFields {
        /**
         * The name of a field
         * @return Field name
         */
        String getFieldName();
    }

    /**
     * An interface that marks the fields in the entity that can be sorted
     */
    public interface SortableFields {
        /**
         * The name of a field
         * @return Field name
         */
        String getFieldName();
    }

    /**
     * An instance of a request for typed entities such as create or get
     * @param <T> The instance of the entity
     */
    public static abstract class TypedEntityRequest<T extends TypedEntityModel> {

        private final Class<T> typedEntityModelClass;

        protected TypedEntityRequest (final Class<T> typedEntityModelClass){
            this.typedEntityModelClass = typedEntityModelClass;
        }

        final protected T getEntityInstance(final EntityModel entityModel){
            try {
                return typedEntityModelClass.getConstructor(EntityModel.class).newInstance(entityModel);
            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot instantiate", e);
            }
        }
    }
}

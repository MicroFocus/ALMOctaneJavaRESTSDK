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

/**
 * The super type for the typed entity models.  These are models that are generated using the model generator.
 * It is not expected that a subclass will be created manually
 */
public abstract class TypedEntityModel implements Entity {

    /**
     * The typed entity model wraps a normal {@link EntityModel}
     */
    protected final EntityModel wrappedEntityModel;

    /**
     * Default Constructor using a new {@link EntityModel}
     */
    protected TypedEntityModel() {
        wrappedEntityModel = new EntityModel();
    }

    /**
     * Constructor with an existing {@link EntityModel}.  This is normally used when creating an instance of this class
     * from the server
     * @param wrappedEntityModel The model to wrap
     */
    protected TypedEntityModel(final EntityModel wrappedEntityModel) {
        this.wrappedEntityModel = wrappedEntityModel;
    }

    /**
     * Returns the wrapped {@link EntityModel}
     * @return The wrapped model
     */
    public final EntityModel getWrappedEntityModel() {
        return wrappedEntityModel;
    }

    /**
     * Returns the id of this entity
     * @return The entity id
     */
    @Override
    public final String getId() {
        FieldModel<?> id = wrappedEntityModel.getValue("id");
        return id == null ? null : id.getValue().toString();
    }

    /**
     * Returns the type of this entity
     * @return The entity type
     */
    @Override
    public final String getType() {
        FieldModel<?> type = wrappedEntityModel.getValue("type");
        return type == null ? null : type.getValue().toString();
    }
}

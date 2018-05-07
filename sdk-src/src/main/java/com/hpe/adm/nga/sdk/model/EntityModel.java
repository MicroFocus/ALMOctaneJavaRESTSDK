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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class hold the EntityModel objects and server as an entity data holder
 * entities.
 * The EntityModel class has two states - clean and dirty.  When creating a new entity from scratch then each addition
 * will be considered dirty and will be sent to the server when creating or updating.
 * When the entity has been returned from the server then the initial state will be considered clean.  Each change after
 * that (for example by using a method set*) will make the entity "dirty" and will be sent to the server when used for updates
 * Note - the id field is <em>always</em> considered a dirty field since it needs to be added
 */
public class EntityModel implements Entity{

    public static final String ID_FIELD_NAME = "id";

    /**
     * Represents the state of the entity.  In most cases it will be DIRTY.  However - when an entity is retrieved from the
     * server then the initial state will be CLEAN (all those fields will not be updated unless changed)
     */
    public enum EntityState {
        CLEAN, DIRTY
    }

    /**
     * Internal Map that keeps the state of fields to be updated
     */
    private final class DirtyHashMap extends HashMap<String, FieldModel> {

        /**
         * The fields that should be updated
         */
        private final Collection<String> dirtyFields = new HashSet<>();
        /**
         * The current state of the map
         */
        private EntityState entityState;

        /**
         * Initialise the hashmap with this initial state
         *
         * @param entityState The state to initialise the map
         */
        private DirtyHashMap(EntityState entityState) {
            super();
            this.entityState = entityState;
        }

        @Override
        public final FieldModel put(String key, FieldModel value) {
            if (entityState == EntityState.DIRTY) {
                dirtyFields.add(key);
            }
            return super.put(key, value);
        }

        @Override
        public FieldModel remove(Object key) {
            if (entityState == EntityState.DIRTY) {
                dirtyFields.add((String) key);
            }
            return super.remove(key);
        }

        @Override
        public void clear() {
            super.clear();
            dirtyFields.clear();
        }

        /**
         * Returns all values that are dirty
         *
         * @return Dirty values
         */
        private Collection<FieldModel> dirtyValues() {
            return
                    entrySet()
                            .stream()
                            .filter(entry -> entry.getKey().equals(ID_FIELD_NAME) || dirtyFields.contains(entry.getKey()))
                            .map(Entry::getValue)
                            .collect(Collectors.toSet());
        }
    }

    /**
     * The internal map of data that this entity represents
     */
    private final DirtyHashMap data;

    /**
     * Creates a new EntityModel object
     * All fields set after using this constructor will be considered "dirty"
     */
    public EntityModel() {
        this(null, EntityState.DIRTY);
    }

    /**
     * Creates a new EntityModel object with given field models
     * Use this when create entity model with mass of fields.
     * By using this constructor these fields will be considered to be the "dirty slate" of the entity.  In other words
     * these fields as well as those set afterwards will be considered in any updates
     *
     * @param values - a collection of field models
     */
    public EntityModel(Set<FieldModel> values) {
        this(values, EntityState.DIRTY);
    }

    /**
     * Creates a new EntityModel object with given field models
     * Use this when create entity model with mass of fields.
     *
     * @param values      - a collection of field models
     * @param entityState The initial state of the entity when constructing.  Once these fields have been initialised
     *                    the entity is considered to be dirty
     */
    public EntityModel(Set<FieldModel> values, EntityState entityState) {
        data = new DirtyHashMap(entityState);
        if (values != null) {
            values.forEach(field -> data.put(field.getName(), field));
        }
        data.entityState = EntityState.DIRTY;
    }

    /**
     * Creates a new EntityModel object with solo string field
     * The entity will be considered dirty and thus these fields will be updated
     *
     * @param value - a collection of field models
     * @param key   The key to the model
     */
    public EntityModel(String key, String value) {
        this();
        FieldModel fldModel = new StringFieldModel(key, value);
        data.put(key, fldModel);
    }

    /**
     * getter of entity value
     *
     * @return a collection of field models
     */
    public Set<FieldModel> getValues() {
        return new HashSet<>(data.values());
    }

    /**
     * Returns all dirty values.
     * Used when sending the entity to be updated
     *
     * @return a collection of field models
     */
    Collection<FieldModel> getDirtyValues() {
        return data.dirtyValues();
    }

    /**
     * getter of single field
     *
     * @param key the fieldName
     * @return the field of specified field name
     */
    public FieldModel getValue(String key) {
        return data.get(key);
    }

    /**
     * Remove a value from completely, different from setting the value to null
     *
     * @param key the fieldName
     */
    public void removeValue(String key) {
        data.remove(key);
    }

    /**
     * setter of new entity value, all old fields are cleared
     * @param values - a collection of field models
     * @return EntityModel with the field models set as a values
     */
    public EntityModel setValues(Set<FieldModel> values) {
        if (values != null) {
            data.clear();
            values.forEach(field -> data.put(field.getName(), field));
        }
        return this;
    }

    /**
     * setter of single field, update if field exists
     * @param fieldModel the single field to update
     * @return EntityModel with the field model set as a value
     */
    public EntityModel setValue(FieldModel fieldModel) {
        data.put(fieldModel.getName(), fieldModel);
        return this;
    }

    @Override
    public final String getType() {
        final StringFieldModel type = (StringFieldModel) getValue("type");
        return type == null ? null : type.getValue();
    }

    @Override
    public final String getId() {
        final StringFieldModel id = (StringFieldModel) getValue("id");
        return id == null ? null : id.getValue();
    }
}

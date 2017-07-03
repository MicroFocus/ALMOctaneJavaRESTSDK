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

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * This class hold the EntityModel objects and server as an entity data holder
 * entities.
 *
 */
public class EntityModel implements Entity{


    private Map<String, FieldModel> data = null;

    public EntityModel() {
        data = new HashMap<>();
    }

    /**
     * Creates a new EntityModel object with given field models
     * Use this when create entity model with mass of fields
     *
     * @param values - a collection of field models
     */
    public EntityModel(Set<FieldModel> values) {
        if (values != null) {
            data = new HashMap<>(values.size());
            values.forEach(field -> data.put(field.getName(), field));
        } else {
            data = new HashMap<>();
        }
    }

    /**
     * Creates a new EntityModel object with solo string field
     *
     * @param value - a collection of field models
     * @param key The key to the model
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
        return data.values().stream().collect(Collectors.toSet());
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
     * setter of new entity value, all old fields are cleared
     *
     * @param values - a collection of field models
     */
    public void setValues(Set<FieldModel> values) {
        if (values != null) {
            data.clear();
            values.forEach(field -> data.put(field.getName(), field));
        }
    }

    /**
     * setter of single field, update if field exists
     * @param fieldModel the single field to update
     */
    public void setValue(FieldModel fieldModel) {
        data.put(fieldModel.getName(), fieldModel);
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

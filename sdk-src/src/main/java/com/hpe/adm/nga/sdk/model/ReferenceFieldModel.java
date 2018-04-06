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

/**
 *
 * This class hold the ReferenceFieldModel objects and serve as a ReferenceField
 * type FieldModel data holder
 *
 *
 */
public class ReferenceFieldModel implements FieldModel<EntityModel> {

    // Private
    private EntityModel refValue;
    private String refName;

    /**
     * Creates a new ReferenceFieldModel object
     * 
     * @param name
     *            - Field name
     * @param value
     *            - Field Value
     */
    public ReferenceFieldModel(String name, EntityModel value) {

        setValue(name, value);
    }

    /**
     * GetEntities Value
     */
    @Override
    public EntityModel getValue() {
        return refValue;
    }

    /**
     * GetEntities name
     */
    @Override
    public String getName() {
        return refName;
    }

    /**
     * Set name/value
     */
    @Override
    public void setValue(String name, EntityModel value) {

        refValue = value;
        refName = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((refName == null) ? 0 : refName.hashCode());
        result = prime * result + ((refValue == null) ? 0 : refValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReferenceFieldModel other = (ReferenceFieldModel) obj;
        if (refName == null) {
            if (other.refName != null)
                return false;
        } else if (!refName.equals(other.refName))
            return false;
        if (refValue == null) {
            if (other.refValue != null)
                return false;
        } else if (!refValue.equals(other.refValue))
            return false;
        return true;
    }

}

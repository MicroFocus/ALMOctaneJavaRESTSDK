package com.hpe.adm.nga.sdk.model;

import java.util.Collection;

public class ArrayFieldModel extends MultiReferenceFieldModel {

    /**
     * Creates a new MultiReferenceFieldModel object
     *
     * @param newName - Field name
     * @param value   - Field Value
     */
    public ArrayFieldModel(String newName, Collection<EntityModel> value) {
        super(newName, value);
    }
}

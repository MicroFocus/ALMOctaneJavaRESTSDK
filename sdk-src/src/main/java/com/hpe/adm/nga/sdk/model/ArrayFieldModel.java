package com.hpe.adm.nga.sdk.model;

import org.json.JSONArray;

/**
 * This class hold the ArrayFieldModel objects. These are normally arbitrary JSON arrays of primitives.  In this case the JSON is
 * represented as a string
 */
public class ArrayFieldModel extends StringFieldModel implements FieldModel<String> {

    /**
     * Creates a new ArrayFieldModel object
     *
     * @param newName  - Field name
     * @param newValue - Field Value
     */
    public ArrayFieldModel(String newName, String newValue) {
        super(newName, newValue);
    }

    @Override
    public Object getJSONValue() {
        return new JSONArray(getValue());
    }
}

package com.hpe.adm.nga.sdk.model;

/**
 * This class hold the ObjectFieldModel objects. These are normally arbitrary JSON objects.  In this case the JSON is
 * represented as a string
 */
public class ObjectFieldModel extends StringFieldModel implements FieldModel<String> {

    /**
     * Creates a new StringFieldModel object
     *
     * @param newName  - Field name
     * @param newValue - Field Value
     */
    public ObjectFieldModel(String newName, String newValue) {
        super(newName, newValue);
    }
}

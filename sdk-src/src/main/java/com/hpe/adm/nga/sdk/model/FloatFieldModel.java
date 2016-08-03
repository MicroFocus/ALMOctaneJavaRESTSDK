package com.hpe.adm.nga.sdk.model;

/**
 * Created by ngthien on 8/3/2016.
 */
public class FloatFieldModel implements FieldModel<Float> {
    private Float value;
    private String name;

    public FloatFieldModel(String newName, Float newValue) {
        setValue(newName, newValue);
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(String newName, Float newValue) {
        name = newName;
        value = newValue;
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.hpe.adm.nga.sdk.model;

public class FactFieldModel implements FieldModel<Fact> {

    private String name = "";
    private Fact value = null;

    public FactFieldModel(String name, Fact value) {
        setValue(name, value);
    }

    @Override
    public Fact getValue() {
        return value;
    }

    @Override
    public void setValue(String name, Fact value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

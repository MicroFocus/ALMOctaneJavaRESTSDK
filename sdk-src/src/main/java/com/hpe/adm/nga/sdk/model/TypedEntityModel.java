package com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 27-Jun-17.
 */
public abstract class TypedEntityModel implements Entity {

    protected final EntityModel wrappedEntityModel;

    protected TypedEntityModel() {
        wrappedEntityModel = new EntityModel();
    }

    protected TypedEntityModel(final EntityModel wrappedEntityModel) {
        this.wrappedEntityModel = wrappedEntityModel;
    }

    public final EntityModel getWrappedEntityModel() {
        return wrappedEntityModel;
    }

    @Override
    public final String getType() {
        return ((StringFieldModel) wrappedEntityModel.getValue("type")).getValue();
    }
}

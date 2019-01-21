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
     * Returns the type of this entity
     * @return The entity type
     */
    @Override
    public final String getType() {
        return ((StringFieldModel) wrappedEntityModel.getValue("type")).getValue();
    }
}

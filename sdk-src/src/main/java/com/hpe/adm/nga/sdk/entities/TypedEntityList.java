package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * The abstract super class for getting entity lists for typed entities.  This does not inherit the {@link EntityList}
 * or share an interface due to the different ways that the context is created.  However the same functionality is available
 * in both versions
 * @see EntityList
 */
public abstract class TypedEntityList {

    protected final OctaneHttpClient octaneHttpClient;
    protected final String baseDomain;

    /**
     * An interface that marks the fields in the entity that can be selected in queries and field selection
     */
    public interface AvailableFields {
        /**
         * The name of a field
         * @return Field name
         */
        String getFieldName();
    }

    /**
     * An interface that marks the fields in the entity that can be sorted
     */
    public interface SortableFields {
        /**
         * The name of a field
         * @return Field name
         */
        String getFieldName();
    }

    /**
     * An instance of a request for typed entities such as create or get
     * @param <T> The instance of the entity
     */
    public static abstract class TypedEntityRequest<T extends TypedEntityModel> {

        private final Class<T> typedEntityModelClass;

        protected TypedEntityRequest (final Class<T> typedEntityModelClass){
            this.typedEntityModelClass = typedEntityModelClass;
        }

        final protected T getEntityInstance(final EntityModel entityModel){
            try {
                return typedEntityModelClass.getConstructor(EntityModel.class).newInstance(entityModel);
            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot instantiate", e);
            }
        }
    }

    /**
     * Creates a new object.  This represents an entity collection
     *
     * @param octaneHttpClient - Http Client
     * @param baseDomain - Domain Name
     */
    public TypedEntityList(OctaneHttpClient octaneHttpClient, String baseDomain) {
        this.octaneHttpClient = octaneHttpClient;
        this.baseDomain = baseDomain;
    }
}

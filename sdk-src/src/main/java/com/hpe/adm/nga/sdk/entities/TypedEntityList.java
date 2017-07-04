package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * Created by brucesp on 27-Jun-17.
 */
public abstract class TypedEntityList {

    protected final OctaneHttpClient octaneHttpClient;
    protected final String baseDomain;

    public interface AvailableFields {
        String getFieldName();
    }

    public interface SortableFields {
        String getFieldName();
    }

    /**
     * Created by brucesp on 04-Jul-17.
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

    public TypedEntityList(OctaneHttpClient octaneHttpClient, String baseDomain) {
        this.octaneHttpClient = octaneHttpClient;
        this.baseDomain = baseDomain;
    }
}

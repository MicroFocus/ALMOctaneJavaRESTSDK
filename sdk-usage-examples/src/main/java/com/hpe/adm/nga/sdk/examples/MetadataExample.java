package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.metadata.features.Feature;

import java.util.Collection;

/**
 * Demonstrates how to read metadata using the REST SDK
 * Created by brucesp on 11-Jan-17.
 */
@SuppressWarnings("ALL")
public class MetadataExample {

    /**
     * Used as a placeholder.  The assumption is that there is a valid instance of the Octane context
     */
    private final Octane octane = null;

    /**
     * The instance of metadata that will be used in these examples
     */
    private final Metadata metadata;

    public MetadataExample() {
        // init the metadata context
        metadata = octane.metadata();
    }

    /**
     * Gets all entity metadata.  This is the same as calling /metadata/entities
     */
    public void getAllEntityMetadata(){
        // can call execute immediately since there is nothing else to build
        final Collection<EntityMetadata> entityMetadata = metadata.entities().execute();

        // get the first entity
        final EntityMetadata next = entityMetadata.iterator().next();
        // get the name
        next.getName();

        // can modify label?
        next.canModifyLabel();

        // returns a list of features for this entity
        final Collection<Feature> features = next.features();
        // gets feature
        final Feature feature = features.iterator().next();

        // gets the feature name
        feature.getName();
    }
}

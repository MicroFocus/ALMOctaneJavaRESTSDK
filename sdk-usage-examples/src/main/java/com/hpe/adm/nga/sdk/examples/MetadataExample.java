/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

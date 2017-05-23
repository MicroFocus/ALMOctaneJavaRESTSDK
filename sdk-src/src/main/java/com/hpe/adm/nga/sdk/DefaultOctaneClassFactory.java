/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;

/**
 * Default implementation of the {@link OctaneClassFactory}, used by {@link OctaneClassFactory#getSystemParamImplementation()} when no system property is specified
 */
final class DefaultOctaneClassFactory implements OctaneClassFactory{

    private static final OctaneClassFactory instance = new DefaultOctaneClassFactory();

    private DefaultOctaneClassFactory(){}

    static OctaneClassFactory getInstance(){
        return instance;
    }

    @Override
    public OctaneHttpClient getOctaneHttpClient(String urlDomain) {
        return new GoogleHttpClient(urlDomain);
    }

    @Override
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new EntityList(octaneHttpClient, baseDomain +  entityName);
    }
}
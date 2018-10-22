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

package com.microfocus.adm.octane.sdk;

import com.microfocus.adm.octane.sdk.entities.EntityList;
import com.microfocus.adm.octane.sdk.entities.TypedEntityList;
import com.microfocus.adm.octane.sdk.network.OctaneHttpClient;
import com.microfocus.adm.octane.sdk.network.google.GoogleHttpClient;

import java.lang.reflect.InvocationTargetException;

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
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new EntityList(octaneHttpClient, baseDomain +  entityName);
    }

    @Override
    public <T extends TypedEntityList> T getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, Class<T> entityListClass) {
        try {
            return entityListClass.getConstructor(OctaneHttpClient.class, String.class).newInstance(octaneHttpClient, baseDomain);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
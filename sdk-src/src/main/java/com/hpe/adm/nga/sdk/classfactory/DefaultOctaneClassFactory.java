/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.classfactory;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

import java.lang.reflect.InvocationTargetException;

/**
 * Default implementation of the {@link OctaneClassFactory}, used by {@link OctaneClassFactory#getImplementation(String)}
 * when no string or system property is specified
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

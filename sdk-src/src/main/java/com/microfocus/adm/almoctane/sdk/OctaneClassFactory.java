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

package com.microfocus.adm.almoctane.sdk;

import com.microfocus.adm.almoctane.sdk.entities.EntityList;
import com.microfocus.adm.almoctane.sdk.entities.TypedEntityList;
import com.microfocus.adm.almoctane.sdk.network.OctaneHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Factory for the {@link Octane} class, can implemented by clients to modify the behaviour of the sdk
 * Implementations of this class are required to be singleton
 * If you specify a the system param for {@link #getSystemParamImplementation()},
 * this method is called to instantiate the class
 */
public interface OctaneClassFactory {

    /**
     * Sys param name used by {@link #getSystemParamImplementation()}
     */
    String OCTANE_CLASS_FACTORY_CLASS_NAME = "octaneClassFactoryClassName";

    /**
     * Create an instance of the OctaneHttpClient for the {@link Octane} to use
     * @param urlDomain for the {@link OctaneHttpClient} constructor
     * @return OctaneHttpClient implementation with urlDomain set
     */
    OctaneHttpClient getOctaneHttpClient(String urlDomain);

    /**
     * Get the implementation of the {@link EntityList} used by the {@link Octane} object
     * @param octaneHttpClient used to create the
     * @param baseDomain for the entity list
     * @param entityName API entity name to be used in the url
     * @return EntityList for the specific entity, implementation uses provided @param octaneHttpClient
     */
    EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName);

    <T extends TypedEntityList>T getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, Class<T> entityListClass);

    /**
     * Get the implementation implementation of OctaneClassFactory, can be modified by changing the OCTANE_CLASS_FACTORY_CLASS_NAME system param
     * @return OctaneClassFactory implementation based on the OCTANE_CLASS_FACTORY_CLASS_NAME sys param, if the param is missing, returns {@link DefaultOctaneClassFactory}
     */
    static OctaneClassFactory getSystemParamImplementation() {
        Logger logger = LoggerFactory.getLogger(Octane.class.getName());
        String octaneClassFactoryClassName = System.getProperty(OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME);

        if (octaneClassFactoryClassName != null) {
            logger.debug("Creating OctaneClassFactory using implementation {}", octaneClassFactoryClassName);
            //Use reflection to instantiate the class
            Class<OctaneClassFactory> clazz;
            try {
                //noinspection unchecked
                clazz = (Class<OctaneClassFactory>) Class.forName(octaneClassFactoryClassName);
            } catch (ClassNotFoundException e) {
                logger.error("Failed to instantiate OctaneClassFactory class from name: " + octaneClassFactoryClassName + ": " + e.getMessage());
                throw new RuntimeException("Failed to find class with name: " + octaneClassFactoryClassName, e);
            }

            try {
                //Call the get instance method of the class
                Method method = clazz.getDeclaredMethod("getInstance");
                return (OctaneClassFactory) method.invoke(null);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                String message = "Failed to instantiate class "
                        + octaneClassFactoryClassName
                        + ", the class must be a singleton and have a static getInstance() method";
                logger.error(message);
                throw new RuntimeException(message, e);
            }
        } else {
            return DefaultOctaneClassFactory.getInstance();
        }
    }

}

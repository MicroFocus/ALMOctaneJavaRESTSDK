/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
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

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Factory for the {@link Octane} class, can implemented by clients to modify the behaviour of the sdk
 * Implementations of this class are required to be singleton
 * If you specify a the system param for {@link #getImplementation(String)},
 * this method is called to instantiate the class
 */
public interface OctaneClassFactory {

    /**
     * Sys param name used by {@link #getImplementation(String)}
     */
    String OCTANE_CLASS_FACTORY_CLASS_NAME = "octaneClassFactoryClassName";

    /**
     * Default octane class factory name.  Useful in some edge cases where the system parameter needs to be overriden
     */
    String DEFAULT_OCTANE_CLASS_FACTORY_CLASS_NAME = DefaultOctaneClassFactory.class.getName();

    /**
     * Get the implementation of the {@link EntityList} used by the {@link Octane} object
     *
     * @param octaneHttpClient used to create the
     * @param baseDomain       for the entity list
     * @param entityName       API entity name to be used in the url
     * @return EntityList for the specific entity, implementation uses provided @param octaneHttpClient
     */
    EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName);

    <T extends TypedEntityList> T getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, Class<T> entityListClass);

    /**
     * Get the implementation implementation of OctaneClassFactory, can be modified by
     * sending the class factory name or setting the OCTANE_CLASS_FACTORY_CLASS_NAME system param.
     * The priority is the String that is given here.  If it is null then the system parameter is checked.
     *
     * @param octaneClassFactoryClassName the String that can determine the class factory to use.  This can be NULL
     * @return OctaneClassFactory implementation based on the given classfactory String or
     * OCTANE_CLASS_FACTORY_CLASS_NAME sys param, if the param is missing, returns {@link DefaultOctaneClassFactory}
     */
    static OctaneClassFactory getImplementation(String octaneClassFactoryClassName) {
        Logger logger = LoggerFactory.getLogger(Octane.class.getName());

        octaneClassFactoryClassName =
                octaneClassFactoryClassName != null ? octaneClassFactoryClassName :
                        System.getProperty(OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME);

        if (octaneClassFactoryClassName != null) {
            logger.debug("Creating OctaneClassFactory using implementation {}", octaneClassFactoryClassName);
            //Use reflection to instantiate the class
            Class<OctaneClassFactory> clazz;
            try {
                //noinspection unchecked
                clazz = (Class<OctaneClassFactory>) Class.forName(octaneClassFactoryClassName);
            } catch (ClassNotFoundException e) {
                logger.error("Failed to instantiate OctaneClassFactory class from name: {}: {}", octaneClassFactoryClassName, e.getMessage());
                throw new RuntimeException("Failed to find class with name: " + octaneClassFactoryClassName, e);
            }

            try {
                //Call the get instance method of the class
                Method method = clazz.getDeclaredMethod("getInstance");
                return (OctaneClassFactory) method.invoke(null);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                String message = "Failed to instantiate class {}, the class must be a singleton and have a static getInstance() method";
                logger.error(message, octaneClassFactoryClassName);
                throw new RuntimeException(message, e);
            }
        } else {
            return DefaultOctaneClassFactory.getInstance();
        }
    }

}

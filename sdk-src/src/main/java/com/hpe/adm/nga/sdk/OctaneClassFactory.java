package com.hpe.adm.nga.sdk;

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
 * If you specify a the system param for {@link #getSystemParamImplementation()},
 * this method is called to instantiate the class
 */
public interface OctaneClassFactory {

    /**
     * Sys param name used by {@link #getSystemParamImplementation()}
     */
    String OCTANE_CLASS_FACTORY_CLASS_NAME = "octaneClassFactoryClassName";

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

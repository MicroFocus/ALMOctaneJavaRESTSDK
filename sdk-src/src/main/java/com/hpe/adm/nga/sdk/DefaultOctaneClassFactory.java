package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by brucesp on 15-May-17.
 */
final class DefaultOctaneClassFactory implements OctaneClassFactory{

    private final Logger logger = LogManager.getLogger(DefaultOctaneClassFactory.class.getName());
    private static final OctaneClassFactory instance = new DefaultOctaneClassFactory();

    private DefaultOctaneClassFactory(){}

    static OctaneClassFactory getInstance(){
        return instance;
    }

    /**
     * The implementation used for the {@link OctaneHttpClient} can be configured using the
     * "{@value #OCTANE_HTTP_CLIENT_CLASS_NAME}" system property. <br>
     * The value of this property has to be the fully qualified name of a class which implements {@link OctaneHttpClient}. <br>
     * The implementation must have a single argument constructor with a String parameter.
     * The urlDomain value given to the builder is passed to the {@link OctaneHttpClient} through this constructor.
     * @return
     */
    @Override
    public OctaneHttpClient getOctaneHttpClient(String urlDomain) {
        String octaneHttpClientClassName = System.getProperty(OCTANE_HTTP_CLIENT_CLASS_NAME);

        if (octaneHttpClientClassName != null) {
            logger.info("Creating OctaneHttpClient using implementation {}", octaneHttpClientClassName);
            //Use reflection to instantiate the class
            Class<OctaneHttpClient> clazz;
            try {
                clazz = (Class<OctaneHttpClient>) Class.forName(octaneHttpClientClassName);
            } catch (ClassNotFoundException e) {
                logger.error(e);
                throw new RuntimeException("Failed to find class with name: " + octaneHttpClientClassName, e);
            }

            Constructor<?> ctor;
            try {
                ctor = clazz.getConstructor(String.class);
            } catch (NoSuchMethodException e) {
                logger.error(e);
                throw new RuntimeException(
                        "Failed to instantiate class " + octaneHttpClientClassName + "," +
                                " does not have a single string argument constructor", e);
            }

            try {
                return (OctaneHttpClient) ctor.newInstance(urlDomain);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                logger.error(e);
                throw new RuntimeException("Failed to instantiate class " + octaneHttpClientClassName, e);
            }
        } else {
            return new GoogleHttpClient(urlDomain);
        }
    }

    @Override
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new EntityList(octaneHttpClient, baseDomain +  entityName);
    }
}

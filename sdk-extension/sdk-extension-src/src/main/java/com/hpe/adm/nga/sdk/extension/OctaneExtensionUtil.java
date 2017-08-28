package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.OctaneClassFactory;

/**
 * Util class, will toggle extension,
 * all {@link com.hpe.adm.nga.sdk.Octane} classes build after {@link #enable()} will be build
 * using the {@link ExtendedOctaneClassFactory}. <br>
 * These methods are not thread-safe!
 */
public class OctaneExtensionUtil {

    public static boolean isEnabled(){
        String propValue = System
                .getProperties()
                .getProperty(OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME);

        return ExtendedOctaneClassFactory.class.getCanonicalName().equals(propValue);
    }

    public static void enable(){
        System.getProperties().setProperty(
                OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME,
                ExtendedOctaneClassFactory.class.getCanonicalName());
    }

    public static void disable(){
        System.getProperties().remove(OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME);
    }

}
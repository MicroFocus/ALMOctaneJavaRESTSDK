/*
 * © Copyright 2016-2021 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.classfactory.OctaneClassFactory;

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

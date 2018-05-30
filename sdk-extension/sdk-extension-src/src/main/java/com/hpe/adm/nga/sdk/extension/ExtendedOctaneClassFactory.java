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

package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.OctaneClassFactory;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.extension.entities.ExtendedEntityList;
import com.hpe.adm.nga.sdk.extension.network.google.InterceptorGoogleHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

import java.net.Proxy;

/**
 * Class factory for the extension
 */
public class ExtendedOctaneClassFactory implements OctaneClassFactory {

    private static Proxy httpProxy;
    private static ExtendedOctaneClassFactory instance = new ExtendedOctaneClassFactory();

    private ExtendedOctaneClassFactory(){}
    public static ExtendedOctaneClassFactory getInstance(){ return instance; }

    /**
     * Set the http proxy for all {@link OctaneHttpClient} created by this factory.
     * This does not affect exiting http clients in exiting instances of {@link com.hpe.adm.nga.sdk.Octane}
     * @param httpProxy {@link Proxy} to use when connecting to octane
     */
    public static void setHttpClientProxy(Proxy httpProxy){
        ExtendedOctaneClassFactory.httpProxy = httpProxy;
    }

    @Override
    public OctaneHttpClient getOctaneHttpClient(String urlDomain) {
        InterceptorGoogleHttpClient httpClient = new InterceptorGoogleHttpClient(urlDomain);
        if(httpProxy != null){
            httpClient.setHttpProxy(httpProxy);
        }
        return httpClient;
    }

    @Override
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new ExtendedEntityList(octaneHttpClient, baseDomain + entityName);
    }

    /**
     * This is not supported at the moment. Do not use
     * @param octaneHttpClient
     * @param baseDomain
     * @param enityListClass
     * @param <T>
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public <T extends TypedEntityList> T getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, Class<T> enityListClass) {
        throw new UnsupportedOperationException("Currently cannot get typed entities from the extension. Sorry :(");
    }
}
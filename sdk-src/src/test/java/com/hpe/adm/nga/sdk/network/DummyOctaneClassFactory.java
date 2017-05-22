package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.OctaneClassFactory;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.entities.EntityList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Dummy factory for {@link TestCustomOctaneHttpClient} tests
 */
public class DummyOctaneClassFactory implements OctaneClassFactory {

    //Singleton
    private static OctaneClassFactory instance = new DummyOctaneClassFactory();

    private DummyOctaneClassFactory() {
    }

    public static OctaneClassFactory getInstance() {
        return instance;
    }

    /**
     * Dummy http client that always returns an empty set of entities
     */
    public class DummyOctaneHttpClient implements OctaneHttpClient {

        private String stringConstructorArg;

        public DummyOctaneHttpClient(String stringConstructorArg) {
            this.stringConstructorArg = stringConstructorArg;
        }

        @Override
        public boolean authenticate(Authentication authentication) {
            return true;
        }

        @Override
        public void signOut() {
        }

        @Override
        public OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest) {
            //Return empty response
            String returnJson = "{\"total_count\":0,\"data\":[],\"exceeds_total_count\":false}";
            InputStream stream = new ByteArrayInputStream(returnJson.getBytes(StandardCharsets.UTF_8));
            return new OctaneHttpResponse(202, returnJson, stream);
        }

    }

    @Override
    public OctaneHttpClient getOctaneHttpClient(String urlDomain) {
        return new DummyOctaneHttpClient(urlDomain);
    }

    @Override
    public EntityList getEntityList(OctaneHttpClient octaneHttpClient, String baseDomain, String entityName) {
        return new EntityList(octaneHttpClient, baseDomain + entityName);
    }

}
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.OctaneClassFactory;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.UUID;

import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Test using a custom implementation of {@link OctaneHttpClient}
 * by setting the "{@value OctaneClassFactory#OCTANE_CLASS_FACTORY_CLASS_NAME}" system property.
 */
public class TestCustomOctaneHttpClient {

    //Make the sdk use a custom implementation of the octane http client
    private static final String FACTORY_CLASS = "com.hpe.adm.nga.sdk.network.DummyOctaneClassFactory";

    @Before
    public void setSysProp(){
        System.setProperty(
                OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME,
                FACTORY_CLASS);
    }

    @Test
    public void testCustomOctaneHttpClient() {

        String randomString = UUID.randomUUID().toString();
        Authentication authentication = new SimpleUserAuthentication("", "");

        Octane octane = spy(
                new Octane.Builder(authentication)
                        .Server(randomString)
                        .build()
        );

        //GetEntities the inner octane http client from the octane object
        OctaneHttpClient octaneHttpClient = Whitebox.getInternalState(octane, "octaneHttpClient");

        // Check if the random string passed to the builder
        // matches the field passed to the DummyOctaneHttpClient through its constructor
        String constructorArg = Whitebox.getInternalState(octaneHttpClient, "stringConstructorArg");
        Assert.assertEquals(constructorArg, randomString);

        // Check if the inner http client matches the implementation class set in the system properties
        Assert.assertEquals(octaneHttpClient.getClass().getCanonicalName(),
                DummyOctaneClassFactory.DummyOctaneHttpClient.class.getCanonicalName());

        // Check if the result matches the dummy result provided by the DummyOctaneHttpClient
        Assert.assertEquals(
                octane.entityList("doesn't_matter").get().execute().size(),
                0);
    }

    @After
    public void unsetSysProp(){
        //Unset the system property
        System.getProperties().remove(OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME);
    }
}

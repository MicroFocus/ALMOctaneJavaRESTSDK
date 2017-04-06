package com.hpe.adm.nga.sdk.unit_tests.customhttpclient;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.UUID;

import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Test using a custom implementation of {@link OctaneHttpClient}
 * by setting the "{@value Octane.Builder#OCTANE_HTTP_CLIENT_CLASS_NAME}" system propery.
 */
public class TestCustomOctaneHttpClient {

    @Test
    public void testCustomOctaneHttpClient() {
        //Make the sdk use a custom implementation of the octane http client
        String octaneHttpClientClassName = "com.hpe.adm.nga.sdk.unit_tests.customhttpclient.DummyOctaneHttpClient";

        System.getProperties().setProperty(
                Octane.Builder.OCTANE_HTTP_CLIENT_CLASS_NAME,
                octaneHttpClientClassName);


        String randomString = UUID.randomUUID().toString();
        Authentication authentication = new SimpleUserAuthentication("", "");

        Octane octane = spy(
                new Octane.Builder(authentication)
                        .Server(randomString)
                        .build()
        );

        //Get the inner octane http client from the octane object
        OctaneHttpClient octaneHttpClient = Whitebox.getInternalState(octane, "octaneHttpClient");

        // Check if the random string passed to the builder
        // matches the field passed to the DummyOctaneHttpClient through its constructor
        String constructorArg = Whitebox.getInternalState(octaneHttpClient, "stringConstructorArg");
        Assert.assertEquals(constructorArg, randomString);

        // Check if the inner http client matches the implementation class set in the system properties
        Assert.assertEquals(octaneHttpClient.getClass().getCanonicalName(), octaneHttpClientClassName);

        // Check if the result matches the dummy result provided by the DummyOctaneHttpClient
        Assert.assertEquals(
                octane.entityList("doesn't_matter").get().execute().size(),
                0);

        //Unset the system property
        System.getProperties().remove(Octane.Builder.OCTANE_HTTP_CLIENT_CLASS_NAME);
    }

}

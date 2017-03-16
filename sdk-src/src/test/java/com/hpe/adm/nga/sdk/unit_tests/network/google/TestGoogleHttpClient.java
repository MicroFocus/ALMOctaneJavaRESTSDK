package com.hpe.adm.nga.sdk.unit_tests.network.google;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(GoogleHttpClient.class)
public class TestGoogleHttpClient {

    /**
     * Exception thrown every time any request is executed
     */
    private static HttpResponseException forbiddenException
            = new HttpResponseException.Builder(401, "Unauthorized", new HttpHeaders()).build();

    @Test
    public void testRequestRetry() throws Exception {

        GoogleHttpClient googleHttpClientSpy = spy(new GoogleHttpClient(""));

        doReturn(null).when(googleHttpClientSpy, "convertOctaneRequestToGoogleHttpRequest", any(OctaneHttpRequest.class));

        doThrow(forbiddenException)
                .when(googleHttpClientSpy, "executeRequest", Matchers.any(HttpRequest.class));

        OctaneHttpRequest request = new OctaneHttpRequest.GetOctaneHttpRequest("");

        try {
            googleHttpClientSpy.execute(request);
        } catch (Exception ex) {
            //this is supposed to fail, eventually
        }

        /**
         * Check if the method retried the right amount of times
         */
        verifyPrivate(
                googleHttpClientSpy,
                times(GoogleHttpClient.getHttpRequestRetryCount() + 1))
                .invoke("execute", any(), anyInt());





    }



}

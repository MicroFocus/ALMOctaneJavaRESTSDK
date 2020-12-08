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
package com.hpe.adm.nga.sdk.network.google;

import com.google.api.client.http.HttpRequest;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.model.Cookie;
import org.mockserver.model.Delay;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.powermock.api.mockito.PowerMockito.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(GoogleHttpClient.class)
public class TestGoogleHttpClient {

    @Test
    public void testRequestRetry() throws Exception {

        GoogleHttpClient googleHttpClientSpy = spy(new GoogleHttpClient("http://url.com"));

        doReturn(null).when(googleHttpClientSpy, "convertOctaneRequestToGoogleHttpRequest", any(OctaneHttpRequest.class));
        doReturn(true).when(googleHttpClientSpy, "authenticate", any(Authentication.class));
        Whitebox.setInternalState(googleHttpClientSpy, "lastUsedAuthentication", PowerMockito.mock(Authentication.class));
        Whitebox.setInternalState(googleHttpClientSpy, "lastSuccessfulAuthTimestamp", new Date(0));

        //Create timeout exception, the same way octane does
        ErrorModel errorModel = new ErrorModel(new HashSet<>());
        errorModel.setValue(new StringFieldModel("errorCode", "VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT"));
        errorModel.setValue(new LongFieldModel(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME, 401L));
        OctaneException octaneException = new OctaneException(errorModel);

        doThrow(octaneException)
                .when(googleHttpClientSpy, "executeRequest", Matchers.any(HttpRequest.class));

        OctaneHttpRequest request = new OctaneHttpRequest.GetOctaneHttpRequest("http://url.com");

        try {
            googleHttpClientSpy.execute(request);
        } catch (Exception ex) {
            //this is supposed to fail, eventually
        }

        /*
          Check if the method retried the right amount of times
         */
        verifyPrivate(
                googleHttpClientSpy,
                times(GoogleHttpClient.getHttpRequestRetryCount() + 1))
                .invoke("execute", any(), anyInt());
    }

    @Test
    public void testCustomSettings() {
        int connTimeout = 2345;

        Octane.OctaneCustomSettings settings = new Octane.OctaneCustomSettings() {{
                set(Setting.READ_TIMEOUT,55000);
                set(Setting.CONNECTION_TIMEOUT,2345);
        }};

        GoogleHttpClient client = new GoogleHttpClient("http://google.com:8090", settings );
        OctaneHttpRequest request = new OctaneHttpRequest.GetOctaneHttpRequest("http://google.com:9090");

        long start = System.currentTimeMillis();
        try {
            client.execute(request);
        } catch (Exception e) {
            long end = System.currentTimeMillis();

            Assert.assertTrue(e.getCause() instanceof SocketTimeoutException);
            long duration = end - start;
            Assert.assertTrue(duration < 3000 && duration > 2000);
        }

    }

    @Test
    @Ignore
    public void testParallelRequestRetry() {
        ConfigurationProperties.logLevel("WARN");

        ClientAndServer clientAndServer = startClientAndServer();
        Octane octane;
        long totalExecutionTime = 500;
        long cookieExpirationTime = 250;

        initServerResponse(clientAndServer, cookieExpirationTime);
        Authentication authentication = new SimpleUserAuthentication("", "");
        String url = "http://localhost:" + clientAndServer.getLocalPort();
        GoogleHttpClient spyGoogleHttpClient = spy(new GoogleHttpClient(url));
        octane = new Octane.Builder(authentication, spyGoogleHttpClient).Server(url).workSpace(1002).sharedSpace(1001).build();

        int nrCores = Runtime.getRuntime().availableProcessors();
        IntStream.rangeClosed(1, nrCores).parallel().forEach((nr) -> runGetRequests(octane, totalExecutionTime));

        clientAndServer.stop();

        //Exactly 2 authentications are done: one for 1st log in and 2nd for cookie refresh
        Mockito.verify(spyGoogleHttpClient, times(2)).authenticate(any());

        //There are more execution invocations than there are threads
        Mockito.verify(spyGoogleHttpClient, atLeast(nrCores + 1)).execute(any());
    }

    public void runGetRequests(Octane octane, long totalExecutionTime) {
        LocalDateTime pastTime = LocalDateTime.now();
        LocalDateTime currentTime = LocalDateTime.now();
        while (Duration.between(pastTime, currentTime).toMillis() < totalExecutionTime) {
            octane.entityList("").get().execute();
            currentTime = LocalDateTime.now();
        }
    }

    public void initServerResponse(ClientAndServer clientAndServer, long cookieExpirationTime) {
        Cookie firstCookie = new Cookie("LWSSO_COOKIE_KEY", "one");
        Cookie secondCookie = new Cookie("LWSSO_COOKIE_KEY", "two");
        Delay serverDelay = Delay.milliseconds(10);

        //First cookie given only once to the main octane object
        clientAndServer
                .when(
                        request().withPath("/authentication/sign_in"),
                        Times.once())
                .respond(
                        response().withStatusCode(200).withCookie(firstCookie).withDelay(serverDelay));

        //Second cookie given every time a new authentication request is made
        clientAndServer
                .when(
                        request().withPath("/authentication/sign_in"))
                .respond(
                        response().withStatusCode(200).withCookie(secondCookie).withDelay(serverDelay));

        //Accept request with second cookie
        clientAndServer
                .when(
                        request().withCookie(secondCookie))
                .respond(
                        response().withStatusCode(200).withDelay(serverDelay));

        //Requests with the first cookie are accepted cookieExpirationTime milliseconds
        clientAndServer
                .when(
                        request().withMethod("GET").withCookie(firstCookie),
                        Times.unlimited(),
                        TimeToLive.exactly(TimeUnit.MILLISECONDS, cookieExpirationTime))
                .respond(
                        response().withStatusCode(200).withDelay(serverDelay));

        //Requests with first cookies (after cookieExpirationTime milliseconds) get IDLE TIME OUT
        clientAndServer
                .when(
                        request().withMethod("GET").withCookie(firstCookie),
                        Times.unlimited())
                .respond(
                        response().withStatusCode(401).withDelay(serverDelay)
                                .withBody("{\"errorCode\":\"VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT\"," +
                                        "\"accumulatedMessage\":\"Previous validation errors " +
                                        "(non mandatory validators): \"}"));
    }
}
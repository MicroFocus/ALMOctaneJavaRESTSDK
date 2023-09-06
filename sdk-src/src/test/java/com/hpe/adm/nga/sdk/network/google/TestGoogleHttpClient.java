/*
 * Copyright 2016-2023 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.mock.action.ExpectationResponseCallback;
import org.mockserver.model.Cookie;
import org.mockserver.model.Delay;
import org.mockserver.model.HttpResponse;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpClassCallback.callback;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(GoogleHttpClient.class)
public class TestGoogleHttpClient {
    public static final String OAUTH_AUTH_URL = "/authentication/sign_in";
    public static final String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";

    private static final Logger logger = LoggerFactory.getLogger(TestGoogleHttpClient.class);

    @Test
    public void testRequestRetry() throws Exception {

        Authentication authentication = new SimpleUserAuthentication("", "");
        GoogleHttpClient googleHttpClientSpy = spy(new GoogleHttpClient("http://url.com", authentication));

        doReturn(null).when(googleHttpClientSpy, "convertOctaneRequestToGoogleHttpRequest", any(OctaneHttpRequest.class));
        doReturn(true).when(googleHttpClientSpy, "authenticate");
        Whitebox.setInternalState(googleHttpClientSpy, "lastUsedAuthentication", PowerMockito.mock(Authentication.class));
        long currentTimeMs = System.currentTimeMillis();
        Whitebox.setInternalState(googleHttpClientSpy, "lastSuccessfulAuthTimestamp", currentTimeMs);
        Whitebox.setInternalState(googleHttpClientSpy, "requestStartTime", ThreadLocal.withInitial(() -> currentTimeMs + 1));

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
            set(Setting.READ_TIMEOUT, 55000);
            set(Setting.CONNECTION_TIMEOUT, 2345);
        }};

        GoogleHttpClient client = new GoogleHttpClient("http://google.com:8090", null, settings);
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
        ClientAndServer clientAndServer = startClientAndServer();
        Octane octane;
        long totalExecutionTime = 500;
        long cookieExpirationTime = 250;

        initServerResponse(clientAndServer, cookieExpirationTime);
        Authentication authentication = new SimpleUserAuthentication("", "");
        String url = "http://localhost:" + clientAndServer.getLocalPort();
        GoogleHttpClient spyGoogleHttpClient = spy(new GoogleHttpClient(url, authentication));
        octane = new Octane.Builder(authentication, spyGoogleHttpClient).Server(url).workSpace(1002).sharedSpace(1001).build();

        int nrCores = Math.max(Runtime.getRuntime().availableProcessors(),2);
        IntStream.rangeClosed(1, nrCores).parallel().forEach((nr) -> runGetRequests(octane, totalExecutionTime));

        clientAndServer.stop();

        //Exactly 2 authentications are done: one for 1st log in and 2nd for cookie refresh
        Mockito.verify(spyGoogleHttpClient, times(2)).authenticate();

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

    @Test
    public void testCookieCollision() {
        try (ClientAndServer clientAndServer = startClientAndServer()) {

            int nrCores = Math.max(Runtime.getRuntime().availableProcessors(), 4);
            TestOctaneResponseCallback.cores = nrCores;

            clientAndServer
                    .when(request())
                    .respond(
                            callback().withCallbackClass(
                                    TestOctaneResponseCallback.class
                            )
                    );

            Authentication authentication = new SimpleUserAuthentication("", "");
            String url = "http://localhost:" + clientAndServer.getLocalPort();
            GoogleHttpClient googleHttpClient = new GoogleHttpClient(url, authentication);

            googleHttpClient.authenticate();
            Octane octane = new Octane.Builder(authentication, googleHttpClient)
                    .Server(url).workSpace(1002).sharedSpace(1001).build();

            IntStream.rangeClosed(1, nrCores).parallel().forEach((nr) -> runGetRequests(octane, 5000));

        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        }
    }

    public static class TestOctaneResponseCallback implements ExpectationResponseCallback {
        public static int cores = 4;
        private static int regularRequestsCount = 0;
        private static int authRequestsCount = 0;
        private static Cookie usingCookie = null;
        private static Long cookieExpiredAt = null;
        private static Long cookieShouldBeUpdatedAt = null;
        private final static Random random = new Random();

        public synchronized void setNewUsingCookie(String name) {
            usingCookie = new Cookie(LWSSO_COOKIE_KEY, name);
            cookieExpiredAt = System.currentTimeMillis() + 1000;
            cookieShouldBeUpdatedAt = System.currentTimeMillis() + 500;
            logger.debug("Using new cookie: {}={}", usingCookie.getName(), usingCookie.getValue());
        }
        public boolean isCookieShouldBeUpdated() {
            return cookieShouldBeUpdatedAt != null && cookieShouldBeUpdatedAt <= System.currentTimeMillis();
        }
        public static Delay getServerDelay() {
            int max = 50;
            int min = 10;
            return Delay.milliseconds(random.nextInt(max - min + 1) + min);
        }
        @Override
        public HttpResponse handle(org.mockserver.model.HttpRequest httpRequest) throws Exception {
            String howToHandle = "empty";
            boolean isAuthRequest = httpRequest.getPath().getValue().endsWith(OAUTH_AUTH_URL);
            synchronized (TestOctaneResponseCallback.class) {
                if (isAuthRequest) {
                    howToHandle = "auth";
                    authRequestsCount++;
                } else {
                    if (cookieExpiredAt != null) {
                        Long currentTime = System.currentTimeMillis();
                        if (cookieExpiredAt <= currentTime) {
                            logger.debug("NULL the cookie");
                            usingCookie = null;
                        }
                    }
                    if (usingCookie != null && httpRequest.getCookies() != null) {
                        List<Cookie> cookies = httpRequest.getCookies().getEntries();
                        for (Cookie cookie : cookies) {
                            if (cookie.equals(usingCookie)) {
                                howToHandle = "regular";
                                break;
                            }
                        }
                    }
                    regularRequestsCount++;
                }
            }
            switch (howToHandle) {
                case "auth":
                    synchronized (TestOctaneResponseCallback.class) {
                        setNewUsingCookie("auth-lwsso-" + authRequestsCount);
                        return response()
                                .withStatusCode(200)
                                .withCookie(usingCookie.getName(), usingCookie.getValue());
                    }
                case "regular":
                    synchronized (TestOctaneResponseCallback.class) {
                        if (isCookieShouldBeUpdated()) {
                            setNewUsingCookie("regular-lwsso-" + regularRequestsCount);
                            return response().withStatusCode(200)
                                    .withCookie(usingCookie.getName(), usingCookie.getValue());
                        }
                    }
                    return response().withStatusCode(200).withDelay(getServerDelay());
                default:
                    return response().withStatusCode(401).withDelay(getServerDelay())
                            .withBody("{\"errorCode\":\"VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT\"," +
                                    "\"accumulatedMessage\":\"Previous validation errors " +
                                    "(non mandatory validators): \"}");
            }
        }
    }
}

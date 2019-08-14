/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.mockserver.model.Cookie;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class TestGoogleHttpClientParallelCookies {

    private Octane octane;

    public ClientAndServer clientAndServer = new ClientAndServer();


    public void setServerResponse() {
        Cookie c1 = new Cookie("LWSSO_COOKIE_KEY", "one");
        Cookie c2 = new Cookie("LWSSO_COOKIE_KEY", "two");

        clientAndServer
                .when(
                        request().withPath("/authentication/sign_in"),
                        Times.once()
                )
                .respond(
                        response().withStatusCode(200).withCookie(c1)


                );

        clientAndServer
                .when(
                        request().withPath("/authentication/sign_in")
                )
                .respond(
                        response().withStatusCode(200).withCookie(c2)
                );

        clientAndServer
                .when(
                        request().withCookie(c2)
                )
                .respond(
                        response().withStatusCode(200)
                );
        clientAndServer
                .when(
                        request().withMethod("GET").withCookie(c1),
                        Times.unlimited(),
                        TimeToLive.exactly(TimeUnit.SECONDS, 2L)
                )
                .respond(
                        response().withStatusCode(200)
                );
        clientAndServer
                .when(
                        request().withMethod("GET").withCookie(c1),
                        Times.unlimited()
                )
                .respond(
                        response().withStatusCode(401).withBody("{\"errorCode\":\"VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT\",\"accumulatedMessage\":\"Previous validation errors (non mandatory validators): \"}")
                );
    }


    @Test
    public void test2() throws InterruptedException {
        setServerResponse();
        Authentication authentication = new SimpleUserAuthentication("sa@nga", "Welcome1");
        String url = "http://localhost:" + clientAndServer.getLocalPort();
        GoogleHttpClientProfiler profiler = new GoogleHttpClientProfiler(url);
        octane = new Octane.Builder(authentication, profiler).Server(url).workSpace(1002).sharedSpace(1001).build();
        System.out.println(profiler.getAll());

        Thread t = new Thread(() -> runGetRequest(profiler));
        Thread t2 = new Thread(() ->runGetRequest(profiler));
        Thread t3 = new Thread(() -> runGetRequest(profiler));
        t.start();
        t2.start();
        t3.start();
        t.join();
        t2.join();
        t3.join();
       clientAndServer.stop();
    }

    public void runGetRequest(GoogleHttpClientProfiler profiler) {
        LocalDateTime pastTime = LocalDateTime.now();
        LocalDateTime currentTime = LocalDateTime.now();
        while (Duration.between(pastTime, currentTime).toMillis() < 3000) {

            octane.entityList("").get().execute();
            System.out.println("@pid:"+Thread.currentThread().getId() + profiler.getAll());
            currentTime = LocalDateTime.now();
        }
    }
}

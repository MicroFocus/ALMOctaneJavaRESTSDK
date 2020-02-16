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
package com.hpe.adm.nga.sdk.utils;

import java.net.Socket;

/**
 *
 * Created by Guy Guetta on 21/04/2016.
 */
public class HttpUtils {
    private HttpUtils() {
    }

    // Setup System "keep-alive" HTTP header value
    public static void SetSystemKeepAlive(Boolean isKeepAlive) {
        System.setProperty("http.keepAlive", isKeepAlive ? "true" : "false");
    }

    // Setup System proxy for logs in "Fiddler"
    public static void SetSystemProxy() {
        if (System.getProperty("should.set.proxy") != null
                && System.getProperty("should.set.proxy").equals("true")) {
            String proxyHost = "127.0.0.1";
            int proxyPort = 8888;
            if (isProxyServerRunning(proxyHost, proxyPort)) {
                // HTTP
                System.setProperty("http.proxyHost", proxyHost);
                System.setProperty("http.proxyPort", String.valueOf(proxyPort));
                // HTTPS
                System.setProperty("https.proxyHost", proxyHost);
                System.setProperty("https.proxyPort", String.valueOf(proxyPort));
            }
        }
    }

    //TODO: may be add this???
    //System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    //System.setProperty("http.agent", "");

    private static boolean isProxyServerRunning(String host, int port) {
        Socket s = null;
        try {
            s = new Socket(host, port);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (Exception e) {
                }
        }
    }
}

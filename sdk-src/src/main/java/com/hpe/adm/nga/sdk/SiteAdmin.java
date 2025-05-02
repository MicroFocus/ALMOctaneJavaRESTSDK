/*
 * Copyright 2016-2025 Open Text.
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
package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import com.hpe.adm.nga.sdk.network.jetty.JettyHttpClient;
import com.hpe.adm.nga.sdk.siteadmin.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the octane site admin APIs.  This is different from the {@link Octane} instance in that there are no
 * spaces or workspaces that can be referenced.  See the REST API documentation for more information as to what can
 * be garnered from the site admin api
 */
public class SiteAdmin {

    private static final String SITE_ADMIN_DOMAIN_FORMAT = "/admin/";

    //private members
    private final String urlDomain;
    private final OctaneHttpClient octaneHttpClient;

    // functions
    private SiteAdmin(OctaneHttpClient octaneHttpClient, String domain) {
        this.octaneHttpClient = octaneHttpClient;
        urlDomain = domain + SITE_ADMIN_DOMAIN_FORMAT;
    }

    /**
     * Gets an instance of the server API.  Used for getting the server version as an example
     *
     * @return A new server instance
     */
    public Server getServer() {
        return new Server(octaneHttpClient, urlDomain);
    }

    /**
     * This class is used to create an {@link SiteAdmin} instance for site admin API usage.  It is initialised using the correct {@link Authentication}
     * <br>
     * The {@code Builder} class uses the builder pattern.  This builds up the correct Octane REST API context.
     * <br>
     * Once the correct context has been built up use the {@link #build()} method to create the {@code SiteAdmin} instance
     */
    public static class Builder {
        //Private
        private final Logger logger = LoggerFactory.getLogger(Octane.class.getName());
        protected String urlDomain = "";
        protected OctaneHttpClient octaneHttpClient;
        private final Authentication authentication;
        private boolean isHttp2 = false;

        //Functions

        /**
         * Creates a new Builder object using the correct authentication
         *
         * @param authentication - Authentication object.  Cannot be null
         * @throws NullPointerException if the authentication object is null
         */
        public Builder(Authentication authentication) {
            assert authentication != null;
            this.authentication = authentication;
        }

        /**
         * Creates a new Builder object using the correct authentication
         *
         * @param authentication   - Authentication object.  Cannot be null
         * @param octaneHttpClient - Implementation of {@link OctaneHttpClient}. Cannot be null
         * @throws AssertionError if the authentication or octaneHttpClient is null
         */
        public Builder(Authentication authentication, OctaneHttpClient octaneHttpClient) {
            assert authentication != null;
            assert octaneHttpClient != null;
            this.authentication = authentication;
            this.octaneHttpClient = octaneHttpClient;
        }

        public Builder isHttp2(boolean http2) {

            isHttp2 = http2;
            return this;
        }


        /**
         * Sets the domain and the port.  The domain should include the full http scheme (http/https)
         * <br>
         * eg {@code http://octane.server.com}
         *
         * @param domain - domain name including http scheme
         * @param port   - port number
         * @return this object
         * @throws NullPointerException if the domain is null
         */
        public Builder Server(String domain, int port) {

            urlDomain = domain + ":" + port;

            return this;
        }

        /**
         * Sets the domain and the port.  The domain should include the full http scheme (http/https)
         * <br>
         * eg {@code http://octane.server.com}
         *
         * @param domain - domain name including http scheme
         * @return this object
         * @throws NullPointerException if the domain is null
         */
        public Builder Server(String domain) {

            urlDomain = domain;

            return this;
        }

        /**
         * The main build procedure which creates the {@link SiteAdmin} object and authenticates against the server
         *
         * @return a new SiteAdmin instance which has the set context and is correctly authenticated
         */
        public SiteAdmin build() {

            SiteAdmin objOctane = null;

            logger.info("Building SiteAdmin Octane context using {}", this);

            // Init default http client if it wasn't specified
            OctaneHttpClient client = isHttp2 ? new JettyHttpClient(urlDomain, authentication) : new GoogleHttpClient(urlDomain, authentication);
            this.octaneHttpClient = this.octaneHttpClient == null ? client : this.octaneHttpClient;

            if (octaneHttpClient.authenticate()) {
                objOctane = getSiteAdmin();
            }

            return objOctane;
        }

        protected SiteAdmin getSiteAdmin() {
            return new SiteAdmin(octaneHttpClient, urlDomain);
        }

        @Override
        public String toString() {
            return String.format("Server: %s Admin", urlDomain);
        }
    }
}

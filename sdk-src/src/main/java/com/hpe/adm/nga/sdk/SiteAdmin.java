package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import com.hpe.adm.nga.sdk.siteadmin.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Server getServer() {
        return new Server(octaneHttpClient, urlDomain);
    }

    /**
     * This class is used to create an {@link Octane} instance for site admin API usage.  It is initialised using the correct {@link Authentication}
     * <br>
     * The {@code AdminBuilder} class uses the builder pattern.  This builds up the correct Octane REST API context.
     * <br>
     * Once the correct context has been built up use the {@link #build()} method to create the {@code Octane} instance
     */
    public static class Builder {
        //Private
        private final Logger logger = LoggerFactory.getLogger(Octane.class.getName());
        protected String urlDomain = "";
        protected OctaneHttpClient octaneHttpClient;
        private final Authentication authentication;

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

            urlDomain = domain + ":" + String.valueOf(port);

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
         * The main build procedure which creates the {@link Octane} object and authenticates against the server
         *
         * @return a new Octane instance which has the set context and is correctly authenticated
         */
        public SiteAdmin build() {

            SiteAdmin objOctane = null;

            logger.info("Building SiteAdmin Octane context using {}", this);

            // Init default http client if it wasn't specified
            this.octaneHttpClient = this.octaneHttpClient == null ? new GoogleHttpClient(urlDomain) : this.octaneHttpClient;

            if (octaneHttpClient.authenticate(authentication)) {
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

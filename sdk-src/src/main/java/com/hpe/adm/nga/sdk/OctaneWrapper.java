package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.authentication.BasicAuthentication;
import com.hpe.adm.nga.sdk.authentication.ExplicitAuthentication;
import com.hpe.adm.nga.sdk.authentication.ExplicitAuthenticationRequest;
import com.hpe.adm.nga.sdk.classfactory.OctaneClassFactory;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;

import java.util.HashMap;
import java.util.Map;

public class OctaneWrapper {

    private final static OctaneCustomSettings defaultOctaneSettings = new OctaneCustomSettings();
    protected final String urlDomain;
    protected final OctaneInternalConfiguration octaneInternalConfiguration;

    private OctaneWrapper(final OctaneInternalConfiguration octaneInternalConfiguration, final String urlDomain) {
        this.octaneInternalConfiguration = octaneInternalConfiguration;
        this.urlDomain = urlDomain;
    }

    public Octane.Builder octane() {
        return new Octane.Builder(octaneInternalConfiguration, urlDomain);
    }

    public SiteAdmin.Builder siteAdmin() {
        return new SiteAdmin.Builder(octaneInternalConfiguration.getOctaneHttpClient(), urlDomain);
    }

    public static class ExplicitAuthenticationOctaneWrapper extends OctaneWrapper {
        private final ExplicitAuthentication explicitAuthentication;

        private ExplicitAuthenticationOctaneWrapper(final OctaneInternalConfiguration octaneInternalConfiguration, final String urlDomain,
                                                    final ExplicitAuthentication explicitAuthentication) {
            super(octaneInternalConfiguration, urlDomain);
            this.explicitAuthentication = explicitAuthentication;
        }

        public ExplicitAuthenticationRequest authenticate() {
            return new ExplicitAuthenticationRequest(octaneInternalConfiguration.getOctaneHttpClient(), urlDomain, explicitAuthentication);
        }
    }

    public static class Builder {

        public ExplicitAuthenticationOctaneWrapperBuilder authentication(ExplicitAuthentication explicitAuthentication) {
            return new ExplicitAuthenticationOctaneWrapperBuilder(explicitAuthentication);
        }

        public BasicAuthenticationOctaneWrapperBuilder authentication(BasicAuthentication basicAuthentication) {
            return new BasicAuthenticationOctaneWrapperBuilder(basicAuthentication);
        }
    }

    public abstract static class OctaneWrapperBuilder<T extends OctaneWrapper> {

        protected OctaneHttpClient octaneHttpClient;
        private OctaneCustomSettings customSettings;
        private String octaneClassFactoryClassName;

        private OctaneWrapperBuilder() {
        }

        protected String urlDomain = "";

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
        public OctaneWrapperBuilder<T> Server(String domain, int port) {

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
        public OctaneWrapperBuilder<T> Server(String domain) {

            urlDomain = domain;

            return this;
        }

        /**
         * Sets the {@link OctaneHttpClient} instance.
         *
         * @param octaneHttpClient The instance to use
         * @return An instance of this builder object
         */
        public OctaneWrapperBuilder<T> OctaneHttpClient(OctaneHttpClient octaneHttpClient) {
            this.octaneHttpClient = octaneHttpClient;

            return this;
        }

        /**
         * Sets the OctaneClassFactoryName.
         * See {@link OctaneClassFactory} for more details
         *
         * @param octaneClassFactoryClassName The name of the {@link OctaneClassFactory} to use
         * @return An instance of this builder object
         */
        public OctaneWrapperBuilder<T> OctaneClassFactoryClassName(String octaneClassFactoryClassName) {
            this.octaneClassFactoryClassName = octaneClassFactoryClassName;

            return this;
        }

        /**
         * Configure a settings provider with custom settings like: readTimeout
         *
         * @param settings - a plain java object with timeout settings(for now)
         * @return An instance of this builder object
         */
        public OctaneWrapperBuilder<T> settings(OctaneCustomSettings settings) {

            customSettings = settings;

            return this;
        }

        public final T build() {
            final OctaneInternalConfiguration octaneInternalConfiguration = new OctaneInternalConfiguration();
            // Init default http client if it wasn't specified
            OctaneCustomSettings settings = customSettings != null ? customSettings : defaultOctaneSettings;
            octaneInternalConfiguration.octaneHttpClient = this.octaneHttpClient == null
                    ? getOctaneHttpClient(settings) : this.octaneHttpClient;
            octaneInternalConfiguration.octaneClassFactoryClassName = this.octaneClassFactoryClassName;

            return getOctaneWrapperInstance(octaneInternalConfiguration);
        }

        protected abstract OctaneHttpClient getOctaneHttpClient(OctaneCustomSettings settings);

        protected abstract T getOctaneWrapperInstance(OctaneInternalConfiguration octaneInternalConfiguration);
    }

    public static class ExplicitAuthenticationOctaneWrapperBuilder extends OctaneWrapperBuilder<ExplicitAuthenticationOctaneWrapper> {
        private final ExplicitAuthentication explicitAuthentication;

        private ExplicitAuthenticationOctaneWrapperBuilder(ExplicitAuthentication explicitAuthentication) {
            this.explicitAuthentication = explicitAuthentication;
        }

        @Override
        protected OctaneHttpClient getOctaneHttpClient(OctaneCustomSettings settings) {
            return this.octaneHttpClient == null ? new GoogleHttpClient(urlDomain, settings) : this.octaneHttpClient;
        }

        protected ExplicitAuthenticationOctaneWrapper getOctaneWrapperInstance(OctaneInternalConfiguration octaneInternalConfiguration) {
            return new ExplicitAuthenticationOctaneWrapper(octaneInternalConfiguration, urlDomain, explicitAuthentication);
        }
    }

    public static class BasicAuthenticationOctaneWrapperBuilder extends OctaneWrapperBuilder<OctaneWrapper> {
        private final BasicAuthentication basicAuthentication;

        private BasicAuthenticationOctaneWrapperBuilder(BasicAuthentication basicAuthentication) {
            this.basicAuthentication = basicAuthentication;
        }

        @Override
        protected OctaneWrapper getOctaneWrapperInstance(OctaneInternalConfiguration octaneInternalConfiguration) {
            return new OctaneWrapper(octaneInternalConfiguration, urlDomain);
        }

        @Override
        protected OctaneHttpClient getOctaneHttpClient(OctaneCustomSettings settings) {
            return this.octaneHttpClient == null ? new GoogleHttpClient(urlDomain, basicAuthentication) : this.octaneHttpClient;
        }
    }

    /**
     * Used to aggregate internal configurations that need to be passed around
     */
    public static final class OctaneInternalConfiguration {
        private OctaneHttpClient octaneHttpClient;
        private String octaneClassFactoryClassName;

        /**
         * Returns the instance of the {@link OctaneHttpClient}
         *
         * @return instance
         */
        public OctaneHttpClient getOctaneHttpClient() {
            return octaneHttpClient;
        }

        /**
         * Returns the class string of the {@link OctaneClassFactory}
         *
         * @return class string
         */
        public String getOctaneClassFactoryClassName() {
            return octaneClassFactoryClassName;
        }
    }

    /**
     * Octane settings holder containing lower level configurations
     */
    public static class OctaneCustomSettings {

        public enum Setting {
            READ_TIMEOUT,
            CONNECTION_TIMEOUT,
            TRUST_ALL_CERTS,
            SHARED_HTTP_TRANSPORT
        }

        private final Map<Setting, Object> settings = new HashMap<>();

        // Initialize defaults
        {
            settings.put(Setting.READ_TIMEOUT, 60000);
            settings.put(Setting.CONNECTION_TIMEOUT, 10000);
            settings.put(Setting.TRUST_ALL_CERTS, false);
            settings.put(Setting.SHARED_HTTP_TRANSPORT, null);
        }

        public void set(Setting setting, Object value) {
            settings.put(setting, value);
        }

        public Object get(Setting setting) {
            return settings.get(setting);
        }
    }
}

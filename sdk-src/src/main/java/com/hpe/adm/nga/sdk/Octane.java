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
package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.classfactory.OctaneClassFactory;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents the main Octane context.  This context represents the following:
 * <br>
 * <ul>
 * <li>Octane server</li>
 * <li>Sharedspace id</li>
 * <li>Workspace id</li>
 * <li>Authentication object</li>
 * </ul>
 * <br>
 * This represents the following URL in the Octane REST API:
 * <br>
 * <p>
 * {@code server_url:port/api/shared_spaces/[sharedspace_id]/workspaces/[workspace_id]}
 * </p>
 * However if both the sharedspace and workspace ids are omitted the context will be assumed to be the space admin:
 * <br>
 * <p>
 * {@code server_url:port/api/shared_spaces}
 * </p>
 * <br>
 * If only the workspace id is omitted the context is assumed to be workspace admin
 * <br>
 * <p>
 * {@code server_url:port/api/shared_spaces/[sharedspace_id]}
 * </p>
 * <br>
 * <p>
 * The {@code Octane} class is instantialized using the {@link Octane.Builder} class.  Once that instance has been
 * obtained the Octane context can be used to create further entity, metadata, attachment contexts or to sign out of the server.
 * </p>
 * <p>
 * The Octane instance can be reused to create different contexts
 * </p>
 */
public class Octane {

    //Constants
    private static final String SHARED_SPACES_DOMAIN_FORMAT = "%s/api/shared_spaces";
    private static final String ID_FORMAT = "%s/%s";
    private static final String WORKSPACES_DOMAIN_FORMAT = "%s/workspaces";
    private static final Logger logger = LoggerFactory.getLogger(Octane.class.getName());
    public static final long NO_WORKSPACE_ID = Long.MIN_VALUE;
    public static final String NO_ENTITY = "";
    private static final long ONLY_SHAREDSPACE_WORKSPACE_ID = 0L;

    //private members
    private final String urlDomain;
    private final String idsharedSpaceId;
    private final long workSpaceId;
    private final OctaneInternalConfiguration octaneInternalConfiguration;

    private final static OctaneCustomSettings defaultOctaneSettings = new OctaneCustomSettings();

    // functions
    private Octane(OctaneInternalConfiguration octaneInternalConfiguration, String domain, String sharedSpaceId, long workId) {
        this.octaneInternalConfiguration = octaneInternalConfiguration;
        urlDomain = domain;
        idsharedSpaceId = sharedSpaceId;
        workSpaceId = workId;
        logger.info("Setting context to: domain=" + urlDomain + "; spaceid=" + idsharedSpaceId + "; workspaceid=" + workSpaceId);
    }

    private Octane(OctaneInternalConfiguration octaneInternalConfiguration, String domain, String sharedSpaceId) {
        this(octaneInternalConfiguration, domain, sharedSpaceId, ONLY_SHAREDSPACE_WORKSPACE_ID);
    }

    private Octane(OctaneInternalConfiguration octaneInternalConfiguration, String domain) {
        this(octaneInternalConfiguration, domain, null);
    }

    /**
     * <p>
     * Creates a new EntityList context.  The entity name should be the collection name of the entity.
     * For example {@code defects, tests, releases}
     * </p>
     * This method creates a new separate entity context each time that can be reused or used in parallel
     * <p>
     * By using {@link Octane#NO_ENTITY} this is set to be the admin context to get the list of spaces or workspaces
     * </p>
     *
     * @param entityName - The name of the entity as a collection
     * @return A new EntityList object that list of entities
     */
    public EntityList entityList(String entityName) {
        return OctaneClassFactory.getImplementation(octaneInternalConfiguration.octaneClassFactoryClassName)
                .getEntityList(octaneInternalConfiguration.octaneHttpClient, getBaseDomainFormat(), entityName);
    }

    /**
     * Creates a new {@link TypedEntityList} context.  The class is the concrete instance of the TypedEntityList
     *
     * @param entityListClass The class that is the instance of the TypedEntityList
     * @param <T>             The type of class
     * @return The instance that can then be set as the context
     */
    public <T extends TypedEntityList> T entityList(Class<T> entityListClass) {
        return OctaneClassFactory.getImplementation(octaneInternalConfiguration.octaneClassFactoryClassName)
                .getEntityList(octaneInternalConfiguration.octaneHttpClient, getBaseDomainFormat(), entityListClass);
    }

    /**
     * Creates a new Metadata object.  This represents the following URL:
     * <p>
     * {@code [workspace_url/metadata}
     * </p>
     * <p>
     * This can then be used further to get metadata information from the server
     * </p>
     *
     * @return A new Metadata object that holds the metadata context
     */
    public Metadata metadata() {
        return new Metadata(octaneInternalConfiguration.octaneHttpClient, getBaseDomainFormat());
    }

    /**
     * Creates a new attachmentList object.  This returns the context for attachments.  This is equivalent to
     * <br>
     * {@code [workspace_url/attachments}
     *
     * @return A new attachmentList object that holds the attachments context
     */
    public AttachmentList attachmentList() {
        return new AttachmentList(octaneInternalConfiguration, getBaseDomainFormat());
    }

    /**
     * get the base domain based on workSpaceId and idsharedSpaceId
     *
     * @return base domain
     */
    private String getBaseDomainFormat() {
        // this is the same as SERVER/api/shared_spaces.  Used to get information about all spaces (such as IDs)
        String baseDomain = String.format(SHARED_SPACES_DOMAIN_FORMAT, urlDomain);
        // if there is a shared space ID that means that we are entering a specific space
        if (idsharedSpaceId != null && !idsharedSpaceId.isEmpty()) {
            // this is the same as SERVER/api/shared_spaces/id.
            baseDomain = String.format(ID_FORMAT, baseDomain, idsharedSpaceId);
            // this is the same as SERVER/api/shared_spaces/id/workspaces.  Used to get information about all workspaces (such as IDs) within a specific space
            if (workSpaceId == NO_WORKSPACE_ID) {
                baseDomain = String.format(WORKSPACES_DOMAIN_FORMAT, baseDomain);
            }
            // this is the same as SERVER/api/shared_spaces/id/workspaces/id  Normal workspace context
            else if (workSpaceId != ONLY_SHAREDSPACE_WORKSPACE_ID) {
                baseDomain = String.format(ID_FORMAT, String.format(WORKSPACES_DOMAIN_FORMAT, baseDomain), workSpaceId);
            }
            // if neither of the above are triggered then we remain in a space id but without any workspace context
            // here we just add the end / to ensure the correct URL
            baseDomain = baseDomain.concat("/");
        }

        return baseDomain;
    }

    /**
     * Signs out of the Octane server.  Any cookies that are held are deleted
     */
    public void signOut() {
        octaneInternalConfiguration.octaneHttpClient.signOut();
    }

    /**
     * This class is used to create an {@link Octane} instance for normal API usage.  It is initialised using the correct {@link Authentication}
     * <br>
     * The {@code Builder} class uses the builder pattern.  This builds up the correct Octane REST API context.  It is not
     * necessary to add a sharedspace or workspace and will work with entities under that context.
     * <br>
     * Use the workspace and sharedspace methods only once otherwise the behaviour cannot be guaranteed!
     * <br>
     * Once the correct context has been built up use the {@link #build()} method to create the {@code Octane} instance
     */
    public static class Builder {
        //Private
        private final Logger logger = LoggerFactory.getLogger(Octane.class.getName());
        private String urlDomain = "";
        private String idsharedSpaceId = null;
        private long workSpaceId = 0;
        private OctaneHttpClient octaneHttpClient;
        private final Authentication authentication;
        private String octaneClassFactoryClassName;
        private OctaneCustomSettings customSettings;

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
         * Sets the shared_space id to be a UUID object
         *
         * @param ssID - sharedSpace id
         * @return this instance
         * @throws NullPointerException if ssID is null
         */
        public Builder sharedSpace(UUID ssID) {

            idsharedSpaceId = ssID.toString();
            return this;
        }

        /**
         * Sets the shared_space id to be a long
         *
         * @param ssID - sharedSpace id
         * @return this instance
         */
        public Builder sharedSpace(long ssID) {

            idsharedSpaceId = String.valueOf(ssID);
            return this;
        }

        /**
         * Sets the workspace id to be a long.
         * <p>
         * This can be set to be {@link Octane#NO_WORKSPACE_ID} which means that the context is set to be the space
         * admin to get the list of workspaces
         * <br>
         * If this is not set and the space id is set the context is assumed to be the space admin
         * </p>
         *
         * @param lId - workSpace id
         * @return this instance
         */
        public Builder workSpace(long lId) {

            workSpaceId = lId;
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
         * Sets the OctaneClassFactoryName.
         * See {@link OctaneClassFactory} for more details
         *
         * @param octaneClassFactoryClassName The name of the {@link OctaneClassFactory} to use
         * @return An instance of this builder object
         */
        public Builder OctaneClassFactoryClassName(String octaneClassFactoryClassName) {
            this.octaneClassFactoryClassName = octaneClassFactoryClassName;

            return this;
        }

        /**
         * Sets the {@link OctaneHttpClient} instance.
         *
         * @param octaneHttpClient The instance to use
         * @return An instance of this builder object
         */
        public Builder OctaneHttpClient(OctaneHttpClient octaneHttpClient) {
            this.octaneHttpClient = octaneHttpClient;

            return this;
        }

        /**
         * Configure a settings provider with custom settings like: readTimeout
         * @param settings - a plain java object with timeout settings(for now)
         * @return An instance of this builder object
         */
        public Builder settings(OctaneCustomSettings settings) {

            customSettings = settings;

            return this;
        }

        /**
         * The main build procedure which creates the {@link Octane} object and authenticates against the server
         *
         * @return a new Octane instance which has the set context and is correctly authenticated
         */
        public Octane build() {

            Octane objOctane = null;

            logger.info("Building Octane context using {}", this);

            final OctaneInternalConfiguration octaneInternalConfiguration = new OctaneInternalConfiguration();
            // Init default http client if it wasn't specified
            OctaneCustomSettings settings = customSettings != null ? customSettings : defaultOctaneSettings;
            octaneInternalConfiguration.octaneHttpClient = this.octaneHttpClient == null ? new GoogleHttpClient(urlDomain, settings) : this.octaneHttpClient;
            octaneInternalConfiguration.octaneClassFactoryClassName = this.octaneClassFactoryClassName;

            if (octaneInternalConfiguration.octaneHttpClient.authenticate(authentication)) {
                if (idsharedSpaceId == null) {
                    objOctane = new Octane(octaneInternalConfiguration, urlDomain);
                } else {
                    objOctane = new Octane(octaneInternalConfiguration, urlDomain, idsharedSpaceId, workSpaceId);
                }
            }

            return objOctane;
        }

        @Override
        public String toString() {
            return String.format("Server: %s SharedSpace: %s Workspace: %s", urlDomain, idsharedSpaceId, workSpaceId);
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

        private Map<Setting, Object> settings = new HashMap<>();

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

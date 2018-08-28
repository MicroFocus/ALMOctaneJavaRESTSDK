/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microfocus.adm.nga.sdk;

import com.microfocus.adm.nga.sdk.attachments.AttachmentList;
import com.microfocus.adm.nga.sdk.authentication.Authentication;
import com.microfocus.adm.nga.sdk.entities.EntityList;
import com.microfocus.adm.nga.sdk.entities.TypedEntityList;
import com.microfocus.adm.nga.sdk.metadata.Metadata;
import com.microfocus.adm.nga.sdk.network.OctaneHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * <p>
 * This represents the following URL in the Octane REST API:
 * <br>
 * {@code
 * <p>
 * server_url:port/api/shared_spaces/[sharedspace_id]/workspaces/[workspace_id]
 * }
 * </p>
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
    private static final String SITE_ADMIN_DOMAIN_FORMAT = "/api/siteadmin/";
    private static final String SHARED_SPACES_DOMAIN_FORMAT = "%s/api/shared_spaces/%s/";
    private static final String WORKSPACES_DOMAIN_FORMAT = "workspaces/%s/";
    private static final Logger logger = LoggerFactory.getLogger(Octane.class.getName());

    //private members
    private final String urlDomain;
    private final String idsharedSpaceId;
    private final long workSpaceId;
    private final OctaneHttpClient octaneHttpClient;

    // functions
    private Octane(OctaneHttpClient octaneHttpClient, String domain, String sharedSpaceId, long workId) {
        this.octaneHttpClient = octaneHttpClient;
        urlDomain = domain;
        idsharedSpaceId = sharedSpaceId;
        workSpaceId = workId;
    }

    /**
     * <p>
     * Creates a new EntityList context.  The entity name should be the collection name of the entity.
     * For example {@code defects, tests, releases}
     * </p>
     * This method creates a new separate entity context each time that can be reused or used in parallel
     * <p>
     *
     * @param entityName - The name of the entity as a collection
     * @return A new EntityList object that list of entities
     */
    public EntityList entityList(String entityName) {
        return OctaneClassFactory.getSystemParamImplementation().getEntityList(octaneHttpClient, getBaseDomainFormat(), entityName);
    }

    /**
     * Creates a new {@link TypedEntityList} context.  The class is the concrete instance of the TypedEntityList
     * @param entityListClass The class that is the instance of the TypedEntityList
     * @param <T> The type of class
     * @return The instance that can then be set as the context
     */
    public <T extends TypedEntityList> T entityList(Class<T> entityListClass) {
        return OctaneClassFactory.getSystemParamImplementation().getEntityList(octaneHttpClient, getBaseDomainFormat(), entityListClass);
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
        return new Metadata(octaneHttpClient, getBaseDomainFormat());
    }

    /**
     * Creates a new attachmentList object.  This returns the context for attachments.  This is equivalent to
     * <br>
     * {@code [workspace_url/attachments}
     *
     * @return A new attachmentList object that holds the attachments context
     */
    public AttachmentList attachmentList() {
        return new AttachmentList(octaneHttpClient, getBaseDomainFormat());
    }

    /**
     * get the base domain based on workSpaceId and idsharedSpaceId
     *
     * @return base domain
     */
    protected String getBaseDomainFormat() {

        String baseDomain = urlDomain + SITE_ADMIN_DOMAIN_FORMAT;

        if (idsharedSpaceId != null && !idsharedSpaceId.isEmpty()) {
            baseDomain = String.format(SHARED_SPACES_DOMAIN_FORMAT, urlDomain, idsharedSpaceId);

            if (workSpaceId != 0)
                baseDomain = baseDomain + String.format(WORKSPACES_DOMAIN_FORMAT, String.valueOf(workSpaceId));
        }

        return baseDomain;
    }

    /**
     * Signs out of the Octane server.  Any cookies that are held are deleted
     */
    public void signOut() {
        octaneHttpClient.signOut();
    }

    /**
     * This class is used to create an {@link Octane} instance.  It is initialised using the correct {@link Authentication}
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
         * Sets the workspace id to be a long
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
        public Octane build() {

            Octane objOctane = null;

            logger.info("Building Octane context using {}", this);
            OctaneHttpClient octaneHttpClient = createOctaneHttpClient();
            if (octaneHttpClient.authenticate(authentication)) {
                objOctane = new Octane(octaneHttpClient, urlDomain, idsharedSpaceId, workSpaceId);
            }

            return objOctane;
        }

        private OctaneHttpClient createOctaneHttpClient() {
            return OctaneClassFactory.getSystemParamImplementation().getOctaneHttpClient(urlDomain);
        }

        @Override
        public String toString() {
            return String.format("Server: %s SharedSpace: %s Workspace: %s", urlDomain, idsharedSpaceId, workSpaceId);
        }
    }

}

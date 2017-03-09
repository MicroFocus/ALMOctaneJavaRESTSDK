/*    Copyright 2017 Hewlett-Packard Development Company, L.P.
*    Licensed under the Apache License, Version 2.0 (the "License");
*    you may not use this file except in compliance with the License.
*    You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*    Unless required by applicable law or agreed to in writing, software
*    distributed under the License is distributed on an "AS IS" BASIS,
*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*    See the License for the specific language governing permissions and
*    limitations under the License.
*/

package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
 * The <code>Octane</code> class is instantialized using the {@link Octane.Builder} class.  Once that instance has been
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
    private static final String METADATA_DOMAIN_FORMAT = "metadata";
    private static final String ATTACHMENT_LIST_DOMAIN_FORMAT = "attachments";

    //private members
    private final String urlDomain;
    private final String idsharedSpaceId;
    private final long workSpaceId;
    private final OctaneHttpClient octaneHttpClient;

    // functions
    protected Octane(OctaneHttpClient octaneHttpClient, String domain, String sharedSpaceId, long workId) {
        this.octaneHttpClient = octaneHttpClient;
        urlDomain = domain;
        idsharedSpaceId = sharedSpaceId;
        workSpaceId = workId;
    }

    /**
     * <p>
     * Creates a new EntityList context.  The entity name should be the collection name of the entity.
     * For example <code>defects, tests, releases</code>
     * </p>
     * This method creates a new separate entity context each time that can be reused or used in parallel
     * <p>
     *
     * @param entityName - The name of the entity as a collection
     * @return A new EntityList object that list of entities
     */
    public EntityList entityList(String entityName) {

        String entityListDomain = getBaseDomainFormat() + entityName;
        return new EntityList(octaneHttpClient, entityListDomain);
    }

    /**
     * Creates a new Metadata object.  This represents the following URL:
     * <p>
     *     <code>[workspace_url/metadata</code>
     * </p>
     * <p>
     *     This can then be used further to get metadata information from the server
     * </p>
     *
     * @return A new Metadata object that holds the metadata context
     */
    public Metadata metadata() {
        String metadataDomain = getBaseDomainFormat() + METADATA_DOMAIN_FORMAT;
        return new Metadata(octaneHttpClient, metadataDomain);
    }

    /**
     * Creates a new AttachmentList object.  This returns the context for attachments.  This is equivalent to
     * <br>
     *  <code>[workspace_url/attachments</code>
     *
     * @return A new AttachmentList object that holds the attachments context
     */
    public AttachmentList AttachmentList() {

        String attachmentListDomain = getBaseDomainFormat() + ATTACHMENT_LIST_DOMAIN_FORMAT;
        return new AttachmentList(octaneHttpClient, attachmentListDomain);
    }

    /**
     * get the base domain based on workSpaceId and idsharedSpaceId
     *
     * @return base domain
     */
    private String getBaseDomainFormat() {

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
     * The <code>Builder</code> class uses the builder pattern.  This builds up the correct Octane REST API context.  It is not
     * necessary to add a sharedspace or workspace and will work with entities under that context.
     * <br>
     * Use the workspace and sharedspace methods only once otherwise the behaviour cannot be guaranteed!
     * <br>
     * Once the correct context has been built up use the {@link #build()} method to create the <code>Octane</code> instance
     */
    public static class Builder {
        //Private
        private final Logger logger = LogManager.getLogger(Octane.class.getName());
        private String urlDomain = "";
        private String idsharedSpaceId = null;
        private long workSpaceId = 0;
        private final AuthenticationProvider authenticationProvider;

        //Functions
        /**
         * Sets the {@link AuthenticationProvider} object the SDK will use each time it has to perform authentication with the server
         * Be careful with your {@link AuthenticationProvider} instance lifecycle,
         * as the SDK might try to call {@link AuthenticationProvider#getAuthentication()} method
         * when needed
         * @param authenticationProvider an implementation of {@link AuthenticationProvider}
         */
        public Builder(AuthenticationProvider authenticationProvider) {
            assert authenticationProvider != null;
            this.authenticationProvider = authenticationProvider;
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
         * eg <code>http://octane.server.com</code>
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
         * eg <code>http://octane.server.com</code>
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
            logger.info("Building Octane context using %s", this);
            OctaneHttpClient octaneHttpClient = new GoogleHttpClient(urlDomain, authenticationProvider);

            if(octaneHttpClient.authenticate()){
                return new Octane(octaneHttpClient, urlDomain, idsharedSpaceId, workSpaceId);
            }
            return null;
        }

        @Override
        public String toString() {
            return String.format("Server: %s2%s1SharedSpace: %s3Workspace: %s4", System.lineSeparator(), urlDomain, idsharedSpaceId, workSpaceId);
        }
    }
}

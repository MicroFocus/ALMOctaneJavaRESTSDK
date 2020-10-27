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
package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.classfactory.OctaneClassFactory;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.delete.DeleteEntities;
import com.hpe.adm.nga.sdk.entities.delete.DeleteEntity;
import com.hpe.adm.nga.sdk.entities.get.GetEntities;
import com.hpe.adm.nga.sdk.entities.get.GetEntity;
import com.hpe.adm.nga.sdk.entities.update.UpdateEntities;
import com.hpe.adm.nga.sdk.entities.update.UpdateEntity;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * <p>
 * The object that represents attachments in the REST API.  Attachments contain both the binary data and the metadata
 * surrounding them. See the REST API documentation for further information as to how to use attachments.
 * </p>
 * <p>
 * Attachments have different functionality depending on whether they are being created, updated, read or deleted.
 * </p>
 */
public class AttachmentList {

    private static final String ATTACHMENTS_URL = "attachments";
    private final EntityList entityList;
    private final OctaneHttpClient octaneHttpClient;
    private final String attachmentListDomain;

    /**
     * Creates a new attachmentList object
     *
     * @param octaneInternalConfiguration - Octane internal configuration
     * @param baseDomain                  - domain of attachmentList
     */
    public AttachmentList(Octane.OctaneInternalConfiguration octaneInternalConfiguration, String baseDomain) {
        this.octaneHttpClient = octaneInternalConfiguration.getOctaneHttpClient();
        this.attachmentListDomain = baseDomain + ATTACHMENTS_URL;
        entityList = OctaneClassFactory.getImplementation(octaneInternalConfiguration.getOctaneClassFactoryClassName())
                .getEntityList(this.octaneHttpClient, baseDomain, ATTACHMENTS_URL);
    }

    /**
     * getter of attachmentList GetEntities object
     *
     * @return - new attachmentList GetEntities object
     */
    public GetEntities get() {
        return entityList.get();
    }

    /**
     * getter of attachmentList UpdateEntities object ( same functionality as EntityList.UpdateEntities )
     *
     * @return - new attachmentList UpdateEntities object
     */
    public UpdateEntities update() {
        return entityList.update();
    }

    /**
     * getter of attachmentList create object
     *
     * @return - new attachmentList CreateEntities object
     */
    public CreateAttachment create() {
        return new CreateAttachment(octaneHttpClient, attachmentListDomain);
    }

    /**
     * getter of attachmentList DeleteEntities object ( same functionality as EntityList.DeleteEntities )
     *
     * @return - new attachmentList UpdateEntities object
     */
    public DeleteEntities delete() {
        return entityList.delete();
    }

    /**
     * getter of specific attachment ( An Attachments object with specific id )
     *
     * @param entityId -  An Attachments object with specific id
     * @return - An Attachments object with specific id
     */
    public Attachments at(String entityId) {
        return new Attachments(entityId);
    }

    /**
     * This class hold the Attachments objects (handle a unique Attachment model )
     */
    public class Attachments {

        private final String entityId;
        private final EntityList.Entities entities;

        /**
         * Creates a new Attachments object
         *
         * @param entityId - attachment id
         */
        private Attachments(String entityId) {
            entities = entityList.at(entityId);
            this.entityId = entityId;
        }

        /**
         * getter of a new GetEntities Object
         *
         * @return new instance of GetEntities
         */
        public GetEntity get() {
            return entities.get();
        }

        /**
         * getter of a new UpdateEntities Object
         *
         * @return new instance of UpdateEntities
         */
        public UpdateEntity update() {
            return entities.update();
        }

        /**
         * getter of a new DeleteEntities Object
         *
         * @return new instance of DeleteEntities
         */
        public DeleteEntity delete() {
            return entities.delete();
        }


        /**
         * GetEntities GetBinary object
         *
         * @return new instance of GetBinary
         */
        public GetBinaryAttachment getBinary() {
            return new GetBinaryAttachment(octaneHttpClient, attachmentListDomain, entityId);
        }

    }

}


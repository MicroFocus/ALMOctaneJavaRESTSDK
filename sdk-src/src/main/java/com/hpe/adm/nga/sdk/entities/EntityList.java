/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.entities.create.CreateEntities;
import com.hpe.adm.nga.sdk.entities.delete.DeleteEntities;
import com.hpe.adm.nga.sdk.entities.delete.DeleteEntity;
import com.hpe.adm.nga.sdk.entities.get.GetEntities;
import com.hpe.adm.nga.sdk.entities.get.GetEntity;
import com.hpe.adm.nga.sdk.entities.update.UpdateEntities;
import com.hpe.adm.nga.sdk.entities.update.UpdateEntity;
import com.hpe.adm.nga.sdk.manualtests.TestStepList;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;


/**
 * This class represents the entity context and carries out the actual server requests.  It builds the correct URL as
 * appropriate
 */
public class EntityList {

    // private members
    protected final String urlDomain;
    protected final OctaneHttpClient octaneHttpClient;

    // **** public Functions ***

    /**
     * Creates a new EntityList object.  This represents an entity collection
     *
     * @param octaneHttpClient - Http Client
     * @param entityListDomain - Domain Name
     */
    public EntityList(OctaneHttpClient octaneHttpClient, String entityListDomain) {
        this.octaneHttpClient = octaneHttpClient;
        urlDomain = entityListDomain;
    }

    /**
     * getter of an Entities object ( Entities object handle a unique entity
     * model )
     *
     * @param entityId - entity id
     * @return a new Entities object with specific id
     */
    public Entities at(String entityId) {
        return isTestEntity() ? new TestEntities(entityId) : new Entities(entityId);
    }

    /**
     * getter of an GetEntities object of EntityList ( EntityList object handle a
     * collection of entity models )
     *
     * @return a new GetEntities object
     */
    public GetEntities get() {
        return new GetEntities(octaneHttpClient, urlDomain);
    }

    /**
     * getter of an UpdateEntities object of EntityList ( EntityList object handle a
     * collection of entity models )
     *
     * @return a new UpdateEntities object
     */
    public UpdateEntities update() {
        return new UpdateEntities(octaneHttpClient, urlDomain);
    }

    /**
     * getter of an CreateEntities object of EntityList ( EntityList object handle a
     * collection of entity models
     *
     * @return a new CreateEntities object
     */
    public CreateEntities create() {
        return new CreateEntities(octaneHttpClient, urlDomain);
    }

    /**
     * getter of an DeleteEntities object of EntityList ( EntityList object handle a
     * collection of entity models
     *
     * @return a new DeleteEntities object
     */
    public DeleteEntities delete() {
        return new DeleteEntities(octaneHttpClient, urlDomain);
    }

    /**
     * This is hard-coded.  Only manual tests or gherkin tests have a script
     *
     * @return whether this is a test entity that has a script
     */
    private boolean isTestEntity() {
        return urlDomain.endsWith("manual_tests") || urlDomain.endsWith("gherkin_tests");
    }

    // **** Classes ***

    /**
     * This class hold the Entities object(An object that represent one Entity )
     */
    public class Entities {

        protected final String entityId;

        /**
         * Set entityId parameter
         *
         * @param entityId The entity id
         */
        public Entities(String entityId) {
            this.entityId = entityId;
        }

        /**
         * getter of a GetEntities object with specific entity
         *
         * @return The GetEntities object
         */
        public GetEntity get() {
            return new GetEntity(octaneHttpClient, urlDomain, entityId);
        }

        /**
         * getter of a UpdateEntities object with specific entity
         *
         * @return The UpdateEntities object
         */
        public UpdateEntity update() {
            return new UpdateEntity(octaneHttpClient, urlDomain, entityId);
        }

        /**
         * getter of a CreateEntities object with specific entity
         *
         * @return The DeleteEntities object
         */
        public DeleteEntity delete() {
            return new DeleteEntity(octaneHttpClient, urlDomain, entityId);
        }

    }

    /**
     * This class hold the Entities object but as a test for the manipulation of test scripts (An object that represent one Entity )
     */
    public class TestEntities extends Entities {

        /**
         * Set entityId parameter
         *
         * @param entityId The entity id
         */
        public TestEntities(String entityId) {
            super(entityId);
        }

        /**
         * An instance of {@link TestStepList} for access to the script of the test
         *
         * @return Instance of TestStepList
         */
        public TestStepList getTestSteps() {
            return new TestStepList(octaneHttpClient, urlDomain.substring(0, urlDomain.lastIndexOf('/') + 1), entityId);
        }
    }

}

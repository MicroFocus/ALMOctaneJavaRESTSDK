/*
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
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
package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;


/**
 * This class represents the entity context and carries out the actual server requests.  It builds the correct URL as
 * appropriate
 */
public class EntityListService {

    // private members
    private final String urlDomain;
    private final OctaneHttpClient octaneHttpClient;


    // **** public Functions ***

    /**
     * Creates a new EntityListService object.  This represents an entity collection
     *
     * @param octaneHttpClient - Http Client
     * @param entityListDomain - Domain Name
     */
    public EntityListService(OctaneHttpClient octaneHttpClient, String entityListDomain) {

        urlDomain = entityListDomain;
        this.octaneHttpClient = octaneHttpClient;
    }

    /**
     * getter of an Entities object ( Entities object handle a unique entity
     * model )
     *
     * @param entityId - entity id
     * @return a new Entities object with specific id
     */
    Entities at(int entityId) {
        return new Entities(entityId);
    }

    /**
     * getter of an GetEntities object of EntityList ( EntityList object handle a
     * collection of entity models )
     *
     * @return a new GetEntities object
     */
    GetEntities get() {
        return new GetEntities(octaneHttpClient, urlDomain);
    }

    /**
     * getter of an UpdateEntities object of EntityList ( EntityList object handle a
     * collection of entity models )
     *
     * @return a new UpdateEntities object
     */
    UpdateEntities update() {
        return new UpdateEntities(octaneHttpClient, urlDomain);
    }

    /**
     * getter of an CreateEntities object of EntityList ( EntityList object handle a
     * collection of entity models
     *
     * @return a new CreateEntities object
     */
    CreateEntities create() {
        return new CreateEntities(octaneHttpClient, urlDomain);
    }

    /**
     * getter of an DeleteEntities object of EntityList ( EntityList object handle a
     * collection of entity models
     *
     * @return a new DeleteEntities object
     */
    DeleteEntities delete() {
        return new DeleteEntities(octaneHttpClient, urlDomain);
    }


    // **** Classes ***

    /**
     * This class hold the Entities object(An object that represent one Entity )
     */
    public class Entities {

        private int iEntityId = 0;

        /**
         * Set entityId parameter
         *
         * @param entityId The entity id
         */
        public Entities(int entityId) {
            iEntityId = entityId;
        }

        /**
         * getter of a GetEntities object with specific entity
         *
         * @return The GetEntities object
         */
        public GetEntity get() {
            return new GetEntity(octaneHttpClient, urlDomain, iEntityId);
        }

        /**
         * getter of a UpdateEntities object with specific entity
         *
         * @return The UpdateEntities object
         */
        public UpdateEntity update() {
            return new UpdateEntity(octaneHttpClient, urlDomain, iEntityId);
        }

        /**
         * getter of a CreateEntities object with specific entity
         *
         * @return The DeleteEntities object
         */
        public DeleteEntity delete() {
            return new DeleteEntity(octaneHttpClient, urlDomain, iEntityId);
        }

    }

}

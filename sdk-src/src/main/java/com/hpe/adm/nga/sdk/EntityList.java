/*
 *
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
package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

/**
 * <p>
 * Creates a new EntityList object.  This can be used to get one entity (using the {@link #at(int) method}
 * or a collection of entities.
 * </p>
 * <p>
 *     Depending on the HTTP method that is required the correct method should be used
 * </p>
 *
 */
public class EntityList {

	private EntityListService entityListService = null;

    /**
     * Creates a new {@link EntityList} instance with the entity collection name and the client to be used
     *
     * @param octaneHttpClient    - The client that is used for REST communication
     * @param strEntityListDomain - The entity collection name
     */
	public EntityList(OctaneHttpClient octaneHttpClient, String strEntityListDomain) {

		entityListService = new EntityListService(octaneHttpClient, strEntityListDomain);
		
	}

    /**
     * Returns an entity context.  This is the same as setting the context to:
     * <br>
     * <code>[octane_url]/entity/entity_id</code>
     * <br>
     * This does not yet make a call to the server as the HTTP method has not been set
     * @param entityId - The id of the entity
     * @return the entity context that can be used further to carry out the call to the server
     */
	public EntityListService.Entities at(int entityId) {
		return entityListService.at(entityId);
	}

    /**
     * Returns a context that will be used to <code>GET</code> from the server.  Because this is a collection no
     * id is sent.  This is the same  as setting the context to:
     * <br>
     * <code>[octane_url]/entity</code>
     * <br>
     * This does not yet make a call to the server but sets the context
     *
     * @return a context to the entity collection that will be used for GET
     */
	public EntityListService.Get get() {
		
		return entityListService.get();
	}

    /**
     * Returns a context that will be used to <code>PUT</code> from the server.  Because this is a collection no
     * id is sent.  This is the same  as setting the context to:
     * <br>
     * <code>[octane_url]/entity</code>
     * <br>
     * This does not yet make a call to the server but sets the context
     *
     * @return a context to the entity collection that will be used for PUT
     */
	public EntityListService.Update update() {
		
		return entityListService.update();
	}

    /**
     * Returns a context that will be used to <code>POST</code> from the server.  Because this is a collection no
     * id is sent.  This is the same  as setting the context to:
     * <br>
     * <code>[octane_url]/entity</code>
     * <br>
     * This does not yet make a call to the server but sets the context
     *
     * @return a context to the entity collection that will be used for POST
     */
	public EntityListService.Create create() {

		return entityListService.create();
	}

    /**
     * Returns a context that will be used to <code>DELETE</code> from the server.  Because this is a collection no
     * id is sent.  This is the same  as setting the context to:
     * <br>
     * <code>[octane_url]/entity</code>
     * <br>
     * This does not yet make a call to the server but sets the context
     *
     * @return a context to the entity collection that will be used for DELETE
     */
	public EntityListService.Delete delete() {
		return entityListService.delete();
	}


}

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

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

import java.util.Collection;

/**
 *
 * This class hold the entities objects and serve all functionality concern to entities.
 * 
 * @author moris oz
 *
 */
public class EntityList {

	private EntityListService entityListService = null;
	
	
	/**
	 * Creates a new EntityList object 
	 * @param octaneHttpClient - Http Request Factory
	 * @param strEntityListDomain - Domain Name 
	 */
	public EntityList(OctaneHttpClient octaneHttpClient, String strEntityListDomain) {

		entityListService = new EntityListService(octaneHttpClient, strEntityListDomain);
		
	}
	
	/**
	 * getter of an Entities object ( Entities object handle a unique entity model ) 
	 * @param entityId - entity id
	 * @return a new Entities object with specific id
	 */
	public EntityListService.Entities at(int entityId) {
		return entityListService.at(entityId);
	}
	
	/**
	 * getter of an Get object of EntityList ( EntityList object handle a collection of entity models  )
	 * @return a new Get object 
	 */
	public EntityListService.Get get() {
		
		return entityListService.get();
	}
	
	/**
	 * getter of an Update object of EntityList ( EntityList object handle a collection of entity models  )
	 * @return a new Update object 
	 */
	public EntityListService.Update update() {
		
		return entityListService.update();
	}
	
	/**
	 * getter of an Create object of EntityList ( EntityList object handle a collection of entity models
	 * @return a new Create object 
	 */
	public EntityListService.Create create() {

		return entityListService.create();
	}
	/**
	 * getter of an Delete object of EntityList ( EntityList object handle a collection of entity models
	 * @return a new Delete object 
	 */
	public EntityListService.Delete delete() {
		return entityListService.delete();
	}

	/**
	 * TBD - Remove after testing
	 */
	public Collection<EntityModel> testGetEntityModels(String jason)  {

		return entityListService.testGetEntityModels(jason);
	}
}

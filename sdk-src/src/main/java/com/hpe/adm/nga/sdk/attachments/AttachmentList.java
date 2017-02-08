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
package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.EntityListService;
import com.hpe.adm.nga.sdk.OctaneRequest;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

import java.io.InputStream;
import java.util.Collection;

/**
 *<p>
 * The object that represents attachments in the REST API.  Attachments contain both the binary data and the metadata
 * surrounding them. See the REST API documentation for further information as to how to use attachments.
 * </p>
 * <p>
 *     Attachments have different functionality depending on whether they are being created, updated, read or deleted.
 *     <br/>
 *
 * </p>
 */
public class AttachmentList {

	private EntityListService entityListService = null;
	
	/**
	 * Creates a new AttachmentList object
	 * @param octaneHttpClient - Octane request factory
	 * @param attachmentListDomain - domain of attachmentList
	 */
	public AttachmentList(OctaneHttpClient octaneHttpClient, String attachmentListDomain) {
		
		entityListService = new EntityListService(octaneHttpClient, attachmentListDomain);
	}
	
	/**
	 * getter of AttachmentList Get object
	 * @return - new AttachmentList Get object
	 */
	public Get get() {
		return new Get();
	}
	
	/**
	 * 	getter of AttachmentList Update object ( same functionality as EntityListService.Update )
	 * @return - new AttachmentList Update object
	 */
	public EntityListService.Update update() {
		return  entityListService.update();
	}
	
	/**
	 * getter of AttachmentList create object
	 * @return - new AttachmentList Create object
	 */
	public Create create() {
		return new Create();
	}
	
	/**
	 * 	getter of AttachmentList Delete object ( same functionality as EntityListService.Delete )
	 * @return - new AttachmentList Update object
	 */
	public EntityListService.Delete delete() {
		return entityListService.delete();
	}
	
	/**
	 * getter of specific attachment ( An Attachments object with specific id )
	 * @param entityId -  An Attachments object with specific id
	 * @return - An Attachments object with specific id
	 */
	public Attachments at(int entityId) {
		return new Attachments(entityId);
	}
	
	/**
	 * This class hold the Get objects and serve all functions concern to REST
	 * Get.
	 *
	 */
	public class Get extends OctaneRequest<Collection<EntityModel>> {
		
		/**
		 * Request Get Execution 
		 * return collection of entities models
		 */
		@Override
		public Collection<EntityModel> execute() throws RuntimeException{
			
			return entityListService.get().execute();
		}
		
	}
	
	/**
	 * This class hold the Create objects and serve all functions concern to REST
	 * Get.
	 *
	 */
	public class Create extends OctaneRequest<Collection<EntityModel>> {
		
		
		private String contentType = "";
		private String contentName = "";
		private InputStream inputStream = null;
		private Collection<EntityModel> colEntities = null;
		
		/**
		 * Request Post Execution with Multipart content type
		 * return a collection of entities models that have been created
		 */
		@Override
		public Collection<EntityModel> execute() throws RuntimeException {
			
			return entityListService.create().executeMultipart(colEntities,inputStream,contentType,contentName); 
		}
		
		/**
		 * Setter of new entities to create and file to upload
		 * @param entities - new entities to create
		 * @param stream - file stream
		 * @return - An Object with new data
		 */
		public Create entities(Collection<EntityModel> entities, InputStream stream, String type, String name ) {

			colEntities = entities;
			inputStream = stream;
			contentType = type;
			contentName = name;
			
			return this;
		}
		
				
	}
	
	/**
	 * This class hold the Attachments objects (handle a unique Attachment model )
	 *
	 */
	public class Attachments {
		
		private int iEntityId= 0;
		
		/**
		 * Creates a new Attachments object
		 * @param entityId - attachment id
		 */
		public Attachments(int entityId){
			iEntityId = entityId;
		}
		
		/**
		 * getter of a new Get Object
		 * @return new instance of Get
		 */
		public Get get() {
			return new Get();
		}
		
		/**
		 * getter of a new Update Object
		 * @return new instance of Update
		 */
		public Update update() {
			return new Update();
		}
		
		/**
		 * getter of a new Delete Object
		 * @return new instance of Delete
		 */
		public Delete delete() {
			return new Delete();
		}
		
		
		/**
		 * Get GetBinary object
		 * @return new instance of GetBinary
		 */
		public GetBinary getBinary() {
			return new GetBinary();
		}
		
		/**
		 * This class hold the GetBinary objects (handle the binary data of a unique Attachment model )
		 *
		 */
		public class GetBinary extends OctaneRequest<InputStream> {
			
			/**
			 * 
			 * Get Request execution of binary data
			 * return a stream with binary data
			 */		
			@Override
			public InputStream execute() throws RuntimeException{

				return entityListService.at(iEntityId).get().executeBinary();
			}
		}
		
		/**
		 *  This class hold the Get objects
		 *
		 */
		public class Get extends OctaneRequest<EntityModel> {
			
			/**
			 * 
			 * Get Request execution ( unique attachment )
			 * return a new entity model 
			 */				
			@Override
			public EntityModel execute() throws RuntimeException {

				return entityListService.at(iEntityId).get().execute();
			
			}
			
		}
		
		/**
		 *  This class hold the Update objects
		 *
		 */
		public class Update extends OctaneRequest<EntityModel> {
			
			EntityModel entityModel = null;
			
			/**
			 * 
			 * Put Request execution ( unique attachment )
			 * return a new entity model 
			 */		
			@Override
			public EntityModel execute() throws RuntimeException {
				return entityListService.at(iEntityId).update().entity(entityModel).execute();
			}
			
			/**
			 * Getter of an Update object with new Entity Model
			 * @param entModel
			 * @return -an Update object with new Entity Model
			 */
			public Update entity(EntityModel entModel) {
				
				entityModel = entModel;
				return this;
				
			}
		}
		
		/**
		 * This class hold the Delete objects
		 *
		 */
		public  class Delete extends OctaneRequest<EntityModel> {
			
			/**
			 * 
			 * Delete Request execution ( unique attachment )
			 * return null
			 */		
			@Override
			public EntityModel execute() throws RuntimeException {
				return entityListService.at(iEntityId).delete().execute();
			}
		}	
	  }
	
}	


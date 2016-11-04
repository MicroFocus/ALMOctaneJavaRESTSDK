package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.EntityListService;
import com.hpe.adm.nga.sdk.NGARequest;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.network.HttpRequestFactory;

import java.io.InputStream;
import java.util.Collection;

/**
 * This class hold the AttachmentList objects
 * Created by Moris on 23/02/2016.
 */
public class AttachmentList {

	private EntityListService entityListService = null;
	
	/**
	 * Creates a new AttachmentList object
	 * @param reqFactory - NGA request factory 
	 * @param attachmentListDomain - domain of attachmentList
	 */
	public AttachmentList(HttpRequestFactory reqFactory, String attachmentListDomain) {
		
		entityListService = new EntityListService(reqFactory, attachmentListDomain);
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
	 * @author Moris oz
	 *
	 */
	public class Get extends NGARequest<Collection<EntityModel>> {
		
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
	 * @author Moris oz
	 *
	 */
	public class Create extends NGARequest<Collection<EntityModel>> {
		
		
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
	 * @author Moris oz
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
		 * @return
		 */
		public Get get() {
			return new Get();
		}
		
		/**
		 * getter of a new Update Object
		 * @return
		 */
		public Update update() {
			return new Update();
		}
		
		/**
		 * getter of a new Delete Object
		 * @return
		 */
		public Delete delete() {
			return new Delete();
		}
		
		
		/**
		 * Get GetBinary object
		 * @return
		 */
		public GetBinary getBinary() {
			return new GetBinary();
		}
		
		/**
		 * This class hold the GetBinary objects (handle the binary data of a unique Attachment model )
		 * @author Moris oz
		 *
		 */
		public class GetBinary extends NGARequest<InputStream> {
			
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
		 * @author moris oz
		 *
		 */
		public class Get extends NGARequest<EntityModel> {
			
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
		 * @author moris oz
		 *
		 */
		public class Update extends NGARequest<EntityModel> {
			
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
		 * @author moris oz
		 *
		 */
		public  class Delete extends NGARequest<EntityModel> {
			
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


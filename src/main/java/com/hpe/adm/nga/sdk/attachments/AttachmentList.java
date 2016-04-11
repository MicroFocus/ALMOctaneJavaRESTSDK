package com.hpe.adm.nga.sdk.attachments;


import com.hpe.adm.nga.sdk.NGARequest;
import com.hpe.adm.nga.sdk.model.EntityModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.json.JSONException;

/**
 * Created by Moris on 23/02/2016.
 */
public class AttachmentList {
	
	
	public Get get() {
		return null;
	}
		
	public Create create() {
		return null;
	}
			
	
	public Attachments at(int entityId) {
		return null;
	}
	
	public class Get extends NGARequest<Collection<EntityModel>> {
		
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {
			
			return null;
		}
		
	}
	
	public class Create extends NGARequest<Collection<EntityModel>> {
		
	
		
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {
			
			return null;
		}
		
		public Create entities(Collection<EntityModel> entities,String file) {

			return null;
		}
		
				
	}
	
	public class Attachments {
	
		
		public Get get() {
			return null;
		}

		public Update update() {
			return null;
		}

		public Delete delete() {
			return null;
		}
		
		// Get Binary Data
		public GetBinary getBinary() {
			return null;
		}
		
		public class GetBinary extends NGARequest<InputStream> {
			
					
			@Override
			public InputStream execute() throws JSONException, IOException {

				return null;
			}
		}
		
		public class Get extends NGARequest<EntityModel> {
			
						
			@Override
			public EntityModel execute() throws JSONException, IOException {

				return null;
			
			}
			
		}
		
		public class Update extends NGARequest<EntityModel> {
			
						
			
			@Override
			public EntityModel execute() throws IOException, JSONException {
				return null;
			}

			public Update entity(EntityModel entModel) {
				
				return null;
				
			}
		}

		public  class Delete extends NGARequest<EntityModel> {
			@Override
			public EntityModel execute() throws IOException, JSONException {
				return null;
			}
		}	
	  }
	
}	


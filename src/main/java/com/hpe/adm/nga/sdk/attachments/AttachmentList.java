package com.hpe.adm.nga.sdk.attachments;


import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGARequest;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.EntityList.Entities;
import com.hpe.adm.nga.sdk.EntityList.Get;
import com.hpe.adm.nga.sdk.EntityList.Update;
import com.hpe.adm.nga.sdk.EntityList.Entities.Delete;
import com.hpe.adm.nga.sdk.model.EntityModel;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Moris on 23/02/2016.
 */
public class AttachmentList {

	
	public AttachmentList(String strAttachments) {
		
		
	}
	
	public Get get() {
		return new Get();
	}
	
		
	public Update update() {
		return null;
	}
	
	public Attachments at(int entityId) {
		return new Attachments();
	}
	
	public static class Get extends NGARequest<Collection<EntityModel>> {
		
		@Override
		public Collection<EntityModel> execute() throws IOException, JSONException {
			
						
			return null;
		}

		public Get addFields(String... fields) {
			return this;
		}

		public Get limit(int limit) {
			return this;
		}

		public Get offset(int offset) {
			return this;
		}

		public Get addOrderBy(String orderBy, boolean asc) {
			return this;
		}

		public Get query(Query query) {
			return this;
		}
	}
	
	public static class Update extends NGARequest<Collection<EntityModel>> {
		@Override
		public Collection<EntityModel> execute() {
			return null;
		}

		public Update query(Query query) {
			return this;
		}

		public Update entity(EntityModel entityModel) {
			return this;
		}

		public Update entities(Collection<EntityModel> entities) {
			return this;
		}
	}
	
	public static class Attachments {
		public Get get() {
			return new Get();
		}

		public Update update() {
			return new Update();
		}

		public Delete delete() {
			return new Delete();
		}
		
		// Get Binary Data
		public Get getBinary() {
					return null;
		}
				
		public static class Get extends NGARequest<EntityModel> {
			@Override
			public EntityModel execute() {
				return null;
			}

			public Get addFields(String... fields) {
				return this;
			}
		}

		public static class Update extends NGARequest<EntityModel> {
			@Override
			public EntityModel execute() {
				return null;
			}

			public Update entity(EntityModel entityModel) {
				return this;
			}
		}

		public static class Delete extends NGARequest<EntityModel> {
			@Override
			public EntityModel execute() {
				return null;
			}
		}
	}
	
	
}

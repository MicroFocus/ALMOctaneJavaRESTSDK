package java.com.hpe.adm.nga.sdk;

import java.com.hpe.adm.nga.sdk.model.EntityModel;

import java.util.Collection;

/**
 * Created by brucesp on 22/02/2016.
 */
public  class EntityList {

	
	// **** Functions  *** 
	public Entities at(int entityId) {
		return new Entities();
	}

	
	public Get get() {
			return new Get();
		}

	public Update update() {
			return new Update();
		}

	public Create create() {
			return new Create();
		}

	public Delete delete() {
			return new Delete();
		}
	
	// **** Classes  *** 
	public static class Get extends NGARequest<Collection<EntityModel>> {
			@Override
			public Collection<EntityModel> execute() {
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

	public static class Create extends NGARequest<Collection<EntityModel>> {
			@Override
			public Collection<EntityModel> execute() {
				return null;
			}

			public Create entities(Collection<EntityModel> entities) {
				return this;
			}
		}

	public static class Delete extends NGARequest<Collection<EntityModel>> {
			@Override
			public Collection<EntityModel> execute() {
				return null;
			}

			public Delete query(Query query) {
				return this;
			}
		}
	

	public static class Entities {
		public Get get() {
			return new Get();
		}

		public Update update() {
			return new Update();
		}

		public Delete delete() {
			return new Delete();
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

package com.hpe.adm.nga.sdk;

import java.util.Collection;

/**
 * Created by brucesp on 22/02/2016.
 */
public final class Entity {

	public EntityList entityList() {
		return new EntityList();
	}

	public Entities entities(int entityId) {
		return new Entities();
	}

	public static class EntityList {
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

		public static class Get extends NGARequest<Collection<com.hpe.adm.nga.sdk.model.Entity>> {
			@Override
			public Collection<com.hpe.adm.nga.sdk.model.Entity> execute() {
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

		public static class Update extends NGARequest<Collection<com.hpe.adm.nga.sdk.model.Entity>> {
			@Override
			public Collection<com.hpe.adm.nga.sdk.model.Entity> execute() {
				return null;
			}

			public Update query(Query query) {
				return this;
			}

			public Update entity(com.hpe.adm.nga.sdk.model.Entity entity) {
				return this;
			}

			public Update entities(Collection<com.hpe.adm.nga.sdk.model.Entity> entities) {
				return this;
			}
		}

		public static class Create extends NGARequest<Collection<com.hpe.adm.nga.sdk.model.Entity>> {
			@Override
			public Collection<com.hpe.adm.nga.sdk.model.Entity> execute() {
				return null;
			}

			public Create entity(com.hpe.adm.nga.sdk.model.Entity entity) {
				return this;
			}

			public Create entities(Collection<com.hpe.adm.nga.sdk.model.Entity> entities) {
				return this;
			}
		}

		public static class Delete extends NGARequest<Collection<com.hpe.adm.nga.sdk.model.Entity>> {
			@Override
			public Collection<com.hpe.adm.nga.sdk.model.Entity> execute() {
				return null;
			}

			public Delete query(Query query) {
				return this;
			}
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

		public static class Get extends NGARequest<com.hpe.adm.nga.sdk.model.Entity> {
			@Override
			public com.hpe.adm.nga.sdk.model.Entity execute() {
				return null;
			}

			public Get addFields(String... fields) {
				return this;
			}
		}

		public static class Update extends NGARequest<com.hpe.adm.nga.sdk.model.Entity> {
			@Override
			public com.hpe.adm.nga.sdk.model.Entity execute() {
				return null;
			}

			public Update entity(com.hpe.adm.nga.sdk.model.Entity entity) {
				return this;
			}
		}

		public static class Delete extends NGARequest<com.hpe.adm.nga.sdk.model.Entity> {
			@Override
			public com.hpe.adm.nga.sdk.model.Entity execute() {
				return null;
			}
		}
	}
}

package com.hpe.adm.nga.sdk.metadata;

import com.hpe.adm.nga.sdk.NGARequest;

import java.util.Collection;

/**
 * Created by brucesp on 23/02/2016.
 */
public class Metadata {

	public Entity entities(){
		return new Entity();
	}

	public Entity entities(String...entities){
		return new Entity();
	}

	public Field fields(){
		return new Field();
	}

	public Field fields(String...entities){
		return new Field();
	}

	public static class Entity extends NGARequest<Collection<EntityMetadata>> {
		@Override
		public Collection<EntityMetadata> execute() {
			return null;
		}
	}

	public static class Field extends NGARequest<Collection<FieldMetadata>> {
		@Override
		public Collection<FieldMetadata> execute() {
			return null;
		}
	}

}

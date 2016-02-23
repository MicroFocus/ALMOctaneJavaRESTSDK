package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;

import java.util.UUID;

/**
 * Created by brucesp on 22/02/2016.
 */
public class NGA {

	public Entity entity(String entityName) {
		return null;
	}

	public Metadata metadata() {
		return null;
	}

	public static class Builder {

		public Builder(Authorisation authorisation) {

		}

		public Builder sharedSpace(long ssID) {
			return this;
		}

		public Builder sharedSpace(UUID ssID) {
			return this;
		}

		public Builder workSpace(long id) {
			return this;
		}

		public NGA build() {
			return new NGA();
		}
	}

}

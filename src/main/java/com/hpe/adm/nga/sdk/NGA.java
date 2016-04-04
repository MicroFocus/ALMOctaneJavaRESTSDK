package java.com.hpe.adm.nga.sdk;

import java.com.hpe.adm.nga.sdk.attachments.Attachments;
import java.com.hpe.adm.nga.sdk.authorisation.Authorisation;
import java.com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import java.com.hpe.adm.nga.sdk.metadata.Metadata;

import java.util.UUID;


/**
 * Created by brucesp on 22/02/2016.
 */
public class NGA {

	public EntityList entityList(String entityName) {
		return null;
	}

	public Metadata metadata() {
		return null;
	}

	public Attachments attachments() {
		return null;
	}

	public static class Builder {

		public Builder(BasicAuthorisation basicAuthorisation) {

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

		public Builder Server(String host, int port) {
			return this;
		}

		public Builder Server(String host) {
			return Server(host, 8080);
		}

		public NGA build() {
			return new NGA();
		}
	}

}

package  com.hpe.adm.nga.sdk.authorisation;

import com.google.api.client.util.GenericData;
import com.hpe.adm.nga.sdk.network.HttpRequest;

/**
 * Created by brucesp on 23/05/2016.
 */
public class ClientAuthorisation implements Authorisation {

	private final String clientId;
	private final String clientSecret;

	public ClientAuthorisation(final String clientId, final String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@Override
	public void executeAuthorisation(HttpRequest request) {
		final GenericData genericData = new GenericData();
		genericData.put("client_id", clientId);
		genericData.put("client_secret", clientSecret);
		request.setContent(genericData);
	}
}

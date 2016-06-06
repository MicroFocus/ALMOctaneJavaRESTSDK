package  com.hpe.adm.nga.sdk.authorisation;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;

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
		request.setContent(new JsonHttpContent(new JacksonFactory(), genericData));
	}
}

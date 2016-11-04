package  com.hpe.adm.nga.sdk.authorisation;

import com.google.api.client.util.GenericData;
import com.hpe.adm.nga.sdk.network.HttpRequest;

/**
 * Created by brucesp on 23/05/2016.
 */
public class UserAuthorisation implements Authorisation {

	private final String userName;
	private final String password;

	public UserAuthorisation(final String userName, final String password) {
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void executeAuthorisation(HttpRequest request) {
		GenericData genericData = new GenericData();
		genericData.put("user", userName);
		genericData.put("password", password);
		request.setContent(genericData);
	}
}

package  com.hpe.adm.nga.sdk.authorisation;

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
		request.setUserAuthentication(userName, password);
	}
}

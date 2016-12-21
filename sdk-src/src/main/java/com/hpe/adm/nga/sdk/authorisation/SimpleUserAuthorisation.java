package  com.hpe.adm.nga.sdk.authorisation;

/**
 * Created by brucesp on 23/05/2016.
 */
public class SimpleUserAuthorisation extends UserAuthorisation {

	private final String userName;
	private final String password;

	public SimpleUserAuthorisation(final String userName, final String password) {
		this.userName = userName;
		this.password = password;
	}

	protected String getUserName() {
		return userName;
	}

	protected String getPassword() {
		return password;
	}
}

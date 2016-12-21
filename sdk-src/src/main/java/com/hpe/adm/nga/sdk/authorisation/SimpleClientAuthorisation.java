package  com.hpe.adm.nga.sdk.authorisation;

/**
 * Created by brucesp on 23/05/2016.
 */
public class SimpleClientAuthorisation extends ClientAuthorisation{

	private final String clientId;
	private final String clientSecret;

	public SimpleClientAuthorisation(final String clientId, final String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	protected String getClientId() {
		return clientId;
	}

	protected String getClientSecret() {
		return clientSecret;
	}
}

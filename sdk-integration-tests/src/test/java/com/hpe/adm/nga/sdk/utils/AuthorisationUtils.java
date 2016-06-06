package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.authorisation.ClientAuthorisation;
import com.hpe.adm.nga.sdk.authorisation.UserAuthorisation;

/**
 * Created by brucesp on 06/06/2016.
 */
public class AuthorisationUtils {

	public static Authorisation getAuthorisation(){

		final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
		String authorisationType = configuration.getString("sdk.authorisationType");
		if (authorisationType == null || authorisationType.isEmpty() || authorisationType.equals("userpass")) {
			return new UserAuthorisation(configuration.getString("sdk.username"), configuration.getString("sdk.password"));
		} else if (authorisationType.equals("client")) {
			return new ClientAuthorisation(configuration.getString("sdk.clientId"), configuration.getString("sdk.clientSecret"));
		} else {
			throw new IllegalArgumentException("Authorisation not set!");
		}
	}
}

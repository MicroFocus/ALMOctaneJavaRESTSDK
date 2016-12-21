package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.authorisation.SimpleClientAuthorisation;
import com.hpe.adm.nga.sdk.authorisation.SimpleUserAuthorisation;

/**
 * Created by brucesp on 06/06/2016.
 */
public class AuthorisationUtils {

	public static Authorisation getAuthorisation(){

		final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
		String authorisationType = configuration.getString("sdk.authorisationType");
		if (authorisationType == null || authorisationType.isEmpty() || authorisationType.equals("userpass")) {
			return new SimpleUserAuthorisation(configuration.getString("sdk.username"), configuration.getString("sdk.password"));
		} else if (authorisationType.equals("client")) {
			return new SimpleClientAuthorisation(configuration.getString("sdk.clientId"), configuration.getString("sdk.clientSecret"));
		} else {
			throw new IllegalArgumentException("Authorisation not set!");
		}
	}
}

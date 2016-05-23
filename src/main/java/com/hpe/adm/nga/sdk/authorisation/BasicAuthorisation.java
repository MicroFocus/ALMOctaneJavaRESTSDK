package com.hpe.adm.nga.sdk.authorisation;

import com.google.api.client.http.HttpRequest;

/**
 * BasicAuthorisation object - Hold all functionality concern Basic Authorization.
 * @author Moris oz
 *
 */
public class BasicAuthorisation implements Authorisation{

	private final String userName;
	private final String password;

	public BasicAuthorisation(final String userName, final String password){
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void executeAuthorisation(HttpRequest request) {
		request.getHeaders().setBasicAuthentication(userName, password);
	}

}

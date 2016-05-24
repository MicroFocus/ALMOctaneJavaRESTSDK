package com.hpe.adm.nga.sdk.authorisation;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.GenericData;

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
		request.setContent(new JsonHttpContent(new JacksonFactory(), genericData));
	}
}

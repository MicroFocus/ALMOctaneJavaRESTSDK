package com.hpe.adm.nga.sdk.authorisation;

import com.google.api.client.http.HttpRequest;

/**
 * Interface of Authorization , hold contract functions.
 * @author Moris Oz
 *
 */
public interface Authorisation {
	void executeAuthorisation(HttpRequest request);
}



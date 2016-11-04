package com.hpe.adm.nga.sdk.authorisation;

import com.hpe.adm.nga.sdk.network.HttpRequest;

/**
 * Interface of Authorization , hold contract functions.
 * @author Moris Oz
 *
 */
public interface Authorisation {
	void executeAuthorisation(HttpRequest request);
}



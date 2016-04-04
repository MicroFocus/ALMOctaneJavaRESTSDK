package java.com.hpe.adm.nga.sdk.exception;

import java.com.hpe.adm.nga.sdk.NGARequest;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Get;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Update;
import java.com.hpe.adm.nga.sdk.NGAError;
import java.com.hpe.adm.nga.sdk.exception.NgaException.Error;

import java.util.Collection;

/**
 * Created by brucesp on 23/02/2016.
 */
public class NgaException extends RuntimeException {

	public NGAError getError();
}


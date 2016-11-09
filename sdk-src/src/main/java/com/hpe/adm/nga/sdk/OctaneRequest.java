package com.hpe.adm.nga.sdk;

/**
 * Octane request Interface
 * @author Moris oz
 *
 * @param <T>
 */
public abstract class OctaneRequest<T> {

	public abstract T execute() throws RuntimeException;

}

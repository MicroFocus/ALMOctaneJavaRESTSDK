package main.java.com.hpe.adm.nga.sdk;

/**
 * NGA request Interface 
 * @author Moris oz
 *
 * @param <T>
 */
public abstract class NGARequest<T> {

	public abstract T execute() throws RuntimeException;

}

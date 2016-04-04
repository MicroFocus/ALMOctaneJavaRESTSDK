package java.com.hpe.adm.nga.sdk.model;

/**
 * Created by brucesp on 22/02/2016.
 */
public interface IFieldModel<T> {
	
	public T getValue(String name);
	public void setValue(String name,T value);
	
}

package main.java.com.hpe.adm.nga.sdk.model;

/**
 * This class hold the StringFieldModel objects and serve as a string type FieldModel data holder 
 * 
 * @author moris oz
 *
 */
public class StringFieldModel implements FieldModel<String> {
	
	//Private 
	private String name="";
	private String value="";
	
	/**
	 * Creates a new StringFieldModel object
	 * 
	 * @param newName - Field name
	 * @param newValue - Field Value
	 */
	public StringFieldModel(String newName,String newValue){
		
		setValue(newName,newValue);
	}
	
	/**
	 * get value
	 */
	public String getValue()	{
		return value;
	};
	
	/**
	 * get name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * set value - name/value
	 */
	public void setValue(String newName,String newValue){
		
		name = newName;
		value = newValue;
	};
}

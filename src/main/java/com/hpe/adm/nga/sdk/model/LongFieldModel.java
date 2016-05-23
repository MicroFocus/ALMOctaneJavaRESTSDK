package main.java.com.hpe.adm.nga.sdk.model;

/**
 * This class hold the LongFieldModel objects and serve as a long type FieldModel data holder 
 * 
 * @author moris oz
 *
 */
public class LongFieldModel implements FieldModel<Long> {
	
	//Private 
		private String name = "";
		private Long value = Long.valueOf(0);
		
		/**
		 * Creates a new LongFieldModel object
		 * 
		 * @param newName - Field name
		 * @param newValue - Field Value
		 */
		public LongFieldModel(String newName,Long newValue){
			
			setValue(newName,newValue);
		}
		
		/**
		 * get value
		 */
		public Long getValue()	{
			return value;
		};
		
		/**
		 * get Field's name
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * set Field's name/value
		 */
		public void setValue(String newName,Long newValue){
			
			name = newName;
			value = newValue;
		};
	
	
}

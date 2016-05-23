package main.java.com.hpe.adm.nga.sdk.model;


/**
 * This class hold the ReferenceErrorModel objects and serve as a ReferenceError type FieldModel data holder 
 * 
 * @author moris oz
 *
 */
public class ReferenceErrorModel implements FieldModel<ReferenceErrorModel.ReferenceError> {
	
	//Private 
	ReferenceError refValue;
	String refName;
	
	/**
	 * Creates a new ReferenceErrorModel object
	 * 
	 * @param newName - Field name
	 * @param newValue - Field Value
	 */
	public ReferenceErrorModel(String name,ReferenceError value){
		
		setValue( name, value);
	}
	
	/**
	 * get value
	 */
	public ReferenceError getValue(){
		return refValue ;
	}
	
	/**
	 * get name
	 */
	public String getName(){
		return refName;
	}
	
	/**
	 * set value
	 */
	public void setValue(String name,ReferenceError value){
		
		refValue = value;
		refName = name;
	}
	
	/**
	 * data structure for referenceError
	 * @author moris oz
	 *
	 */
	public static class ReferenceError  {
		
		private long entity_id;  // Variable name must reflect Rest variable name ( Gson().fromJson )
		private String entity_type; // Variable name must reflect Rest variable name ( Gson().fromJson )
		
		/**
		 * Creates a new ReferenceError object
		 * 
		 * @param Id - ReferenceError id
		 * @param type - ReferenceError type
		 */
		public ReferenceError(long Id,String type){
			
			setValues(Id,type);
		}
				
		/**
		 * set value
		 * @param id
		 * @param type
		 */
		public void setValues(long id,String type){
			
			entity_id = id;
			entity_type = type;
		}
		
		/**
		 * get id
		 * @return
		 */
		public long getId(){
			return entity_id;
		}
		
		/**
		 * get type
		 * @return
		 */
		public String gettype(){
			return entity_type;
		}
		
	}
	
}

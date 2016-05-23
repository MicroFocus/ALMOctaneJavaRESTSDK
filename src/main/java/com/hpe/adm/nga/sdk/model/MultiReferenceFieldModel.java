package main.java.com.hpe.adm.nga.sdk.model;

import java.util.Collection;


/**
 * This class hold the MultiReferenceFieldModel objects and serve as a multi reference type FieldModel data holder 
 * 
 * @author moris oz
 *
 */
public class MultiReferenceFieldModel implements FieldModel<Collection<EntityModel>> {
	
	
	
		//Private 
		private String name = "";
		private Collection<EntityModel> data = null;
		
		/**
		 * Creates a new MultiReferenceFieldModel object
		 * 
		 * @param newName - Field name
		 * @param newValue - Field Value
		 */
		public MultiReferenceFieldModel(String newName,Collection<EntityModel> value){
			
			setValue(newName,value);
		}
		
		/**
		 * get value
		 */
		public Collection<EntityModel> getValue(){
			return data;
		};
		
		/**
		 * get name
		 */
		public String getName(){
			
			return name;
		}
		
		/**
		 * set name
		 * @param newName - new field name
		 */
		public void setName(String newName){
			
			name = newName;
		}
		
		/**
		 * set name/value;
		 */
		public void setValue(String newName,Collection<EntityModel> value){
			
			name = newName;
			data = value;
		};
		
				
		
}

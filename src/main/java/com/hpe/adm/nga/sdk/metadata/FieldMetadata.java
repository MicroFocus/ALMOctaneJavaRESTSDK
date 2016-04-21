package com.hpe.adm.nga.sdk.metadata;

/**
 * This class hold the field metadata object and serve all functionality concern to metadata of fields
 * @author Moris oz
 *
 */
public class FieldMetadata {
	
	private String name;
	private String label;
	private String entity_name;
	private boolean filterable;
	private boolean sortable;
	private boolean returned_by_default;
	private String field_type;
	private boolean required;
	private int max_length;
	private boolean unique;
	private boolean editable;
	private String sanitization;
	private FieldTypedata field_type_data;
	
	
	public enum FieldType {
	    Integer, 
	    Long, 
	    Boolean, 
	    DateTime,
	    Date, 
	    String, 
	    Memo, 
	    Object,
	    Refrence
	}
	
	/**
	 * This class hold the data structure of Field Target
	 * @author Moris Oz
	 *
	 */
	public class FieldTarget {
		
		String[] types;
		String type;
		String logical_name;
		
		public String[] getTypes(){return types;};
		public String  getType(){return type;};
		public String  logicalName(){return logical_name;};
		
		
	}
	
	/**
	 * This class hold the data structure of Field Field Type data
	 * @author Moris Oz
	 *
	 */
	public class FieldTypedata {
		
		private boolean multiple;
		private FieldTarget target;
		
		public boolean isMultiple(){return multiple;};
		public FieldTarget getTarget(){return target;};
		
	}
	
	/**
	 * get FieldMetadata's name
	 * @return
	 */
	public String getName(){return name;};
	
	/**
	 * get FieldMetadata's Entity Name
	 * @return
	 */
	public String getEntityName(){return entity_name;};
	
	/**
	 * get FieldMetadata's Filterable state
	 * @return
	 */
	public boolean isFilterable(){return filterable;};
	
	/**
	 * get FieldMetadata's Editable state
	 * @return
	 */
	public boolean isEditable(){return editable;};
	
	/**
	 * get FieldMetadata's ReturnByDeafault state
	 * @return
	 */
	public boolean isReturnByDeafault(){return returned_by_default;};
	
	/**
	 * get FieldMetadata's Label 
	 * @return
	 */
	public String getLabel(){return label;};
	
	/**
	 * get FieldMetadata's Sortable state 
	 * @return
	 */
	public boolean isSortable(){return sortable;};
	
	/**
	 * get FieldMetadata's Required state 
	 * @return
	 */
	public boolean isRequired(){return required;};
	
	/**
	 * get FieldMetadata's Sanitization state 
	 * @return
	 */
	public String getSanitization(){return sanitization;};
	
	/**
	 * get FieldMetadata's Unique state 
	 * @return
	 */
	public boolean isUnique(){return unique;};
	
	/**
	 * get FieldMetadata's field Type
	 * @return
	 */
	public String getFieldType(){return field_type;};
	
	/**
	 * get FieldMetadata's Field Type data
	 * @return
	 */
	public FieldTypedata getFieldTypedata(){return null;};
	
	/**
	 * get FieldMetadata's Max Length
	 * @return
	 */
	public int getMaxLength(){return max_length;};
	
}

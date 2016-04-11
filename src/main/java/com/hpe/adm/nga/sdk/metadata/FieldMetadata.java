package com.hpe.adm.nga.sdk.metadata;

/**
 * Created by brucesp on 23/02/2016.
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
	
	
	public class FieldTarget {
		
		String[] types;
		String type;
		String logical_name;
		
		public String[] getTypes(){return types;};
		public String  getType(){return type;};
		public String  logicalName(){return logical_name;};
		
		
	}
	
	public class FieldTypedata {
		
		private boolean multiple;
		private FieldTarget target;
		
		public boolean isMultiple(){return multiple;};
		public FieldTarget getTarget(){return target;};
		
	}
	
	public String getName(){return name;};
	public String getEntityName(){return entity_name;};
	public boolean isFilterable(){return filterable;};
	public boolean isEditable(){return editable;};
	public boolean isReturnByDeafault(){return returned_by_default;};
	public String getLabel(){return label;};
	public boolean isSortable(){return sortable;};
	public boolean isRequired(){return required;};
	public String getSanitization(){return sanitization;};
	public boolean isUnique(){return unique;};
	public String getFieldType(){return field_type;};
	public FieldTypedata getFieldTypedata(){return null;};
	public int getMaxLength(){return max_length;};
	
}

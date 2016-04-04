package com.hpe.adm.nga.sdk.metadata;

/**
 * Created by brucesp on 23/02/2016.
 */
public class FieldMetadata {
	
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
		
		public String[] getTypes(){return null;};
		public String  getType(){return null;};
		public String  logicalName(){return null;};
		
		
	}
	
	public class FieldTypedata {
		
		public boolean isMultiple(){return false;};
		public FieldTarget getTarget(){return null;};
		
	}
	
	public String getName(){return null;};
	public String getEntityName(){return null;};
	public boolean isFilterable(){return false;};
	public boolean isEditable(){return false;};
	public boolean isReturnByDeafault(){return false;};
	public String getLabel(){return null;};
	public boolean isSortable(){return false;};
	public boolean isRequired(){return false;};
	public String getSanitization(){return null;};
	public boolean isUnique(){return false;};
	public FieldType getFieldType(){return null;};
	public FieldTypedata getFieldTypedata(){return null;};
	public int getMaxLength(){return 0;};
	
}

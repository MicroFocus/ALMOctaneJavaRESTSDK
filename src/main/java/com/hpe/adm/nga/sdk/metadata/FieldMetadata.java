package java.com.hpe.adm.nga.sdk.metadata;

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
		
		public String[] getTypes();
		public String  getType();
		public String  logicalName();
		
		
	}
	
	public class FieldTypedata {
		
		public boolean isMultiple();
		public FieldTarget getTarget();
		
	}
	
	public String getName();
	public String getEntityName();
	public boolean isFilterable();
	public boolean isEditable();
	public boolean isReturnByDeafault();
	public String getLabel();
	public boolean isSortable();
	public boolean isRequired();
	public String getSanitization();
	public boolean isUnique();
	public FieldType getFieldType();
	public FieldTypedata getFieldTypedata();
	public int getMaxLength();
	
}

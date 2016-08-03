package com.hpe.adm.nga.sdk.metadata;

import com.google.gson.annotations.SerializedName;

/**
 * This class hold the field metadata object and serve all functionality concern to metadata of fields
 * @author Moris oz
 *
 */
public class FieldMetadata {
	
	//Enums
	public enum FieldType {
		@SerializedName("integer")
		Integer, 
		@SerializedName("float")
		Float,
		@SerializedName("boolean")
	    Boolean, 
	    @SerializedName("date_time")
	    DateTime,
	    @SerializedName("date")
	    Date, 
	    @SerializedName("string")
	    String, 
	    @SerializedName("memo")
	    Memo, 
	    @SerializedName("object")
	    Object,
	    @SerializedName("reference")
		Reference
	}
	
	// Private
	private final String type = "field_metadata";
	private String name;
	private String label;
	private boolean can_modify_label;
	private String entity_name;
	private boolean is_user_defined;
	private boolean visible_in_ui;
	private String description;
	private boolean can_modify_description;
	private boolean filterable;
	private boolean groupable;
	private boolean sortable;
	private boolean auditable;
	private boolean can_modify_auditable;
	private boolean returned_by_default;
	private FieldType field_type;
	private Object[] field_features;
	private String format;
	private Number min_value;
	private Number max_value;
	private boolean required;
	private int max_length;
	private boolean unique;
	private boolean editable;
	@SerializedName("final")
	private boolean isFinal;
	private String sanitization;
	private FieldTypeData field_type_data;
	private boolean can_modify_required;
	private boolean can_modify_format;
	private boolean can_modify_max_length;
	private boolean can_modify_unique;
	private boolean can_modify_editable;
	private boolean can_modify_sanitization;

	/**
	 * This class hold the data structure of Field Target
	 * @author Moris Oz
	 *
	 */
	public class Target {
		
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
	public class FieldTypeData {
		
		private boolean multiple;
		private Target[] targets;
		
		public boolean isMultiple(){return multiple;};
		public Target[] getTargets(){return targets;};
		
	}

	/**
	 * get Type
	 * @return
     */
	public String getType() {return type;}

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
	 * set FieldMetadata's Editable state
	 * @param value new editable state
	 * @throws IllegalAccessException when can_modify_editable is false
     */
	public void setEditable(boolean value) throws IllegalAccessException {
		if (!can_modify_editable) {
			throw new IllegalAccessException("Can't modify Editable field.");
		}

		editable = value;
	};

	/**
	 * get FieldMetadata's ReturnByDefault state
	 * @return
	 */
	public boolean isReturnByDefault(){return returned_by_default;};
	
	/**
	 * get FieldMetadata's Label 
	 * @return
	 */
	public String getLabel(){return label;};

	/**
	 * set FieldMetadata's Label
	 * @param value new label
	 * @throws IllegalAccessException when can_modify_label is false
     */
	public void setLabel(String value) throws IllegalAccessException {
		if (!can_modify_label) {
			throw new IllegalAccessException("Can't modify Label field.");
		}

		label = value;
	}

	/**
	 * get FieldMetadata's CanModifyLabel
	 * @return
     */
	public boolean canModifyLabel() {return can_modify_label;}

	/**
	 * get FieldMetadata's UserDefined
	 * @return
     */
	public boolean isUserDefined() {return is_user_defined;}

	/**
	 * get FieldMetadata's VisibleInUI
	 * @return
     */
	public boolean isVisibleInUI() {return visible_in_ui;}

	/**
	 * get FieldMetadata's Description
	 * @return
     */
	public String getDescription() {return description;}

	/**
	 * set FieldMetadata's Description
	 * @param value new description
	 * @throws IllegalAccessException when can_modify_description is false
     */
	public void setDescription(String value) throws IllegalAccessException {
		if (!can_modify_description) {
			throw new IllegalAccessException("Can't modify Description field.");
		}

		description = value;
	}

	/**
	 * get FieldMetadata's CanModifyDescription
	 * @return
     */
	public boolean canMofidyDescription() {return can_modify_description;}

	/**
	 * get FieldMetadata's Groupable state
	 * @return
     */
	public boolean isGroupable() {return groupable;}

	/**
	 * get FieldMetadata's Sortable state 
	 * @return
	 */
	public boolean isSortable(){return sortable;};

	/**
	 * get FieldMetadata's Auditable state
	 * @return
     */
	public boolean getAuditable() {return auditable;}

	/**
	 * set FieldMetadata's Auditable state
	 * @param value new auditable state
	 * @throws IllegalAccessException
     */
	public void setAuditable(boolean value) throws IllegalAccessException {
		if (!can_modify_auditable) {
			throw new IllegalAccessException("Can't modify Auditable field.");
		}

		auditable = value;
	}

	/**
	 * get FieldMetadata's CanModifyAuditable
	 * @return
     */
	public boolean canModifyAuditable() {return can_modify_auditable;}

	/**
	 * get FieldMetadata's field features
	 * @return
     */
	public Object[] getFieldFeatures() {return field_features;}

	/**
	 * get FieldMetadata's format
	 * @return
     */
	public String getFormat() {return format;}

	/**
	 * set FieldMetadata's format
	 * @param value new format
	 * @throws IllegalAccessException when can_modify_format is false
     */
	public void setFormat(String value) throws IllegalAccessException {
		if (!can_modify_format) {
			throw new IllegalAccessException("Can't modify Format field.");
		}

		format = value;
	}

	/**
	 * get FieldMetadata's min value
	 * @return
     */
	public Number getMinValue() {return min_value;}

	/**
	 * get FieldMetadata's max value
	 * @return
     */
	public Number getMaxValue() {return max_value;}

	/**
	 * get FieldMetadata's final state
	 * @return
     */
	public boolean isFinal() {return isFinal;}

	/**
	 * get FieldMetadata's Required state 
	 * @return
	 */
	public boolean isRequired(){return required;};

	/**
	 * set FieldMetadata's Required state
	 * @param value new required state
	 * @throws IllegalAccessException when can_modify_required is false
     */
	public void setRequired(boolean value) throws IllegalAccessException {
		if (!can_modify_required) {
			throw new IllegalAccessException("Can't modify Required field.");
		}

		required = value;
	}

	/**
	 * get FieldMetadata's Sanitization state 
	 * @return
	 */
	public String getSanitization(){return sanitization;};

	/**
	 * set FieldMetadata's Sanitization state
	 * @param value new Sanitization state
	 * @throws IllegalAccessException when can_modify_sanitization is false
     */
	public void setSanitization(String value) throws IllegalAccessException {
		if (!can_modify_sanitization) {
			throw new IllegalAccessException("Can't modify Sanitization field.");
		}

		sanitization = value;
	}

	/**
	 * get FieldMetadata's Unique state 
	 * @return
	 */
	public boolean isUnique(){return unique;};

	/**
	 * set FieldMetadata's Unique state
	 * @param value new unique state
	 * @throws IllegalAccessException
     */
	public void setUnique(boolean value) throws IllegalAccessException {
		if (!can_modify_unique) {
			throw new IllegalAccessException("Can't modify Unique field.");
		}

		unique = value;
	}

	/**
	 * get FieldMetadata's field Type
	 * @return
	 */
	public FieldType getFieldType(){return field_type;};
	
	/**
	 * get FieldMetadata's Field Type data
	 * @return
	 */
	public FieldTypeData getFieldTypedata(){return field_type_data;};
	
	/**
	 * get FieldMetadata's Max Length
	 * @return
	 */
	public int getMaxLength(){return max_length;};

	/**
	 * set FieldMetadata's MaxLength
	 * @param value new max length
	 * @throws IllegalAccessException when can_modify_max_length is false
     */
	public void setMaxLength(int value) throws IllegalAccessException {
		if (!can_modify_max_length) {
			throw new IllegalAccessException("Can't modify MaxLength field.");
		}

		max_length = value;
	}

	/**
	 * get FieldMetadata's CanModifyRequired
	 * @return
     */
	public boolean canModifyRequired() {return can_modify_required;}

	/**
	 * get FieldMetadata's CanModifyFormat
	 * @return
	 */
	public boolean canModifyFormat() {return can_modify_format;}

	/**
	 * get FieldMetadata's CanModifyMaxLength
	 * @return
	 */
	public boolean canModifyMaxLength() {return can_modify_max_length;}

	/**
	 * get FieldMetadata's CanModifyUnique
	 * @return
	 */
	public boolean canModifyUnique() {return can_modify_unique;}

	/**
	 * get FieldMetadata's CanModifyEditable
	 * @return
	 */
	public boolean canModifyEditable() {return can_modify_editable;}

	/**
	 * get FieldMetadata's CanModifySanitization
	 * @return
	 */
	public boolean canModifySanitization() {return can_modify_sanitization;}
}

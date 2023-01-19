/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.metadata;

import com.google.gson.annotations.SerializedName;

/**
 *
 * This class hold the field metadata object and serve all functionality concern to metadata of fields.
 * See the entity metadata REST API documentation for more information.  The information that is returned in field metadata
 * is static and therefore there are type-safe methods that can be used to return the correct information
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
	
	public enum AccessLevel {
		@SerializedName("PUBLIC")
		Public,
		@SerializedName("PRIVATE")
		Private,
		@SerializedName("PUBLIC_TECH_PREVIEW")
		TechPreview;
	}

	// Private
	private static final String type = "field_metadata";
	private String name;
	private String label;
	private boolean can_modify_label;
	private String entity_name;
	private boolean is_user_defined;
	private boolean visible_in_ui;
	private AccessLevel access_level;
	private boolean accessible_via_business_rules;
	private String description;
	private boolean can_modify_description;
	private boolean filterable;
	private boolean selectable;
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
	 *
	 */
	public class Target {
		
		String[] types;
		String type;
		String logical_name;
		
		public String[] getTypes(){return types;}

		public String  getType(){return type;}

		public String  logicalName(){return logical_name;}


	}
	
	/**
	 * This class hold the data structure of Field Field Type data
	 *
	 */
	public class FieldTypeData {
		
		private boolean multiple;
		private Target[] targets;
		
		public boolean isMultiple(){return multiple;}

		public Target[] getTargets(){return targets;}

	}

	/**
	 * get Type
	 * @return type
     */
	public String getType() {return type;}

	/**
	 * get FieldMetadata's name
	 * @return name
	 */
	public String getName(){return name;}

	/**
	 * get FieldMetadata's Entity Name
	 * @return entity name
	 */
	public String getEntityName(){return entity_name;}

	/**
	 * get FieldMetadata's Filterable state
	 * @return is filterable
	 */
	public boolean isFilterable(){return filterable;}

	/**
	 * get FieldMetadata's Selectable state
	 * @return is selectable
	 */
	public boolean isSelectable(){return selectable;}
	
	/**
	 * get FieldMetadata's Editable state
	 * @return is editable
	 */
	public boolean isEditable(){return editable;}

	/**
	 * get FieldMetadata's ReturnByDefault state
	 * @return is this field returned by default
	 */
	public boolean isReturnByDefault(){return returned_by_default;}

	/**
	 * get FieldMetadata's Label 
	 * @return the field's label
	 */
	public String getLabel(){return label;}

	/**
	 * get FieldMetadata's CanModifyLabel
	 * @return the field's canmodify label
     */
	public boolean canModifyLabel() {return can_modify_label;}

	/**
	 * get FieldMetadata's UserDefined
	 * @return whether the field is user defined
     */
	public boolean isUserDefined() {return is_user_defined;}

	/**
	 * get FieldMetadata's VisibleInUI
	 * @return whether thr field is visible in UI
     */
	public boolean isVisibleInUI() {return visible_in_ui;}

	/**
	 * get FieldMetadata's Access Level
	 * @return access_level
	 */
	public AccessLevel getAccessLevel() {return access_level;}

	/**
	 * get FieldMetadata's VisibleInUI
	 * @return whether the field is accessible by business rules
	 */
	public boolean isAccessibleViaBusinessRules() {return accessible_via_business_rules;}

	/**
	 * get FieldMetadata's Description
	 * @return the field's description
     */
	public String getDescription() {return description;}

	/**
	 * get FieldMetadata's CanModifyDescription
	 * @return whether the description can be modified
     */
	public boolean canMofidyDescription() {return can_modify_description;}

	/**
	 * get FieldMetadata's Groupable state
	 * @return whether the field is groupable
     */
	public boolean isGroupable() {return groupable;}

	/**
	 * get FieldMetadata's Sortable state 
	 * @return whether the field is sortable
	 */
	public boolean isSortable(){return sortable;}

	/**
	 * get FieldMetadata's Auditable state
	 * @return whether the field is auditable
     */
	public boolean getAuditable() {return auditable;}

	/**
	 * get FieldMetadata's CanModifyAuditable
	 * @return whether the audit label can be modified
     */
	public boolean canModifyAuditable() {return can_modify_auditable;}

	/**
	 * get FieldMetadata's field features
	 * @return a list of all features
     */
	public Object[] getFieldFeatures() {return field_features;}

	/**
	 * get FieldMetadata's format
	 * @return the format of the field
     */
	public String getFormat() {return format;}

	/**
	 * get FieldMetadata's min value
	 * @return the min value (if relevant)
     */
	public Number getMinValue() {return min_value;}

	/**
	 * get FieldMetadata's max value
	 * @return the max value (if relevant)
     */
	public Number getMaxValue() {return max_value;}

	/**
	 * get FieldMetadata's final state
	 * @return the final status
     */
	public boolean isFinal() {return isFinal;}

	/**
	 * get FieldMetadata's Required state 
	 * @return whether the field is required
	 */
	public boolean isRequired(){return required;}

	/**
	 * get FieldMetadata's Sanitization state 
	 * @return whether the field is sanitized
	 */
	public String getSanitization(){return sanitization;}

	/**
	 * get FieldMetadata's Unique state 
	 * @return whether the field is unique
	 */
	public boolean isUnique(){return unique;}

	/**
	 * get FieldMetadata's field Type
	 * @return the type of the field
	 */
	public FieldType getFieldType(){return field_type;}

	/**
	 * get FieldMetadata's Field Type data
	 * @return the typedata of the field
	 */
	public FieldTypeData getFieldTypedata(){return field_type_data;}

	/**
	 * get FieldMetadata's Max Length
	 * @return the max length
	 */
	public int getMaxLength(){return max_length;}

	/**
	 * get FieldMetadata's CanModifyRequired
	 * @return whether the required can be modified
     */
	public boolean canModifyRequired() {return can_modify_required;}

	/**
	 * get FieldMetadata's CanModifyFormat
	 * @return whether the format can be modified
	 */
	public boolean canModifyFormat() {return can_modify_format;}

	/**
	 * get FieldMetadata's CanModifyMaxLength
	 * @return whether the max length can be modified
	 */
	public boolean canModifyMaxLength() {return can_modify_max_length;}

	/**
	 * get FieldMetadata's CanModifyUnique
	 * @return whether the uniqueness can be modified
	 */
	public boolean canModifyUnique() {return can_modify_unique;}

	/**
	 * get FieldMetadata's CanModifyEditable
	 * @return whether the editibility can be modified
	 */
	public boolean canModifyEditable() {return can_modify_editable;}

	/**
	 * get FieldMetadata's CanModifySanitization
	 * @return whether the sanitization can be modified
	 */
	public boolean canModifySanitization() {return can_modify_sanitization;}
}

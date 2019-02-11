/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package com.hpe.adm.nga.sdk.generate;

import java.util.*;

import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.features.Feature;
import com.hpe.adm.nga.sdk.metadata.features.RestFeature;
import com.hpe.adm.nga.sdk.metadata.features.SubTypesOfFeature;
import com.hpe.adm.nga.sdk.model.*;

/**
 * A class that is used to help with entity generation
 */
@SuppressWarnings("ALL")
public final class GeneratorHelper {

	private static final String keywords[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
			"char", "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final",
			"finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
			"native", "new", "null", "package", "private", "protected", "public", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
			"void", "volatile", "while" };

	public static String camelCaseFieldName(final String fieldName) {
		return camelCaseFieldName(fieldName, true);
	}

	/**
	 * Convert fieldName to camelCase form.
	 *
	 * @param fieldName
	 *            the field name
	 * @param theFirstLetterIsCapital
	 *            - set true if the first letter in first world should be
	 *            capital
	 * @return the name in camelCase
	 */
	public static String camelCaseFieldName(final String fieldName, boolean theFirstLetterIsCapital) {
		final StringBuffer stringBuffer = new StringBuffer();
		final String[] splitFields = fieldName.split("_");
		int wordCounter = 0;
		for (final String splitField : splitFields) {
			wordCounter++;
			try {
				if (wordCounter > 1 || theFirstLetterIsCapital) {
					stringBuffer.append(splitField.substring(0, 1).toUpperCase());
				} else {
					stringBuffer.append(splitField.substring(0, 1).toLowerCase());
				}
				stringBuffer.append(splitField.substring(1));
			} catch (StringIndexOutOfBoundsException e) {
				e.getCause();
				// we do nothing here and move to the next wordcounter
			}

		}

		return getSanitisedFieldName(stringBuffer.toString());
	}

	public static String convertToUpperCase(final String fieldName) {
		return fieldName.toUpperCase();
	}

	public static String getSanitisedFieldName(final String fieldName) {
		return Arrays.binarySearch(keywords, fieldName) >= 0 ? "_" + fieldName : fieldName;
	}

	public static String getFieldTypeAsJava(FieldMetadata.FieldType fieldType) {
		switch (fieldType) {
		case Date:
		case DateTime:
			return "java.time.ZonedDateTime";
		case Boolean:
			return "Boolean";
		case Float:
			return "Float";
		case Integer:
			return "Long";
		case Memo:
		case String:
			return "String";
		case Object:
			return "String";
		case Reference:
			return "Object";
		}

		throw new UnsupportedOperationException("type: " + fieldType + "is not supported!");
	}

	public static String getFieldModel(FieldMetadata field) {
		switch (field.getFieldType()) {
		case Date:
		case DateTime:
			return DateFieldModel.class.getName();
		case Boolean:
			return BooleanFieldModel.class.getName();
		case Float:
			return FloatFieldModel.class.getName();
		case Integer:
			return LongFieldModel.class.getName();
		case Memo:
		case String:
			return StringFieldModel.class.getName();
		case Object:
			return StringFieldModel.class.getName();
		case Reference:
			return field.getFieldTypedata().isMultiple() ? MultiReferenceFieldModel.class.getName()
					: ReferenceFieldModel.class.getName();
		}

		throw new UnsupportedOperationException("type: " + field.getFieldType() + "is not supported!");
	}

	public static String getSubTypeOf(final EntityMetadata entityMetadata) {
		final Optional<Feature> subTypeOfFeature = getSubTypeOfFeature(entityMetadata);
		return (subTypeOfFeature.isPresent()
				? camelCaseFieldName(((SubTypesOfFeature) subTypeOfFeature.get()).getType())
				: "Typed") + "EntityModel";
	}

	private static Optional<Feature> getSubTypeOfFeature(EntityMetadata entityMetadata) {
		final Collection<Feature> features = entityMetadata.features();
		return features.stream().filter(feature -> feature instanceof SubTypesOfFeature).findFirst();
	}

	public static final class ReferenceMetadata {
		private String referenceClassForSignature;
		private final Set<String> allowedReferencesForAnnotation = new HashSet<>();
		private boolean hasNonTypedReturn = false;
		private boolean hasTypedReturn = false;
		private final Set<String> referenceTypes = new TreeSet<>();
		private String typedType;

		public String getReferenceClassForSignature() {
			return referenceClassForSignature;
		}

		public String getTypedType() {
			return typedType;
		}

		public Set<String> getAllowedReferencesForAnnotation() {
			return allowedReferencesForAnnotation;
		}

		public Set<String> getReferenceTypes() {
			return referenceTypes;
		}

		public boolean hasNonTypedReturn() {
			return hasNonTypedReturn;
		}

		public boolean hasTypedReturn() {
			return hasTypedReturn;
		}
	}

	public static ReferenceMetadata getAllowedSuperTypesForReference(final FieldMetadata fieldMetadata,
			final Collection<EntityMetadata> entityMetadataCollection) {
		final ReferenceMetadata referenceMetadata = new ReferenceMetadata();

		if (fieldMetadata.getFieldType() != FieldMetadata.FieldType.Reference) {
			return null;
		}

		final FieldMetadata.FieldTypeData fieldTypedata = fieldMetadata.getFieldTypedata();
		final FieldMetadata.Target[] targets = fieldTypedata.getTargets();

		for (FieldMetadata.Target target : targets) {
			final String type = target.getType();
			final Optional<EntityMetadata> matchingEntityMetadata = entityMetadataCollection.stream()
					.filter(entityMetadata -> entityMetadata.getName().equals(type))
					.findAny();
			if (matchingEntityMetadata.isPresent()) {
				String camelCaseFieldName = camelCaseFieldName(type);
				referenceMetadata.allowedReferencesForAnnotation.add(camelCaseFieldName + "EntityModel.class");
				referenceMetadata.referenceTypes.add(type);
				final EntityMetadata matchedEntityMetadata = matchingEntityMetadata.get();
				final Optional<Feature> subTypeOfFeature = getSubTypeOfFeature(matchedEntityMetadata);
				final String typedType = camelCaseFieldName(
						subTypeOfFeature.isPresent() ? (((SubTypesOfFeature) subTypeOfFeature.get()).getType()) : type);
				referenceMetadata.typedType = typedType + "Entity";

				boolean hasMultipleTypes = false;
				if (!referenceMetadata.hasNonTypedReturn) {
					if (referenceMetadata.hasTypedReturn) {
						camelCaseFieldName = typedType;
						hasMultipleTypes = true;
					}
					referenceMetadata.referenceClassForSignature = getReferenceSignature(fieldTypedata.isMultiple(),
							hasMultipleTypes, camelCaseFieldName + "Entity");
				}
				referenceMetadata.hasTypedReturn = true;
			} else {
				referenceMetadata.allowedReferencesForAnnotation.add("EntityModel.class");
				referenceMetadata.hasNonTypedReturn = true;
				referenceMetadata.referenceClassForSignature = getReferenceSignature(fieldTypedata.isMultiple(),
						referenceMetadata.hasTypedReturn, "Entity");
			}
		}

		return referenceMetadata;
	}

	private static String getReferenceSignature(final boolean isMultiple, final boolean hasMulipleTypes,
			final String referenceEntity) {
		final StringBuilder stringBuilder = new StringBuilder();
		if (isMultiple) {
			stringBuilder.append("java.util.Collection<");
			/*
			 * If 1 type of Definitive: BlaEntityModel Else If 1 type of non
			 * definitive EntityModel Else If >1 type of Definitive ? extends
			 * BlaEntity Else if >1 type of non definitive ? extends Entity
			 */
			if (hasMulipleTypes) {
				stringBuilder.append("? extends ");
			}
			stringBuilder.append(referenceEntity);
			if (!hasMulipleTypes) {
				stringBuilder.append("Model");
			}
			stringBuilder.append(">");
		} else {
			/*
			 * If 1 type of Definitive: BlaEntityModel Else If 1 type of non
			 * definitive EntityModel Else If >1 type of Definitive <T extends
			 * BlaEntity> T Else if >1 type of non definitive <T extends Entity>
			 * T
			 */
			if (hasMulipleTypes) {
				stringBuilder.append("<T extends ");
			}
			stringBuilder.append(referenceEntity);
			if (!hasMulipleTypes) {
				stringBuilder.append("Model");
			} else {
				stringBuilder.append("> T");
			}
		}
		return stringBuilder.toString();
	}

	public static final class EntityMetadataWrapper {
		com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods[] availableMethods = new com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods[0];
		String url;

		public com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods[] getAvailableMethods() {
			return availableMethods;
		}

		public String getUrl() {
			return url;
		}
	}

	public static EntityMetadataWrapper entityMetadataWrapper(final EntityMetadata entityMetadata) {
		final EntityMetadataWrapper entityMetadataWrapper = new EntityMetadataWrapper();
		final Optional<Feature> restFeatureOptional = entityMetadata.features()
				.stream()
				.filter(feature -> feature instanceof RestFeature)
				.findAny();
		if (!restFeatureOptional.isPresent()) {
			return entityMetadataWrapper;
		}

		final RestFeature restFeature = (RestFeature) restFeatureOptional.get();
		final String[] methods = restFeature.getMethods();
		entityMetadataWrapper.availableMethods = new com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods[methods.length];
		for (int i = 0; i < methods.length; ++i) {
			switch (methods[i]) {
			case "GET":
				entityMetadataWrapper.availableMethods[i] = com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods.GET;
				break;
			case "POST":
				entityMetadataWrapper.availableMethods[i] = com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods.CREATE;
				break;
			case "PUT":
				entityMetadataWrapper.availableMethods[i] = com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods.UPDATE;
				break;
			case "DELETE":
				entityMetadataWrapper.availableMethods[i] = com.hpe.adm.nga.sdk.model.EntityMetadata.AvailableMethods.DELETE;
				break;
			}
		}
		entityMetadataWrapper.url = restFeature.getUrl();
		return entityMetadataWrapper;
	}

	/**
	 * Replaces accented characters in a String by unaccented equivalents.
	 *
	 * @see https://www.mail-archive.com/java-user@lucene.apache.org/msg23562.html
	 * @param input
	 *            String
	 * @return cleaned String
	 */
	public final static String removeAccents(final String input) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			switch (input.charAt(i)) {
			case '\u00C0': // À
			case '\u00C1': // Á
			case '\u00C2': // Â
			case '\u00C3': // Ã
			case '\u00C5': // Å
				result.append("A");
				break;
			case '\u00C4': // Ä
			case '\u00C6': // Æ
				result.append("AE");
				break;
			case '\u00C7': // Ç
				result.append("C");
				break;
			case '\u00C8': // È
			case '\u00C9': // É
			case '\u00CA': // Ê
			case '\u00CB': // Ë
				result.append("E");
				break;
			case '\u00CC': // Ì
			case '\u00CD': // Í
			case '\u00CE': // Î
			case '\u00CF': // Ï
				result.append("I");
				break;
			case '\u00D0': // Ð
				result.append("D");
				break;
			case '\u00D1': // Ñ
				result.append("N");
				break;
			case '\u00D2': // Ò
			case '\u00D3': // Ó
			case '\u00D4': // Ô
			case '\u00D5': // Õ
			case '\u00D8': // Ø
				result.append("O");
				break;
			case '\u00D6': // Ö
			case '\u0152': // Œ
				result.append("OE");
				break;
			case '\u00DE': // Þ
				result.append("TH");
				break;
			case '\u00D9': // Ù
			case '\u00DA': // Ú
			case '\u00DB': // Û
				result.append("U");
				break;
			case '\u00DC': // Ü
				result.append("UE");
				break;
			case '\u00DD': // Ý
			case '\u0178': // Ÿ
				result.append("Y");
				break;
			case '\u00E0': // à
			case '\u00E1': // á
			case '\u00E2': // â
			case '\u00E3': // ã
			case '\u00E5': // å
				result.append("a");
				break;
			case '\u00E4': // ä
			case '\u00E6': // æ
				result.append("ae");
				break;
			case '\u00E7': // ç
				result.append("c");
				break;
			case '\u00E8': // è
			case '\u00E9': // é
			case '\u00EA': // ê
			case '\u00EB': // ë
				result.append("e");
				break;
			case '\u00EC': // ì
			case '\u00ED': // í
			case '\u00EE': // î
			case '\u00EF': // ï
				result.append("i");
				break;
			case '\u00F0': // ð
				result.append("d");
				break;
			case '\u00F1': // ñ
				result.append("n");
				break;
			case '\u00F2': // ò
			case '\u00F3': // ó
			case '\u00F4': // ô
			case '\u00F5': // õ
			case '\u00F8': // ø
				result.append("o");
				break;
			case '\u00F6': // ö
			case '\u0153': // œ
				result.append("oe");
				break;
			case '\u00DF': // ß
				result.append("ss");
				break;
			case '\u00FE': // þ
				result.append("th");
				break;
			case '\u00F9': // ù
			case '\u00FA': // ú
			case '\u00FB': // û
				result.append("u");
				break;
			case '\u00FC': // ü
				result.append("ue");
				break;
			case '\u00FD': // ý
			case '\u00FF': // ÿ
				result.append("y");
				break;
			default:
				result.append(input.charAt(i));
				break;
			}
		}
		return result.toString();
	}
}

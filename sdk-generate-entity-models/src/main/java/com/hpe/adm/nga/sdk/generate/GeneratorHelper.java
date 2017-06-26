package com.hpe.adm.nga.sdk.generate;

import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.features.Feature;
import com.hpe.adm.nga.sdk.metadata.features.RestFeature;
import com.hpe.adm.nga.sdk.metadata.features.SubTypesOfFeature;
import com.hpe.adm.nga.sdk.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by brucesp on 26-Jun-17.
 */
public class GeneratorHelper {

    public static String camelCaseFieldName(final String fieldName) {
        return camelCaseFieldName(fieldName, true);
    }

    /**
     * Convert fieldName to camelCase form.
     *
     * @param fieldName
     * @param theFirstLetterIsCapital - set true if the first letter in first world should be capital
     * @return
     */
    public static String camelCaseFieldName(final String fieldName, boolean theFirstLetterIsCapital) {
        final StringBuffer stringBuffer = new StringBuffer();
        final String[] splitFields = fieldName.split("_");
        int wordCounter = 0;
        for (final String splitField : splitFields) {
            wordCounter++;
            if (wordCounter > 1 || theFirstLetterIsCapital) {
                stringBuffer.append(splitField.substring(0, 1).toUpperCase());
            } else {
                stringBuffer.append(splitField.substring(0, 1).toLowerCase());
            }
            stringBuffer.append(splitField.substring(1));
        }

        return stringBuffer.toString();
    }

    public static String getFieldTypeAsJava(FieldMetadata.FieldType fieldType) {
        switch (fieldType) {
            case Date:
            case DateTime:
                return "java.time.ZonedDateTime";
            case Boolean:
                return "boolean";
            case Float:
                return "float";
            case Integer:
                return "long";
            case Memo:
            case String:
                return "String";
            case Object:
                return "Object";
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
                return field.getFieldTypedata().isMultiple() ? MultiReferenceFieldModel.class.getName() : ReferenceFieldModel.class.getName();
        }

        throw new UnsupportedOperationException("type: " + field.getFieldType() + "is not supported!");
    }

    public static String getSubTypeOf(final EntityMetadata entityMetadata) {
        final Optional<Feature> subTypeOfFeature = getSubTypeOfFeature(entityMetadata);
        return (subTypeOfFeature.isPresent() ? camelCaseFieldName(((SubTypesOfFeature) subTypeOfFeature.get()).getType()) : "") + "EntityModel";
    }

    private static Optional<Feature> getSubTypeOfFeature(EntityMetadata entityMetadata) {
        final Collection<Feature> features = entityMetadata.features();
        return features.stream().filter(feature -> feature instanceof SubTypesOfFeature).findFirst();
    }

    public static final class ReferenceMetadata {
        public String getReferenceClassForSignature() {
            return referenceClassForSignature;
        }

        public Set<String> getAllowedReferencesForAnnotation() {
            return allowedReferencesForAnnotation;
        }

        public String referenceClassForSignature;
        public final Set<String> allowedReferencesForAnnotation = new HashSet<>();
    }

    public static ReferenceMetadata getAllowedSuperTypesForReference(final FieldMetadata fieldMetadata, final Collection<EntityMetadata> entityMetadataCollection) {
        final ReferenceMetadata referenceMetadata = new ReferenceMetadata();

        if (fieldMetadata.getFieldType() != FieldMetadata.FieldType.Reference) {
            return null;
        }

        final FieldMetadata.FieldTypeData fieldTypedata = fieldMetadata.getFieldTypedata();
        final FieldMetadata.Target[] targets = fieldTypedata.getTargets();
        boolean firstTime = true;
        for (FieldMetadata.Target target : targets) {
            final String type = target.getType();
            final Optional<EntityMetadata> matchingEntityMetadata = entityMetadataCollection.stream().filter(entityMetadata -> entityMetadata.getName().equals(type)).findFirst();
            if (!matchingEntityMetadata.isPresent()) {
                if (firstTime) {
                    referenceMetadata.referenceClassForSignature = (fieldTypedata.isMultiple() ? "java.util.Collection<" : "") +
                            ((type.equals("work_item_root") ? "WorkItem" : "") + "EntityModel") +
                            (fieldTypedata.isMultiple() ? ">" : "")
                    ;
                }
                referenceMetadata.allowedReferencesForAnnotation.add((type.equals("work_item_root") ? "WorkItemRoot" : "") + "EntityModel.class");
            } else {
                final EntityMetadata matchedEntityMetadata = matchingEntityMetadata.get();
                final Optional<Feature> subTypeOfFeature = getSubTypeOfFeature(matchedEntityMetadata);
                final String camelCaseFieldName = camelCaseFieldName(subTypeOfFeature.isPresent() ? (((SubTypesOfFeature) subTypeOfFeature.get()).getType()) : type);
                if (firstTime) {
                    referenceMetadata.referenceClassForSignature = (fieldTypedata.isMultiple() ? "java.util.Collection<" : "") +
                            (camelCaseFieldName + "EntityModel") +
                            (fieldTypedata.isMultiple() ? ">" : "")
                    ;
                }
                referenceMetadata.allowedReferencesForAnnotation.add(camelCaseFieldName(type) + "EntityModel.class");
            }
            firstTime = false;
        }
        return referenceMetadata;
    }

    public static final class EntityMetadataWrapper{
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
        final Optional<Feature> restFeatureOptional = entityMetadata.features().stream().filter(feature -> feature instanceof RestFeature).findAny();
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
}

package com.hpe.adm.nga.sdk.model;

import java.util.*;

/**
 * Util methods for comparing {@link EntityModel} objects.
 * Also has useful methods for handling {@link Collection Collection} of {@link EntityModel EntityModels}
 */
public class EntityUtil {

    /**
     * Interface for checking equality between two entity models
     */
    public interface EntityEquator {
        boolean areEqual(EntityModel leftEntityModel, EntityModel rightEntityModel);
    }

    /**
     * Check if two entities are equal by id + type
     *
     * @param leftEntityModel  {@link EntityModel}
     * @param rightEntityModel {@link EntityModel}
     * @return true if the two params have the same id and type, false otherwise
     */
    public static boolean areEqualByIdAndType(EntityModel leftEntityModel, EntityModel rightEntityModel) {
        return Objects.equals(leftEntityModel.getType(), rightEntityModel.getType()) &&
                Objects.equals(leftEntityModel.getId(), rightEntityModel.getId());
    }

    /**
     * Check if two entities have exactly the same field models, not taking into account their order
     *
     * @param leftEntityModel  {@link EntityModel}
     * @param rightEntityModel {@link EntityModel}
     * @return true if the two params have the same field models, false otherwise
     */
    public static boolean areEqualByContent(EntityModel leftEntityModel, EntityModel rightEntityModel) {

        Set<FieldModel> leftFieldModels = leftEntityModel.getValues();
        Set<FieldModel> rightFieldModels = rightEntityModel.getValues();

        if(leftFieldModels.size() != rightFieldModels.size()){
            return false;
        }
        for (FieldModel leftFieldModel : leftFieldModels) {
            FieldModel rightFieldModel = findByFieldName(rightFieldModels, leftFieldModel.getName());
            if (rightFieldModel == null) {
                return false;
            }
            else if(!areFieldModelsEqualByContent(leftFieldModel, rightFieldModel)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if two field models have the same content, uses {@link #areEqualByContent(EntityModel, EntityModel)} for {@link ReferenceFieldModel}
     * and {@link #containsSameEntities(Collection, Collection, EntityEquator)} for {@link MultiReferenceFieldModel}
     *
     * @param leftFieldModel  {@link EntityModel}
     * @param rightFieldModel {@link EntityModel}
     * @return true if the field models have the same content, false otherwise
     */
    public static boolean areFieldModelsEqualByContent(FieldModel leftFieldModel, FieldModel rightFieldModel) {
        if(!Objects.equals(leftFieldModel.getName(), rightFieldModel.getName())){
            return false;
        }
        if(!leftFieldModel.getClass().equals(rightFieldModel.getClass())) {
            return false;
        }
        if(leftFieldModel instanceof ReferenceFieldModel && rightFieldModel instanceof ReferenceFieldModel) {
            EntityModel refLeftEntityModel = (EntityModel) leftFieldModel.getValue();
            EntityModel refRightEntityModel = (EntityModel) rightFieldModel.getValue();
            return areEqualByContent(refLeftEntityModel, refRightEntityModel);
        }
        if(leftFieldModel instanceof MultiReferenceFieldModel && rightFieldModel instanceof MultiReferenceFieldModel) {
            Collection<EntityModel> refLeftEntityModel = ((MultiReferenceFieldModel)leftFieldModel).getValue();
            Collection<EntityModel> refRightEntityModel = ((MultiReferenceFieldModel)rightFieldModel).getValue();
            return containsSameEntities(refLeftEntityModel, refRightEntityModel, EntityUtil::areEqualByContent);
        }
        //simple field, just equals the value
        return Objects.equals(leftFieldModel.getValue(), rightFieldModel.getValue());
    }

    /**
     * Find a {@link FieldModel} by name in a {@link Collection}
     * @param collection of {@link FieldModel}
     * @param fieldName name of the field to look for
     * @return FieldModel if found, null otherwise
     */
    private static FieldModel findByFieldName(Collection<FieldModel> collection, String fieldName) {
        return collection
            .stream()
            .filter(fieldModel -> fieldName.equals(fieldModel.getName()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Check if a collection contains and entity model, uses {@link #areEqualByIdAndType(EntityModel, EntityModel)}
     *
     * @param collection  collection of entity models
     * @param entityModel target entity model
     * @return true if the entity is in the collection, false otherwise
     */
    public static boolean containsEntityModel(Collection<EntityModel> collection, EntityModel entityModel) {
        return containsEntityModel(collection, entityModel, EntityUtil::areEqualByIdAndType);
    }

    /**
     * Check if a collection contains and entity model, can use any {@link EntityEquator}
     *
     * @param collection  collection of entity models
     * @param entityModel target entity model
     * @return true if the entity is in the collection, false otherwise
     */
    public static boolean containsEntityModel(Collection<EntityModel> collection, EntityModel entityModel, EntityEquator entityEquator) {
        for (EntityModel em : collection) {
            if (entityEquator.areEqual(em, entityModel)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if baseCollection contains all elements of sourceCollection
     * @param baseCollection baseCollection
     * @param sourceCollection sourceCollection
     * @param entityEquator {@link EntityEquator} to determine equality
     * @return true if all elements of sourceCollection are present in baseCollection
     */
    public static boolean containsAllEntityModels(Collection<EntityModel> baseCollection, Collection<EntityModel> sourceCollection, EntityEquator entityEquator) {
        for (EntityModel em : sourceCollection) {
            if (!containsEntityModel(baseCollection, em, entityEquator)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if two entity collections contain the same entities
     * @param leftCollection collection of {@link EntityModel}
     * @param rightCollection collection of {@link EntityModel}
     * @param entityEquator {@link EntityEquator} to use to determine equality
     * @return true if the two collections contain the same entities, false otherwise
     */
    public static boolean containsSameEntities(Collection<EntityModel> leftCollection, Collection<EntityModel> rightCollection, EntityEquator entityEquator) {
        return  leftCollection.size() == rightCollection.size() &&
                containsAllEntityModels(leftCollection, rightCollection, entityEquator) &&
                containsAllEntityModels(rightCollection, leftCollection, entityEquator);
    }

    /**
     * Remove an entity model from a collection, uses {@link #areEqualByIdAndType(EntityModel, EntityModel)}
     *
     * @param collection  collection of entity models
     * @param entityModel target entity model
     * @return true if the entity has been removed, false otherwise
     */
    public static boolean removeEntityModel(Collection<EntityModel> collection, EntityModel entityModel) {
        return removeEntityModel(collection, entityModel, EntityUtil::areEqualByIdAndType);
    }

    /**
     * Remove an entity model from a collection, can use any {@link EntityEquator}
     *
     * @param collection  collection of entity models
     * @param entityModel target entity model
     * @return true if the entity has been removed, false otherwise
     */
    public static boolean removeEntityModel(Collection<EntityModel> collection, EntityModel entityModel, EntityEquator entityEquator) {
        List<EntityModel> toBeRemoved = new ArrayList<>();
        for (EntityModel em : collection) {
            if (entityEquator.areEqual(em, entityModel)) {
                toBeRemoved.add(em);
            }
        }
        return collection.removeAll(toBeRemoved);
    }

}
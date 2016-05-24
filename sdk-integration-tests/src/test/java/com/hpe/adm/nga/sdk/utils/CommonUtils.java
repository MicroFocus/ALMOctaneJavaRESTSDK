package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;

import java.util.*;

/**
 * Created by Guy Guetta on 21/04/2016.
 */
public class CommonUtils {

    public static String getValueFromEntityModel(EntityModel entityModel, String fieldName) {
        Set<FieldModel> fieldModelSet = entityModel.getValue();
        for (FieldModel fm : fieldModelSet) {
            if (fm.getName().equals(fieldName)) {
                if(fm.getValue() == null){
                    return null;
                }
                return fm.getValue().toString();
            }
        }
        throw new RuntimeException("Field  name [" + fieldName + "] not found in entity model: "+ entityModel.toString());
    }

    public static int getIdFromEntityModel(EntityModel entityModel) {
        Set<FieldModel> fieldModelSet = entityModel.getValue();
        for (FieldModel fm : fieldModelSet) {
            if (fm.getName().equals("id")) {
                return Integer.parseInt(fm.getValue().toString());
            }
        }
        throw new RuntimeException("Field  ID not found in entity model: "+ entityModel.toString());
    }


    public static List<Integer> getIdFromEntityModelCollection(Collection<EntityModel> entityModels) {
        ArrayList<Integer> idList = new ArrayList<>();
        entityModels.forEach(entityModel -> idList.add(getIdFromEntityModel(entityModel)));
        return idList;
    }

    public static List<String> getValuesFromEntityModelCollection(Collection<EntityModel> entityModels, String fieldName) {
        ArrayList<String> idList = new ArrayList<>();
        entityModels.forEach(entityModel -> idList.add(getValueFromEntityModel(entityModel, fieldName)));
        return idList;
    }

    public static EntityModel getEntityWithStringValue(Collection<EntityModel> entityModels, String fieldName, String value) {
        Collection<EntityModel> entityModelsResult = new ArrayList<>();
        entityModels.forEach(entityModel -> {if(getValueFromEntityModel(entityModel, fieldName) == value || getValueFromEntityModel(entityModel, fieldName).equals(value)){entityModelsResult.add(entityModel);}});
        return entityModelsResult.iterator().next();
    }

    public static boolean isEntityAInEntityB(EntityModel entityA, EntityModel entityB) {
        if (entityA == null) {
            return true;
        }
        Set<FieldModel> fieldsA = entityA.getValue();
        Set<FieldModel> fieldsB = entityB.getValue();
        boolean isMatch;
        for (FieldModel fieldA : fieldsA) {
            if (fieldA.getClass().equals(MultiReferenceFieldModel.class) || fieldA.getClass().equals(ReferenceFieldModel.class)) {
                continue;
            }
            isMatch = false;
            for (FieldModel fieldB : fieldsB) {
                if (fieldA.getName().equals(fieldB.getName())
                        && (fieldA.getValue() == null
                        || fieldA.getValue().equals(fieldB.getValue()))) {
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCollectionAInCollectionB(Collection<EntityModel> collectionA, Collection<EntityModel> collectionB) {
        boolean isMatch;
        for (EntityModel entityA : collectionA) {
            isMatch = false;
            for (EntityModel entityB : collectionB) {
                if (isEntityAInEntityB(entityA, entityB)) {
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                return false;
            }
        }
        return true;
    }

}

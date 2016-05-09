package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Guy Guetta on 21/04/2016.
 */
public class CommonUtils {

    public static String getNameFromEntityModel(EntityModel entityModel) {
        Set<FieldModel> fieldModelSet = entityModel.getValue();
        for (FieldModel fm : fieldModelSet) {
            if (fm.getName().equals("name")) {
                return fm.getValue().toString();
            }
        }
        return "";
    }

    public static int getIdFromEntityModel(EntityModel entityModel) {
        Set<FieldModel> fieldModelSet = entityModel.getValue();
        for (FieldModel fm : fieldModelSet) {
            if (fm.getName().equals("id")) {
                return Integer.parseInt(fm.getValue().toString());
            }
        }
        return -1;
    }


    public static List<Integer> getIdFromEntityModelCollection(Collection<EntityModel> entityModels) {
        ArrayList<Integer> idList = new ArrayList<>();
        entityModels.forEach(entityModel -> idList.add(getIdFromEntityModel(entityModel)));
        return idList;
    }

    public static boolean isEntityAInEntityB(EntityModel entityA, EntityModel entityB) {
        if (entityA == null) {
            return true;
        }
        Set<FieldModel> fieldsA = entityA.getValue();
        Set<FieldModel> fieldsB = entityB.getValue();
        boolean isMatch;
        for (FieldModel fieldA : fieldsA) {
            if (fieldA.getClass().equals(MultiReferenceFieldModel.class)) {
                continue;
            }
            isMatch = false;
            for (FieldModel fieldB : fieldsB) {
                if (fieldA.getName().equals(fieldB.getName())
                        && (fieldA.getValue() == null
                        || fieldA.getValue().equals(fieldB.getValue())
                        || (fieldA.getClass().equals(ReferenceFieldModel.class)
                        && isEntityAInEntityB((EntityModel) fieldA.getValue(), (EntityModel) fieldB.getValue())))) {
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

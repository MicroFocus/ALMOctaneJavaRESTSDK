package com.hpe.adm.nga.sdk.unit_tests.common;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;

import java.util.Collection;
import java.util.Set;

public class CommonUtils {
    public static boolean isEntityAInEntityB(EntityModel entityA, EntityModel entityB) {
        return isEntityAInEntityB(entityA, entityB, false);
    }

    private static boolean isEntityAInEntityB(EntityModel entityA, EntityModel entityB, boolean includeSingleRefFields) {
        if (entityA == null) return true;
        if (entityB == null) return false;

        Set<FieldModel> fieldsA = entityA.getValues();
        for (FieldModel fieldA : fieldsA) {
            if (fieldA == null ||
                    fieldA.getClass().equals(MultiReferenceFieldModel.class) ||
                    !includeSingleRefFields && fieldA.getClass().equals(ReferenceFieldModel.class)) continue;

            FieldModel fieldB = entityB.getValue(fieldA.getName());
            if (fieldB == null) return false;

            if (!fieldA.getClass().equals(fieldB.getClass())) return false;

            if (fieldA.getValue() != null) {
                if (!fieldA.getClass().equals(ReferenceFieldModel.class)) {
                    if (!fieldA.getValue().equals(fieldB.getValue())) return false;
                } else {
                    if (fieldB.getValue() == null) return false;
                    if (!refsEqual(((ReferenceFieldModel) fieldA).getValue(),
                            ((ReferenceFieldModel) fieldB).getValue())) return false;
                }
            }
        }
        return true;
    }

    public static boolean isCollectionAInCollectionB(Collection<EntityModel> collectionA, Collection<EntityModel> collectionB) {
        return isCollectionAInCollectionB(collectionA, collectionB, false);
    }

    public static boolean isCollectionAInCollectionB(Collection<EntityModel> collectionA, Collection<EntityModel> collectionB, boolean includeSingleRefFields) {
        boolean isMatch;
        for (EntityModel entityA : collectionA) {
            isMatch = false;
            for (EntityModel entityB : collectionB) {
                if (isEntityAInEntityB(entityA, entityB, includeSingleRefFields)) {
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

    private static boolean refsEqual(EntityModel refA, EntityModel refB) {
        if (refA == null) return refB == null;
        if (refB == null) return false;
        FieldModel idA = refA.getValue("id");
        FieldModel idB = refB.getValue("id");
        FieldModel typeA = refA.getValue("type");
        FieldModel typeB = refB.getValue("type");
        FieldModel subtypeA = refA.getValue("subtype");
        FieldModel subtypeB = refB.getValue("subtype");
        return !(idA == null || idB == null || idA.getValue() == null || idB.getValue() == null ||
                !idA.getValue().equals(idB.getValue())) && (typeA != null && typeB != null && typeA.getValue() != null && typeA.getValue().equals(typeB.getValue()) || subtypeA != null && typeB != null && subtypeA.getValue() != null && subtypeA.getValue().equals(typeB.getValue()) || subtypeB != null && typeA != null && subtypeB.getValue() != null && subtypeB.getValue().equals(typeA.getValue()));
    }
}

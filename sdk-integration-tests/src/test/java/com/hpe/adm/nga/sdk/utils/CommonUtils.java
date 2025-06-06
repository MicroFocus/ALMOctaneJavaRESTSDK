/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.model.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by Guy Guetta on 21/04/2016.
 */
public class CommonUtils {

    public static String getValueFromEntityModel(EntityModel entityModel, String fieldName) {
        Set<FieldModel> fieldModelSet = entityModel.getValues();
        for (FieldModel fm : fieldModelSet) {
            if (fm.getName().equals(fieldName)) {
                if (fm.getValue() == null) {
                    return null;
                }
                return fm.getValue().toString();
            }
        }
        throw new RuntimeException("Field  name [" + fieldName + "] not found in entity model: " + entityModel.toString());
    }

    public static String getIdFromEntityModel(EntityModel entityModel) {
        Set<FieldModel> fieldModelSet = entityModel.getValues();
        for (FieldModel fm : fieldModelSet) {
            if (fm.getName().equals("id")) {
                return fm.getValue().toString();
            }
        }
        throw new RuntimeException("Field  ID not found in entity model: " + entityModel.toString());
    }


    public static List<String> getIdFromEntityModelCollection(Collection<EntityModel> entityModels) {
        ArrayList<String> idList = new ArrayList<>();
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
        entityModels.forEach(entityModel -> {
            final String valueFromEntityModel = getValueFromEntityModel(entityModel, fieldName);
            if ((valueFromEntityModel != null && valueFromEntityModel.equals(value)) ||
                    (value == valueFromEntityModel)) {
                entityModelsResult.add(entityModel);
            }
        });
        return entityModelsResult.iterator().next();
    }

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
                if (fieldA.getClass().equals(DateFieldModel.class)){
                    if (!((ZonedDateTime)fieldA.getValue()).isEqual(((ZonedDateTime)fieldB.getValue()))) return false;
                }
                else if (!fieldA.getClass().equals(ReferenceFieldModel.class)) {
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
}

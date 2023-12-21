/*
 * Copyright 2016-2023 Open Text.
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
package com.hpe.adm.nga.sdk.extension.businessrules;

import com.hpe.adm.nga.sdk.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BusinessRuleEntityModel extends EntityModel {

    public BusinessRuleEntityModel(EntityModel entityModel) {
        this(entityModel, true);
    }

    private BusinessRuleEntityModel(EntityModel entityModel, boolean convert) {
        super(new HashSet<>());
        this.setValues(convert ? convert(entityModel).getValues() : entityModel.getValues());
    }

    private EntityModel convert(EntityModel entityModel) {
        entityModel.setValues(entityModel.getValues().stream()
                .map(fieldModel -> {
                    if (fieldModel instanceof ObjectFieldModel) {
                        ObjectFieldModel objectFieldModel = (ObjectFieldModel) fieldModel;

                        try {
                            return new ArrayFieldModel(fieldModel.getName(),
                                    getEntitiesFromArray(new JSONArray(objectFieldModel.getValue())));
                        } catch (Exception e) {
                            return new ReferenceFieldModel(fieldModel.getName(),
                                    convert(ModelParser.getInstance().getEntityModel(new JSONObject(objectFieldModel.getValue()))));
                        }
                    } else if (fieldModel instanceof StringFieldModel) {
                        StringFieldModel stringFieldModel = (StringFieldModel) fieldModel;

                        Fact fact = Fact.getFact(stringFieldModel.getValue());
                        if (fact != null) {
                            return new FactFieldModel(stringFieldModel.getName(), fact);
                        }
                    } else if (fieldModel instanceof ReferenceFieldModel) {
                        ReferenceFieldModel refFieldModel = (ReferenceFieldModel) fieldModel;

                        return new ReferenceFieldModel(fieldModel.getName(), convert(refFieldModel.getValue()));
                    } else if (fieldModel instanceof MultiReferenceFieldModel) {
                        MultiReferenceFieldModel multiReferenceFieldModel = (MultiReferenceFieldModel) fieldModel;

                        return new MultiReferenceFieldModel(multiReferenceFieldModel.getName(),
                                multiReferenceFieldModel.getValue().stream()
                                        .map(this::convert)
                                        .collect(Collectors.toList()));
                    }

                    return fieldModel;
                })
                .collect(Collectors.toSet()));

        return entityModel;
    }

    private Collection<EntityModel> getEntitiesFromArray(JSONArray jsonArray) {
        Collection<EntityModel> entityModels = new ArrayList<>();
        IntStream.range(0, jsonArray.length()).forEach(i ->
                entityModels.add(convert(ModelParser.getInstance().getEntityModel(jsonArray.getJSONObject(i)))));

        return entityModels;
    }

    public Collection<Fact> getFacts() {
        Collection<Fact> facts = new ArrayList<>();

        this.getValues().forEach(fieldModel -> {
            if (fieldModel instanceof FactFieldModel) {
                facts.add(((FactFieldModel) fieldModel).getValue());
            } else if (fieldModel instanceof MultiReferenceFieldModel) {
                MultiReferenceFieldModel multiRefFieldModel = (MultiReferenceFieldModel) fieldModel;
                multiRefFieldModel.getValue().forEach(em -> facts.addAll(new BusinessRuleEntityModel(em, false).getFacts()));
            } else if (fieldModel instanceof ReferenceFieldModel) {
                ReferenceFieldModel refFieldModel = (ReferenceFieldModel) fieldModel;
                facts.addAll(new BusinessRuleEntityModel(refFieldModel.getValue(), false).getFacts());
            }
        });

        return facts;
    }
}

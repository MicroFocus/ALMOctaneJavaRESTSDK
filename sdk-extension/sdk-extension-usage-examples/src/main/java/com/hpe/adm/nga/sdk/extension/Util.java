/**
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
package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class.getName());

    /**
     * Simple debug method used to print a list of EntityModel objects to the console
     * For each entity, it will try to print the type + the fields [name] or [text]
     * If the fields that it tries to print do not exists, it will only print the type of the entity
     * @param entities {@link Collection} of {@link EntityModel}s to print
     */
    public static void printEntities(Collection<EntityModel> entities) {
        logger.info("Collection size: {}", entities.size());
        if (entities.size() != 0) {
            entities
                .stream()
                .map(entityModel -> {
                    String entityType = entityModel.getValue("type").getValue().toString().toUpperCase();
                    String[] printFieldNames = new String[]{"name", "text"};

                    for(String fieldName : printFieldNames) {
                        FieldModel printFieldModel = entityModel.getValue(fieldName);
                        if (printFieldModel != null) {
                            return entityType+":"+printFieldModel.getValue().toString();
                        }
                    }
                    return entityType;
                })
                .forEach(logger::info);
        }
    }

}

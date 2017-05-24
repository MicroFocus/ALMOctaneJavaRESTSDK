/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpe.adm.nga.sdk.examples.customhttpclient;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.OctaneClassFactory;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Example to show how to use your own custom http client with the sdk
 */
public class DummyOctaneHttpClientExample {

    public static void main(String[] args) {

        Authentication authentication
                = new SimpleUserAuthentication("", "");

        //Set custom implementation via system propery
        System.getProperties().setProperty(
                OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME,
                "com.hpe.adm.nga.sdk.examples.customhttpclient.DummyOctaneClassFactory");

        int dummyDefectCount = ThreadLocalRandom.current().nextInt(0, 101);
        //Set the number of dummy entities the custom http client should make
        DummyOctaneHttpClient.dummyDefectCount = dummyDefectCount;

        Octane octane =
                new Octane.Builder(authentication)
                        .Server("")
                        .sharedSpace(-1)
                        .workSpace(-1)
                        .build();

        //Fetch defects as an example a print them to the console
        Collection<EntityModel> defects = octane.entityList("work_items").get().execute();
        printEntities(defects);

        assert defects.size() == dummyDefectCount;

        //Remove system property
        System.getProperties().remove(OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME);
    }

    /**
     * Simple debug method used to print a list of EntityModel objects to the console
     * For each entity, it will try to print the type + the fields [name] or [text]
     * If the fields that it tries to print do not exists, it will only print the type of the entity
     * @param entities
     */
    private static void printEntities(Collection<EntityModel> entities) {
        System.out.println("Collection size: " + entities.size());
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
                    .forEach(System.out::println);
        }
        System.out.println("-----------------------------");
    }
}

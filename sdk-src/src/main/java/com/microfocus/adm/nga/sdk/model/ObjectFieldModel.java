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
package com.microfocus.adm.nga.sdk.model;

/**
 * This class hold the ObjectFieldModel objects. These are normally arbitrary JSON objects.  In this case the JSON is
 * represented as a string
 */
public class ObjectFieldModel extends StringFieldModel implements FieldModel<String> {

    /**
     * Creates a new StringFieldModel object
     *
     * @param newName  - Field name
     * @param newValue - Field Value
     */
    public ObjectFieldModel(String newName, String newValue) {
        super(newName, newValue);
    }
}

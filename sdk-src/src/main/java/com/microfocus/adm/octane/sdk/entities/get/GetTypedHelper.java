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
package com.microfocus.adm.octane.sdk.entities.get;

import com.microfocus.adm.octane.sdk.entities.TypedEntityList;
import com.microfocus.adm.octane.sdk.network.OctaneRequest;

/**
 * A helper class for shared functionality for get typed entities
 */
final class GetTypedHelper {

    /**
     * Set Fields Parameters
     *
     * @param octaneRequest The request to be sent to the server
     * @param fields An array or comma separated list of fields to be retrieved
     */
    static <F extends TypedEntityList.AvailableFields> void addFields(final OctaneRequest octaneRequest, final F... fields) {
        final String fieldsAsString[] = new String[fields.length];
        for (int i = 0; i < fields.length; ++i) {
            fieldsAsString[i] = fields[i].getFieldName();
        }
        octaneRequest.getOctaneUrl().addFieldsParam(fieldsAsString);
    }

}

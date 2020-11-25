/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.unit_tests.common;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.util.Collection;
import java.util.Set;

public class CommonMethods {

    private final static String urlDomain = "http://localhost:8080";
    private final static String sharedSpace = "1001";
    private final static int workSpace = 1002;

    public static Octane getOctaneForTest() {
        final OctaneHttpClient octaneHttpClient = PowerMockito.mock(OctaneHttpClient.class);
        PowerMockito.when(octaneHttpClient.authenticate(Mockito.any())).thenReturn(true);

        return new Octane.Builder(new SimpleUserAuthentication("user", "password"), octaneHttpClient)
                .Server(getDomain())
                .settings(new Octane.OctaneCustomSettings() {{
                    set(Setting.READ_TIMEOUT,5000);
                    set(Setting.CONNECTION_TIMEOUT,2000);
                }})
                .sharedSpace(Long.parseLong(getSharedSpace()))
                .workSpace(getWorkSpace())
                .build();
    }

    public static String getDomain() {
        return urlDomain;
    }

    public static String getSharedSpace() {
        return sharedSpace;
    }

    public static int getWorkSpace() {
        return workSpace;
    }


    @SuppressWarnings("rawtypes")
    public static boolean isErrorAInErrorB(ErrorModel entityA, ErrorModel entityB) {
        if (entityA == null) {
            return true;
        }
        Set<FieldModel> fieldsA = entityA.getValues();
        Set<FieldModel> fieldsB = entityB.getValues();
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

    public static boolean isErrorCollectionAInErrorCollectionB(Collection<ErrorModel> collectionA, Collection<ErrorModel> collectionB) {
        boolean isMatch;
        for (ErrorModel entityA : collectionA) {
            isMatch = false;
            for (ErrorModel entityB : collectionB) {
                if (isErrorAInErrorB(entityA, entityB)) {
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
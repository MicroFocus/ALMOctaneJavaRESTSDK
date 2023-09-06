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
package com.hpe.adm.nga.sdk.tests.metadata;

import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 *
 * Created by Dmitry Zavyalov on 08/05/2016.
 */
public class TestReadMetadata extends TestBase {

    public TestReadMetadata() {
        entityName = "product_areas";
    }

    @Test
    public void testReadMetadataEntities() throws Exception {
        Metadata metadata = octane.metadata();
        Collection<EntityMetadata> entityMetadata = metadata.entities().execute();
        Collection<EntityMetadata> entityMetadataTwoEntities = metadata.entities("defects").execute();
    }

    @Test
    public void testReadMetadataFields() throws Exception {
        Metadata metadata = octane.metadata();
        Collection<FieldMetadata> fieldMetadata = metadata.fields().execute();

        int referenceTypeDataIsEmpty = 0;
        for (FieldMetadata fieldMetadataFields : fieldMetadata) {
            if (fieldMetadataFields.getFieldType().equals(FieldMetadata.FieldType.Reference.toString().toLowerCase()) && fieldMetadataFields.getFieldTypedata() == null) {
                referenceTypeDataIsEmpty++;
            }
        }

        String message = "[field_type_data] is null for field type [reference] " + referenceTypeDataIsEmpty + " times in \"Metadata.fields().execute()\"";
        Assert.assertFalse(message, referenceTypeDataIsEmpty > 0);
    }
}

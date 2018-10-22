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
package com.microfocus.adm.octane.sdk.tests.filtering;

import com.microfocus.adm.octane.sdk.model.EntityModel;
import com.microfocus.adm.octane.sdk.model.FieldModel;
import com.microfocus.adm.octane.sdk.model.ReferenceFieldModel;
import com.microfocus.adm.octane.sdk.query.Query;
import com.microfocus.adm.octane.sdk.query.QueryMethod;
import com.microfocus.adm.octane.sdk.tests.base.TestBase;
import com.microfocus.adm.octane.sdk.utils.CommonUtils;
import com.microfocus.adm.octane.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by Guy Guetta on 02/05/2016.
 */

public class TestCrossFiltering extends TestBase {
    private static String defectId;
    private static String releaseId;

    public TestCrossFiltering() {
        entityName = "releases";
    }

    @Test
    public void simpleCrossFilter() throws Exception {
        Query query = Query.statement("release", QueryMethod.EqualTo,
                (Query.statement("id",QueryMethod.EqualTo, releaseId))
                        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        String newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
        Assert.assertEquals("More defects than expected in response", 1, defects.size());
        Assert.assertEquals("Wrong defect id in response", defectId, newDefectId);
    }

    @Test
    public void simpleCrossFilterReverse() throws Exception {
        Query query = Query.statement("release", QueryMethod.EqualTo,
                Query.not("id", QueryMethod.EqualTo, releaseId)
        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        // it could be that there are no other defects.  So if defects is empty that's also good
        if (defects != null && defects.size() > 0) {
            String newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
            Assert.assertNotEquals("Wrong defect id in response", defectId, newDefectId);
        }
    }

    @Ignore
    @Test
    public void crossFilterTwoHopes() throws Exception {
        Query query = Query.statement("id", QueryMethod.EqualTo,
                            Query.statement("release", QueryMethod.EqualTo,
                                Query.statement("id", QueryMethod.EqualTo, releaseId)
                            )
                        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        String newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
        Assert.assertEquals("More defects than expected in response", 1, defects.size());
        Assert.assertEquals("Wrong defect id in response", defectId, newDefectId);
    }

    @Ignore
    @Test
    public void crossFilterTwoHopesReverse() throws Exception {
        Query query = Query.statement("id", QueryMethod.EqualTo,
                Query.not("release", QueryMethod.EqualTo,
                        Query.statement("id", QueryMethod.EqualTo, releaseId)
                )
                        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        String newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
        Assert.assertNotEquals("Wrong defect id in response", defectId, newDefectId);
    }

    @BeforeClass
    public static void initTests() throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> releaseEntity = DataGenerator.generateEntityModel(octane, "releases", fields);
        Collection<EntityModel> releases = octane.entityList("releases").create().entities(releaseEntity).execute();
        EntityModel release = releases.iterator().next();
        releaseId = CommonUtils.getIdFromEntityModel(release);

        fields.add(new ReferenceFieldModel("release",release));
        Collection<EntityModel> defectEntity = DataGenerator.generateEntityModel(octane, "defects", fields);
        Collection<EntityModel> defects = octane.entityList("defects").create().entities(defectEntity).execute();
        EntityModel defect = defects.iterator().next();
        defectId = CommonUtils.getIdFromEntityModel(defect);
    }
}

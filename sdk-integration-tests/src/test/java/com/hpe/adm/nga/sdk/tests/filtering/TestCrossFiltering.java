package com.hpe.adm.nga.sdk.tests.filtering;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Guy Guetta on 02/05/2016.
 */

public class TestCrossFiltering extends TestBase {
    private static long defectId;
    private static long releaseId;

    public TestCrossFiltering() {
        entityName = "releases";
    }

    @Test
    public void simpleCrossFilter() throws Exception {
        Query query = new Query.QueryBuilder("release", Query::equalTo,
                            new Query.QueryBuilder("id", Query::equalTo, releaseId)
                        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        long newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
        Assert.assertEquals("More defects than expected in response", 1, defects.size());
        Assert.assertEquals("Wrong defect id in response", defectId, newDefectId);
    }

    @Test
    public void simpleCrossFilterReverse() throws Exception {
        Query query = new Query.QueryBuilder("release", Query::equalTo,
                new Query.QueryBuilder("id", Query::equalTo, releaseId),true
        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        long newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
        Assert.assertNotEquals("Wrong defect id in response", defectId, newDefectId);
    }

    @Ignore
    @Test
    public void crossFilterTwoHopes() throws Exception {
        Query query = new Query.QueryBuilder("id", Query::equalTo,
                            new Query.QueryBuilder("release", Query::equalTo,
                                new Query.QueryBuilder("id", Query::equalTo, releaseId)
                            )
                        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        long newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
        Assert.assertEquals("More defects than expected in response", 1, defects.size());
        Assert.assertEquals("Wrong defect id in response", defectId, newDefectId);
    }

    @Ignore
    @Test
    public void crossFilterTwoHopesReverse() throws Exception {
        Query query = new Query.QueryBuilder("id", Query::equalTo,
                new Query.QueryBuilder("release", Query::equalTo,
                        new Query.QueryBuilder("id", Query::equalTo, releaseId), true
                )
                        ).build();
        Collection<EntityModel> defects = octane.entityList("defects").get().query(query).execute();
        long newDefectId = CommonUtils.getIdFromEntityModel(defects.iterator().next());
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

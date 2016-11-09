package com.hpe.adm.nga.sdk.tests.filtering;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
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
@Ignore
public class TestCrossFiltering extends TestBase {
    private static long defectId;
    private static long releaseId;
    private static long paId;

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
        Query query = new Query.QueryBuilder("work_item_has_release", Query::equalTo,
                            new Query.QueryBuilder("id", Query::equalTo, defectId)
                        ).build();
        Collection<EntityModel> releases = octane.entityList("releases").get().query(query).execute();
        long newReleaseId = CommonUtils.getIdFromEntityModel(releases.iterator().next());
        Assert.assertEquals("More releases than expected in response", 1, releases.size());
        Assert.assertEquals("Wrong release id in response", releaseId, newReleaseId);
    }

    @Test
    public void crossFilterTwoHopes() throws Exception {
        Query query = new Query.QueryBuilder("work_item", Query::equalTo,
                            new Query.QueryBuilder("release", Query::equalTo,
                                new Query.QueryBuilder("id", Query::equalTo, releaseId)
                            )
                        ).build();
        Collection<EntityModel> pas = octane.entityList("product_areas").get().query(query).execute();
        long newPaId = CommonUtils.getIdFromEntityModel(pas.iterator().next());
        Assert.assertEquals("More PAs than expected in response", 1, pas.size());
        Assert.assertEquals("Wrong PA id in response", paId, newPaId);
    }

    @Test
    public void crossFilterTwoHopesReverse() throws Exception {
        Query query = new Query.QueryBuilder("work_item_has_release", Query::equalTo,
                            new Query.QueryBuilder("product_areas", Query::equalTo,
                                new Query.QueryBuilder("id", Query::equalTo, paId)
                            )
                        ).build();
        Collection<EntityModel> releases = octane.entityList("releases").get().query(query).execute();
        long newReleaseId = CommonUtils.getIdFromEntityModel(releases.iterator().next());
        Assert.assertEquals("More releases than expected in response", 1, releases.size());
        Assert.assertEquals("Wrong release id in response", releaseId, newReleaseId);
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

        fields.clear();
        fields.add(new MultiReferenceFieldModel("work_item", defects));
        Collection<EntityModel> paEntity = DataGenerator.generateEntityModel(octane, "product_areas", fields);
        Collection<EntityModel> pas = octane.entityList("product_areas").create().entities(paEntity).execute();
        paId = CommonUtils.getIdFromEntityModel(pas.iterator().next());
    }
}

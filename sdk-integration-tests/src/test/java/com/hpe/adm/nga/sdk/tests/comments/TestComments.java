package com.hpe.adm.nga.sdk.tests.comments;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Guy Guetta on 02/05/2016.
 */
public class TestComments extends TestBase {
    public TestComments() {
        entityName = "product_areas";
    }

    @Test
    public void testCreateCommentForDefect() throws Exception {
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, "defects");
        Collection<EntityModel> entityModels = nga.entityList("defects").create().entities(generatedEntity).execute();

        Collection<EntityModel> expectedComments = createComments("owner_work_item", entityModels);

        Collection<EntityModel> actualComments = nga.entityList("comments").get().execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(expectedComments, actualComments));
    }

    private Collection<EntityModel> createComments(String fieldEntityType, Collection<EntityModel> entityModels) throws Exception {
        EntityModel entity = entityModels.iterator().next();

        Collection<EntityModel> users = nga.entityList("workspace_users").get().execute();

        EntityModel user = users.iterator().next();
        Set<FieldModel> fields = new HashSet<>();
        fields.add(new ReferenceFieldModel("author", user));
        fields.add(new ReferenceFieldModel(fieldEntityType, entity));

        Collection<EntityModel> comments = new ArrayList<>();
        comments.add(new EntityModel(fields));

        nga.entityList("comments").create().entities(comments).execute();

        return comments;
    }
}

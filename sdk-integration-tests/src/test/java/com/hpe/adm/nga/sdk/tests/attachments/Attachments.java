package com.hpe.adm.nga.sdk.tests.attachments;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * Created by Guy Guetta on 03/05/2016.
 */
public class Attachments extends TestBase {

    public Attachments() {
        entityName = "product_areas";
    }

//    @Test
    public void testCreateAttachmentForDefect() throws Exception {
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, "defects");
        Collection<EntityModel> defectModel = nga.entityList("defects").create().entities(generatedEntity).execute();

        Collection<EntityModel> expectedComments = createAttachment("owner_work_item", defectModel);

        Collection<EntityModel> actualComments = nga.entityList("attachments").get().execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(expectedComments, actualComments));
    }

    private Collection<EntityModel> createAttachment(String fieldEntityType, Collection<EntityModel> entityModels) throws Exception {
        EntityModel entity = entityModels.iterator().next();

        Set<FieldModel> fields = new HashSet<>();
        fields.add(new ReferenceFieldModel(fieldEntityType, entity));
        FieldModel name = new StringFieldModel("name", "sdk_attachment_" + UUID.randomUUID() + ".txt");
        fields.add(name);
        Collection<EntityModel> attachments = new ArrayList<>();
        attachments.add(new EntityModel(fields));

        ByteArrayInputStream bais = new ByteArrayInputStream("The first line\nThe second line".getBytes());

        nga.AttachmentList().create().entities(attachments, bais, "text/plain", "text_attachment.txt").execute();

        return attachments;
    }
}

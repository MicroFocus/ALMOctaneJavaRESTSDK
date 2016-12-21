package com.hpe.adm.nga.sdk.tests.attachments;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * Created by Guy Guetta on 03/05/2016.
 */
public class TestAttachments extends TestBase {

    public TestAttachments() {
        entityName = "product_areas";
    }

    @Test
    public void testCreateAttachmentForDefect() throws Exception {
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, "defects");
        Collection<EntityModel> defectModel = octane.entityList("defects").create().entities(generatedEntity).execute();

        Collection<EntityModel> expectedAttachments = createAttachment("owner_work_item", defectModel);

        Collection<EntityModel> actualAttachments = octane.entityList("attachments").get().addFields("owner_work_item", "name").execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(expectedAttachments, actualAttachments, true));
    }

    private Collection<EntityModel> createAttachment(String fieldEntityType, Collection<EntityModel> entityModels) throws Exception {
        EntityModel entity = entityModels.iterator().next();

        Set<FieldModel> fields = new HashSet<>();
        fields.add(new ReferenceFieldModel(fieldEntityType, entity));
        final String attachmentNAme = "sdk_attachment_" + UUID.randomUUID() + ".txt";
        FieldModel name = new StringFieldModel("name", attachmentNAme);
        fields.add(name);
        Collection<EntityModel> attachments = new ArrayList<>();
        attachments.add(new EntityModel(fields));

        ByteArrayInputStream bais = new ByteArrayInputStream("The first line\nThe second line".getBytes());

        octane.AttachmentList().create().entities(attachments, bais, "text/plain", attachmentNAme).execute();

        return attachments;
    }
}

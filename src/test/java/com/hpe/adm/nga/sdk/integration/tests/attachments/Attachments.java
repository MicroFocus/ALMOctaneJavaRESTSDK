package test.java.com.hpe.adm.nga.sdk.integration.tests.attachments;

import main.java.com.hpe.adm.nga.sdk.model.EntityModel;
import main.java.com.hpe.adm.nga.sdk.model.FieldModel;
import main.java.com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import main.java.com.hpe.adm.nga.sdk.model.StringFieldModel;
import test.java.com.hpe.adm.nga.sdk.integration.utils.CommonUtils;
import test.java.com.hpe.adm.nga.sdk.integration.utils.generator.DataGenerator;
import test.java.com.hpe.adm.nga.sdk.integration.tests.base.TestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Guy Guetta on 03/05/2016.
 */
public class Attachments extends TestBase {

    public Attachments() {
        entityName = "product_areas";
    }

//    @Test
//    public void testCreateAttachmentForDefect() throws Exception {
//        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, "defects");
//        Collection<EntityModel> defectModel = nga.entityList("defects").create().entities(generatedEntity).execute();
//
//        Collection<EntityModel> expectedComments = createAttachment("owner_work_item", defectModel);
//
//        Collection<EntityModel> actualComments = nga.entityList("attachments").get().execute();
//
//        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(expectedComments, actualComments));
//    }
//
//    private Collection<EntityModel> createAttachment(String fieldEntityType, Collection<EntityModel> entityModels) throws Exception {
//        EntityModel entity = entityModels.iterator().next();
//
//        Set<FieldModel> fields = new HashSet<>();
//        fields.add(new ReferenceFieldModel(fieldEntityType, entity));
//        FieldModel name = new StringFieldModel("name", "sdk_attachment_" + UUID.randomUUID() + ".txt");
//        fields.add(name);
//        Collection<EntityModel> attachments = new ArrayList<>();
//        attachments.add(new EntityModel(fields));
//
//        List<String> lines = Arrays.asList("The first line", "The second line");
//        Path path = Paths.get("text_attachment.txt");
//        Files.write(path, lines, Charset.forName("UTF-8"));
//
//        nga.AttachmentList().create().entities(attachments, path.toString()).execute();
//
//        return attachments;
//    }
}

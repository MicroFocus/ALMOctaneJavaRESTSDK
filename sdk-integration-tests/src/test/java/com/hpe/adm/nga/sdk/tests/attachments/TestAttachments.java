/*
 * © Copyright 2016-2021 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.tests.attachments;

import com.google.common.io.ByteStreams;
import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Integration tests for {@link com.hpe.adm.nga.sdk.attachments.AttachmentList}
 */
public class TestAttachments extends TestBase {

    private static EntityModel attachmentParent;
    private static String ownerFieldName;

    /**
     * Create a parent defect for all tests
     */
    @BeforeClass
    public static void createAttachmentParent() {
        try {
            Collection<EntityModel> entities = DataGenerator.generateEntityModel(octane, "defects");
            entities = octane.entityList("defects").create().entities(entities).execute();
            ownerFieldName = "owner_work_item";
            attachmentParent = entities.iterator().next();
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to create defect to use as attachment owner_work_item", ex);
        }
    }

    /**
     * Create a text attachment. File extensions has to be .txt
     */
    @Test
    public void testTextAttachment() {

        EntityModel initialAttachment = new EntityModel();
        initialAttachment.setValue(new StringFieldModel("name", UUID.randomUUID().toString() + ".txt"));
        initialAttachment.setValue(new StringFieldModel("description", UUID.randomUUID().toString()));
        initialAttachment.setValue(new ReferenceFieldModel(ownerFieldName, attachmentParent));

        String attachmentText = UUID.randomUUID().toString();

        Collection<EntityModel> entities =
                octane.attachmentList()
                        .create()
                        .attachment(
                                initialAttachment,
                                new ByteArrayInputStream(attachmentText.getBytes()),
                                "text/plain",
                                "")
                        .execute();

        EntityModel uploadedAttachment = reloadEntityModel(entities, ownerFieldName);

        checkAttachmentJson(initialAttachment, uploadedAttachment, ownerFieldName);
        checkAttachmentContent(attachmentText.getBytes(), uploadedAttachment);
    }

    /**
     * Create a text attachment using ISO-8859-1 encoding. File extensions has to be .txt
     */
    @Test
    public void testAttachmentName() {
        String defaultCharset = Charset.defaultCharset().displayName();
        setEncoding("ISO-8859-1");

        EntityModel initialAttachment = new EntityModel();
        initialAttachment.setValue(new StringFieldModel("name", "è" + UUID.randomUUID().toString() + ".txt"));
        initialAttachment.setValue(new StringFieldModel("description", UUID.randomUUID().toString()));
        initialAttachment.setValue(new ReferenceFieldModel(ownerFieldName, attachmentParent));

        String attachmentText = UUID.randomUUID().toString();

        Collection<EntityModel> entities =
                octane.attachmentList()
                        .create()
                        .attachment(
                                initialAttachment,
                                new ByteArrayInputStream(attachmentText.getBytes()),
                                "text/plain",
                                "")
                        .execute();

        EntityModel uploadedAttachment = reloadEntityModel(entities, ownerFieldName);

        checkAttachmentJson(initialAttachment, uploadedAttachment, ownerFieldName);
        checkAttachmentContent(attachmentText.getBytes(), uploadedAttachment);

        setEncoding(defaultCharset);
    }

    /**
     * Create an image attachment, generate a random png image. File extension has to be .png,
     * the server will compute the mime type and compare it to the file extension.
     */
    @Test
    public void testImageAttachment() {

        EntityModel initialAttachment = new EntityModel();
        initialAttachment.setValue(new StringFieldModel("name", UUID.randomUUID().toString() + ".png"));
        initialAttachment.setValue(new StringFieldModel("description", UUID.randomUUID().toString()));
        initialAttachment.setValue(new ReferenceFieldModel("owner_work_item", attachmentParent));

        byte[] pngBytes = generateImageAttachmentContent();

        Collection<EntityModel> entities =
                octane.attachmentList()
                        .create()
                        .attachment(
                                initialAttachment,
                                new ByteArrayInputStream(pngBytes),
                                "image/png",
                                "")
                        .execute();

        EntityModel uploadedAttachment = reloadEntityModel(entities, ownerFieldName);

        checkAttachmentJson(initialAttachment, uploadedAttachment, ownerFieldName);
        checkAttachmentContent(pngBytes, uploadedAttachment);
    }

    /**
     * Test updating the name and description of an attachment.
     * In the name, the file extension cannot be changed.
     * The owner fields are also read only, so you cannot move an attachment to another entity.
     */
    @Test
    public void testUpdateAttachment() {

        EntityModel initialAttachment = new EntityModel();
        initialAttachment.setValue(new StringFieldModel("name", UUID.randomUUID().toString() + ".txt"));
        initialAttachment.setValue(new StringFieldModel("description", UUID.randomUUID().toString()));
        initialAttachment.setValue(new ReferenceFieldModel(ownerFieldName, attachmentParent));

        String attachmentText = UUID.randomUUID().toString();

        // Create the attachment
        Collection<EntityModel> entities =
                octane.attachmentList()
                        .create()
                        .attachment(
                                initialAttachment,
                                new ByteArrayInputStream(attachmentText.getBytes()),
                                "text/plain",
                                "")
                        .execute();

        EntityModel createdAttachment = reloadEntityModel(entities, ownerFieldName);

        //Use this for the update call
        EntityModel updateAttachment = new EntityModel();
        updateAttachment.setValue(createdAttachment.getValue("id"));
        updateAttachment.setValue(createdAttachment.getValue("type"));

        String newDescription = UUID.randomUUID().toString();
        updateAttachment.setValue(new StringFieldModel("description", newDescription));

        //The extension cannot be changed
        String newName = UUID.randomUUID().toString();
        updateAttachment.setValue(new StringFieldModel("name", newName + ".txt"));

        entities =
            octane.attachmentList()
                    .update()
                    .entities(Collections.singletonList(updateAttachment))
                    .execute();

        //Do another fetch after to compare the fields
        EntityModel reloadedAttachment = reloadEntityModel(entities, ownerFieldName);

        //Add the parent back for the check, since it's needed
        updateAttachment.setValue(new ReferenceFieldModel(ownerFieldName, attachmentParent));

        checkAttachmentJson(updateAttachment, reloadedAttachment, ownerFieldName);
    }

    /**
     * Create an attachment, then delete it
     */
    @Test
    public void testDeleteAttachment() {

        EntityModel initialAttachment = new EntityModel();
        initialAttachment.setValue(new StringFieldModel("name", UUID.randomUUID().toString() + ".txt"));
        initialAttachment.setValue(new StringFieldModel("description", UUID.randomUUID().toString()));
        initialAttachment.setValue(new ReferenceFieldModel(ownerFieldName, attachmentParent));

        String attachmentText = UUID.randomUUID().toString();

        Collection<EntityModel> entities =
                octane.attachmentList()
                        .create()
                        .attachment(
                                initialAttachment,
                                new ByteArrayInputStream(attachmentText.getBytes()),
                                "text/plain",
                                "")
                        .execute();

        EntityModel uploadedAttachment = reloadEntityModel(entities, ownerFieldName);

        octane.attachmentList()
                .delete()
                .query(Query.statement("id", QueryMethod.EqualTo, uploadedAttachment.getValue("id").getValue()).build())
                .execute();

        entities =
                octane.attachmentList()
                    .get()
                    .query(Query.statement("id", QueryMethod.EqualTo, uploadedAttachment.getValue("id").getValue()).build())
                    .execute();

        Assert.assertEquals(entities.size(), 0);
    }

    /**
     * Reload entity fields after create
     * @param createResponse response from {@link AttachmentList#create()}
     * @param ownerFieldName name of owner field to fetch
     */
    private EntityModel reloadEntityModel(Collection<EntityModel> createResponse, String ownerFieldName) {
        Assert.assertTrue("One attachment should have been created", createResponse.size() == 1);
        EntityModel entityModel = createResponse.iterator().next();
        String id = entityModel.getValue("id").getValue().toString();

        return octane
                .attachmentList()
                .at(id)
                .get()
                .addFields("name", "description", ownerFieldName)
                .execute();
    }

    /**
     * Compares the initial attachment json to the one that's re-downloaded from the server
     * @param initialEntityModel initial attachment
     * @param uploadedEntityModel re-downloaded attachment
     * @param ownerFieldName name of owner field to compare
     */
    private void checkAttachmentJson(EntityModel initialEntityModel, EntityModel uploadedEntityModel, String ownerFieldName) {
        Assert.assertEquals(
                initialEntityModel.getValue("name").getValue(),
                uploadedEntityModel.getValue("name").getValue());

        Assert.assertEquals(
                initialEntityModel.getValue("description").getValue(),
                uploadedEntityModel.getValue("description").getValue());

        EntityModel uploadedOwner = (EntityModel) initialEntityModel.getValue(ownerFieldName).getValue();
        EntityModel createdOwner = (EntityModel) uploadedEntityModel.getValue(ownerFieldName).getValue();
        Assert.assertEquals(uploadedOwner.getValue("id").getValue(),
                createdOwner.getValue("id").getValue());
    }

    /**
     * Compares the initial attachment content to the one that's re-downloaded from the server
     * @param requestContent content that was uploaded initially, not part of the {@link EntityModel}
     * @param attachmentEntityModel attachment that was created
     */
    private void checkAttachmentContent(byte[] requestContent, EntityModel attachmentEntityModel) {
        String id = attachmentEntityModel.getValue("id").getValue().toString();
        InputStream responseInputStream = octane.attachmentList().at(id).getBinary().execute();
        try {
            byte[] responseContent = ByteStreams.toByteArray(responseInputStream);
            Assert.assertTrue(
                    "Content of fetched attachment must be the same as what was uploaded",
                    Arrays.equals(requestContent, responseContent));

        } catch (IOException e) {
            Assert.fail("Failed to read response attachment, " + e.getMessage());
        }
    }

    /**
     * Generate a random pretty picture, used to verify attachment content
     *
     * @return byte[] containing a BufferedImage
     */
    private static byte[] generateImageAttachmentContent() {
        int size = 400;
        int changeColorIndex = size / 4;

        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        int color = getRandomRgb();

        for (int y = 0; y < size; y++) {
            if (y % changeColorIndex == 0) {
                color = getRandomRgb();
            }
            for (int x = 0; x < size; x++) {
                img.setRGB(x, y, color);
            }
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to generate image");
        }
    }

    /**
     * Get random rgb int for {@link #generateImageAttachmentContent()}
     * @return random rgb represented as an int
     */
    private static int getRandomRgb() {
        int r = (int) (Math.random() * 256); //red
        int g = (int) (Math.random() * 256); //green
        int b = (int) (Math.random() * 256); //blue
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Set a specific default encoding
     */
    private static void setEncoding(String encoding) {
        System.setProperty("file.encoding", encoding);
        try {
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set encoding to" + encoding);
        }
    }
}

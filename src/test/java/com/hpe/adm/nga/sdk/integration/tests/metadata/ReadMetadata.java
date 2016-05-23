package test.java.com.hpe.adm.nga.sdk.integration.tests.metadata;

import main.java.com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import main.java.com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import main.java.com.hpe.adm.nga.sdk.metadata.Metadata;
import test.java.com.hpe.adm.nga.sdk.integration.tests.base.TestBase;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * Created by Dmitry Zavyalov on 08/05/2016.
 */
public class ReadMetadata extends TestBase {

    public ReadMetadata() {
        entityName = "product_areas";
    }

    @Test
    public void testReadMetadataEntities() throws Exception {
        Metadata metadata = nga.metadata();
        Collection<EntityMetadata> entityMetadata = metadata.entities().execute();
        Collection<EntityMetadata> entityMetadataTwoEntities = metadata.entities("defects").execute();
    }

    @Test
    public void testReadMetadataFields() throws Exception {
        Metadata metadata = nga.metadata();
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

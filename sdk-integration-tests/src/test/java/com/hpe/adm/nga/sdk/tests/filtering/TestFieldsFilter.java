package com.hpe.adm.nga.sdk.tests.filtering;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by Guy Guetta on 25/04/2016.
 */
public class TestFieldsFilter extends TestBase {
    public TestFieldsFilter() {
        entityName = "releases";
    }

    @Test
    public void testFieldsFilterWithIdOneField() throws Exception {

        List<String> filterFields = Arrays.asList("attachments");
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName, fields);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        EntityModel getEntity = entityList.at(entityId).get().addFields("attachments").execute();

        Assert.assertTrue(isOnlyRequestedFieldsExistInEntity(getEntity, filterFields));
    }

    @Test
    public void testFieldsFilterWithIdMultipleFields() throws Exception {

        List<String> filterFields = Arrays.asList("attachments", "creation_time", "has_attachments");
        Set<FieldModel> fields = new HashSet<>();
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName, fields);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        EntityModel getEntity = entityList.at(entityId).get().addFields("attachments", "creation_time", "has_attachments").execute();

        Assert.assertTrue(isOnlyRequestedFieldsExistInEntity(getEntity, filterFields));
    }

    @Test
    public void testFieldsFilterMultipleFields() throws Exception {

        List<String> filterFields = Arrays.asList("done_work", "description", "expected_velocity");
        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
        entityList.create().entities(generatedEntity).execute();

        Collection<EntityModel> getEntity = entityList.get().addFields("done_work", "description", "expected_velocity").execute();

        Assert.assertTrue(getIdFromEntityModelCollection(getEntity, filterFields));
    }

    public static boolean isOnlyRequestedFieldsExistInEntity(EntityModel entityModel, List<String> fields) {
        Set<FieldModel> fieldModelSet = entityModel.getValues();
        for (FieldModel fm : fieldModelSet) {
            if (!fm.getName().equals("id") && !fm.getName().equals("type") && !fields.contains(fm.getName())) {
                return false;
            }
        }
        return true;
    }


    public static boolean getIdFromEntityModelCollection(Collection<EntityModel> entityModels, List<String> fields) {
        ArrayList<Boolean> existList = new ArrayList<>();
        entityModels.forEach(entityModel -> existList.add(isOnlyRequestedFieldsExistInEntity(entityModel, fields)));
        return !existList.contains(false);
    }
}

package com.hpe.adm.nga.sdk.utils.generator;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.utils.CommonUtils;

import java.util.*;

/**
 * Created by Guy Guetta on 21/04/2016.
 */
public class DataGenerator {
    public static Collection<EntityModel> generateEntityModel(NGA nga, String entityName, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();
        switch (entityName) {
            case "releases":
                entities.add(generateRelease());
                break;
            case "milestones":
                entities.add(generateMilestone());
                break;
            case "features":
                entities.add(generateFeature(nga, fields));
                break;
            case "defects":
                entities.add(generateDefect(nga, fields));
                break;
            case "product_areas":
                entities.add(generatePA(fields));
                break;
        }
        return entities;
    }

    public static Collection<EntityModel> generateEntityModel(NGA nga, String entityName) throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        return generateEntityModel(nga, entityName, fields);
    }

    public static Collection<EntityModel> generateEntityModelCollection(NGA nga, String entityName, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();

        entities.addAll(generateEntityModel(nga, entityName, fields));
        entities.addAll(generateEntityModel(nga, entityName, fields));
        entities.addAll(generateEntityModel(nga, entityName, fields));

        return entities;
    }

    public static List<String> generateNamesForUpdate() {
        List<String> generatedValues = new ArrayList<>();
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
        return generatedValues;
    }


    private static EntityModel generatePA(Set<FieldModel> fields) {
        Set<FieldModel> parentFields = new HashSet<>();
        parentFields.add(new LongFieldModel("id", 1001l));
        parentFields.add(new StringFieldModel("type", "product_area"));
        EntityModel parent = new EntityModel(parentFields);

        FieldModel name = new StringFieldModel("name", "sdk_pa_" + UUID.randomUUID());
        FieldModel parentField = new ReferenceFieldModel("parent", parent);

        fields.add(name);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    private static EntityModel generateFeature(NGA nga, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> phases = nga.entityList("phases").get().execute();
        EntityModel phase = phases.iterator().next();
        Collection<EntityModel> themes = nga.entityList("themes").get().execute();
        EntityModel theme = themes.iterator().next();

        FieldModel name = new StringFieldModel("name", "sdk_feature_" + UUID.randomUUID());
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
        FieldModel parentField = new ReferenceFieldModel("parent", theme);

        fields.add(name);
        fields.add(phaseField);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    private static EntityModel generateDefect(NGA nga, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> phases = nga.entityList("phases").get().execute();
        EntityModel phase = phases.iterator().next();
        Set<FieldModel> parentFields = new HashSet<>();
        parentFields.add(new LongFieldModel("id", 1001l));
        parentFields.add(new StringFieldModel("type", "work_item_root"));
        EntityModel parent = new EntityModel(parentFields);

        FieldModel name = new StringFieldModel("name", "sdk_defect_" + UUID.randomUUID());
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
        FieldModel parentField = new ReferenceFieldModel("parent", parent);

        Collection<EntityModel> listNodes = nga.entityList("list_nodes").get().execute();
        EntityModel severity = CommonUtils.getEntityWithStringValue(listNodes, "logical_name", "list_node.severity.low");
        FieldModel severityField = new ReferenceFieldModel("severity", severity);

        fields.add(name);
        fields.add(phaseField);
        fields.add(parentField);
        fields.add(severityField);
        return new EntityModel(fields);
    }

    private static EntityModel generateRelease() {
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_release_" + UUID.randomUUID());
        FieldModel startDate = new StringFieldModel("start_date", "2015-03-14T12:00:00Z");
        FieldModel endDate = new StringFieldModel("end_date", "2016-03-14T12:00:00Z");
        fields.add(name);
        fields.add(startDate);
        fields.add(endDate);
        return new EntityModel(fields);
    }

    private static EntityModel generateMilestone() {
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_milestone_" + UUID.randomUUID());
        FieldModel date = new StringFieldModel("date", "2016-03-17T12:00:00Z");
        fields.add(name);
        fields.add(date);
        return new EntityModel(fields);
    }
}

/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.utils.generator;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;
import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.utils.CommonUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

/**
 *
 * Created by Guy Guetta on 21/04/2016.
 */
public class DataGenerator {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static Collection<EntityModel> generateEntityModel(Octane octane, String entityName, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();
        switch (entityName) {
            case "releases":
                entities.add(generateRelease());
                break;
            case "milestones":
                entities.add(generateMilestone());
                break;
            case "features":
                entities.add(generateFeature(octane, fields));
                break;
            case "defects":
                entities.add(generateDefect(octane, fields));
                break;
            case "product_areas":
                entities.add(generatePA(octane, fields));
                break;
        }
        return entities;
    }
    public static Collection<EntityModel> getAllDataForEntities(Collection<EntityModel> entities, Octane octane, String entityType){
        Collection<EntityModel> entitiesWithData = new ArrayList<>();
        for(EntityModel entityModel : entities){
            EntityModel entityModelWithData =  octane.entityList(entityType).at(CommonUtils.getValueFromEntityModel(entityModel, "id")).get().execute();
            entitiesWithData.add(entityModelWithData);
        }
        return entitiesWithData;
    }

    public static Collection<EntityModel> generateEntityModel(Octane octane, String entityName) throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        return generateEntityModel(octane, entityName, fields);
    }

    public static Collection<EntityModel> generateEntityModelCollection(Octane octane, String entityName) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();

        entities.addAll(generateEntityModel(octane, entityName));
        entities.addAll(generateEntityModel(octane, entityName));
        entities.addAll(generateEntityModel(octane, entityName));

        return entities;
    }

    public static List<String> generateNamesForUpdate() {
        List<String> generatedValues = new ArrayList<>();
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
        return generatedValues;
    }


    private static EntityModel generatePA(Octane octane, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> pas = octane.entityList("product_areas").get().execute();
        EntityModel parentEntity = CommonUtils.getEntityWithStringValue(pas, "parent", null);
        String parentId = CommonUtils.getIdFromEntityModel(parentEntity);
        Set<FieldModel> parentFields = new HashSet<>();
        parentFields.add(new StringFieldModel("id", parentId));
        parentFields.add(new StringFieldModel("type", "product_area"));
        EntityModel parent = new EntityModel(parentFields);

        FieldModel name = new StringFieldModel("name", "sdk_pa_" + UUID.randomUUID());
        FieldModel parentField = new ReferenceFieldModel("parent", parent);

        fields.add(name);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    private static EntityModel generateFeature(Octane octane, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> phases = octane.entityList("phases").get().execute();
        EntityModel phase = phases.iterator().next();
        Collection<EntityModel> themes = octane.entityList("themes").get().execute();
        EntityModel theme = themes.iterator().next();

        FieldModel name = new StringFieldModel("name", "sdk_feature_" + UUID.randomUUID());
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
        FieldModel parentField = new ReferenceFieldModel("parent", theme);

        fields.add(name);
        fields.add(phaseField);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    private static EntityModel generateDefect(Octane octane, Set<FieldModel> fields) throws Exception {
        Query query = Query.statement("subtype", QueryMethod.EqualTo, "work_item_root").build();
        Collection<EntityModel> roots = octane.entityList("work_items").get().query(query).execute();
        EntityModel root = roots.iterator().next();
        FieldModel parentField = new ReferenceFieldModel("parent", root);

        Collection<EntityModel> users = octane.entityList("workspace_users").get().execute();
        EntityModel user = users.iterator().next();
        FieldModel author = new ReferenceFieldModel("author", user);

        Query query2 = Query.statement("entity", QueryMethod.EqualTo, "defect").build();
        Collection<EntityModel> phases = octane.entityList("phases").get().query(query2).execute();
        EntityModel phase = phases.iterator().next();
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);

        FieldModel name = new StringFieldModel("name", "sdk_defect_" + UUID.randomUUID());

        Collection<EntityModel> listNodes = octane.entityList("list_nodes").get().execute();
        EntityModel severity = CommonUtils.getEntityWithStringValue(listNodes, "logical_name", "list_node.severity.low");
        FieldModel severityField = new ReferenceFieldModel("severity", severity);

        fields.add(name);
        fields.add(author);
        fields.add(phaseField);
        fields.add(parentField);
        fields.add(severityField);
        return new EntityModel(fields);
    }

    private static EntityModel generateRelease() throws ParseException {
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_release_" + UUID.randomUUID());
        FieldModel startDate = new DateFieldModel("start_date", ZonedDateTime.parse("2015-03-14T12:00:00Z"));
        FieldModel endDate = new DateFieldModel("end_date", ZonedDateTime.parse("2016-03-14T12:00:00Z"));
        fields.add(name);
        fields.add(startDate);
        fields.add(endDate);
        return new EntityModel(fields);
    }

    private static EntityModel generateMilestone() throws ParseException {
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_milestone_" + UUID.randomUUID());
        FieldModel date = new DateFieldModel("date", ZonedDateTime.parse("2016-03-17T12:00:00Z"));
        fields.add(name);
        fields.add(date);
        return new EntityModel(fields);
    }
}

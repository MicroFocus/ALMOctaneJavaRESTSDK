package com.hpe.adm.nga.sdk.utils.generator;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;

import java.util.*;

/**
 * Created by Guy Guetta on 21/04/2016.
 */
public class DataGenerator {
    public static Collection<EntityModel> generateEntityModel(NGA nga, String entityName) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();
        switch(entityName) {
            case "releases":
                entities.add(generateRelease());
                break;
            case "milestones":
                entities.add(generateMilestone());
                break;
            case "features":
                entities.add(generateFeature(nga));
                break;
           case "themes":
                entities.add(generateTheme(nga));
               break;
//            case "work_items":
//               entities.add(generateStories(nga));
//               break;
//            //case "tests":
//              //  entities.add(generateTests(nga));
//              //  break;
//            case "defects":
//               entities.add(generateDefects());
//                break;
//            case "teams":
//                entities.add(generateTeam());
//                break;
        }
        return entities;
    }


    public static Collection<EntityModel> generateEntityModelCollection(NGA nga, String entityName) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();

        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));

        return entities;
    }

    private static EntityModel generateFeature(NGA nga) throws Exception{
    	
        Collection<EntityModel> phases = nga.entityList("phases").get().execute();
        EntityModel phase = phases.iterator().next();
        
        Collection<EntityModel> themes = nga.entityList("themes").get().execute();
        EntityModel theme = themes.iterator().next();

        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_feature_" + UUID.randomUUID());
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
        FieldModel parentField = new ReferenceFieldModel("parent", theme);

        fields.add(name);
        fields.add(phaseField);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    public static EntityModel generateRelease(){
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_release_" + UUID.randomUUID());
        FieldModel startDate = new StringFieldModel("start_date","2015-03-14T12:00:00Z");
        FieldModel endDate = new StringFieldModel("end_date", "2016-03-14T12:00:00Z");
        fields.add(name);
        fields.add(startDate);
        fields.add(endDate);
        return new EntityModel(fields);
    }
    
    

    public static EntityModel generateMilestone(){
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_milestone_" + UUID.randomUUID());
        FieldModel date = new StringFieldModel("date", "2016-03-17T12:00:00Z");
        fields.add(name);
        fields.add(date);
        return new EntityModel(fields);
    }

    public static List<String> generateNamesForUpdate() {
        List<String> generatedValues = new ArrayList<>();
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
       
        return generatedValues;
    }
    
    private static EntityModel generateTheme(NGA nga) throws Exception{
    	Collection<EntityModel> phases = nga.entityList("phases").get().execute();
    	EntityModel phase = phases.iterator().next();
    	Query rootQuery = new Query().field("logical_name").equal(new String("work_item.root")).build();
    	Collection<EntityModel> root = nga.entityList("work_items").get().query(rootQuery).execute();
    	EntityModel workItemRoot = root.iterator().next();
        Set<FieldModel> fields = new HashSet<>();
        FieldModel storypoints = new  LongFieldModel("story_points", new Long(12345678));
        FieldModel rank = new LongFieldModel("rank",new Long(123));
        FieldModel description = new StringFieldModel("description", "korentec_Desc");
        FieldModel name = new StringFieldModel("name", "yosi" + UUID.randomUUID());
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
        FieldModel parentField = new ReferenceFieldModel("parent",workItemRoot);
        fields.add(storypoints);
        fields.add(rank);
        fields.add(name);
        fields.add(description);
        fields.add(phaseField);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    private static EntityModel generateStories(NGA nga) throws Exception{
    	Collection<EntityModel> phases = nga.entityList("phases").get().execute();
    	EntityModel phase = phases.iterator().next();
    	Collection<EntityModel> features = nga.entityList("features").get().execute();
    	EntityModel feature = features.iterator().next();
    	  
        Set<FieldModel> fields = new HashSet<>();
        FieldModel storypoint = new  LongFieldModel("story_point", new Long(753112512));
        FieldModel rank = new LongFieldModel("rank",new Long(123));
        FieldModel description = new StringFieldModel("description", "123ab");
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
        FieldModel parentField = new ReferenceFieldModel("parent", feature);
        fields.add(storypoint);
        fields.add(rank);
        fields.add(description);
        return new EntityModel(fields);
    }
    
       
    public static EntityModel generateDefects() throws Exception{
      	
        Set<FieldModel> fields = new HashSet<>();
        FieldModel owner = new  StringFieldModel("owner","KorenOwner");
        FieldModel sprint = new LongFieldModel("sprint",new Long(5));
        FieldModel priority = new LongFieldModel("priority",new Long(1));
        fields.add(owner);
        fields.add(sprint);
        fields.add(priority);
        return new EntityModel(fields);
    }
    public static EntityModel generateTeam(){
        Set<FieldModel> fields = new HashSet<>();
        FieldModel release = new StringFieldModel("release", "sdk_release2" + UUID.randomUUID());
        FieldModel teamlead = new  StringFieldModel("team_lead","Text");
        fields.add(release);
        fields.add(teamlead);
        return new EntityModel(fields);
    }
    
    
}

package com.hpe.adm.nga.sdk.crud;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.base.TestBase;
import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GetEntity extends TestBase {

	public GetEntity() {
		entityName = "themes";
	}
	
	@Test
	public void testGetExecute() {
		
	
		try{
			final EntityList defects = nga.entityList("defects"); // product_areas
			int maxEntitieslimit = 10;
			
			Collection<EntityModel> ColEntityList = defects.get().addFields("version_stamp", "item_type").limit(maxEntitieslimit).offset(1).addOrderBy("version_stamp",false).execute();
			assertNotNull(ColEntityList);
			assertSame(ColEntityList.size(),maxEntitieslimit);
			
		}
		catch (Exception e) {
			fail("Exception:"+e);
		}
	
	
	}
	

	@Test
	public void testGetEntitiesBylimit()throws Exception {
		 
		    int maxEntitieslimit = 6;
		    Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
	        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
//	        List<Integer> getEntitys = CommonUtils.getIdFromEntityModelCollection(entityModels);
	        Query query = new Query.Field("rank").equal(new Long(123)).build();
	        Collection<EntityModel> getEntity = entityList.get().query(query).limit(maxEntitieslimit).execute();
	        
	        assertSame(getEntity.size(),maxEntitieslimit);
	}
	
	
	@Test
	public void testGetEntitiesOffset()throws Exception {
		 
		    int maxEntitieslimit = 2;
		    Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
	        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
//	        List<Integer> getEntitys = CommonUtils.getIdFromEntityModelCollection(entityModels);
	        Query query = new Query.Field("rank").equal(new Long(123)).build();
	        Collection<EntityModel> getEntity = entityList.get().query(query).limit(1).execute();
	        Iterator<EntityModel> entIter = getEntity.iterator();
	        EntityModel firstEnt = entIter.next();
	        Set<FieldModel> fieldModelSet = firstEnt.getValues();
	        int firstId = 0;
	        for (FieldModel fm : fieldModelSet) {
	            if (fm.getName().equals("id")) {
	                firstId = Integer.parseInt(fm.getValue().toString());
	                break;
	            }
	        }
	        getEntity = entityList.get().query(query).offset(2).limit(1).execute();
	        entIter = getEntity.iterator();
	        EntityModel secnEnt = entIter.next();
	        Set<FieldModel> secondModelSet = secnEnt.getValues();
	        int secId = 0;
	        for (FieldModel fm : secondModelSet) {
	            if (fm.getName().equals("id")) {
	            	secId = Integer.parseInt(fm.getValue().toString());
	            	break;
	            }
	        }
	        assertFalse(firstId == secId);
	}
	
	
	
	@Test
	public void testGetEntitiesOrderBy() throws Exception {
		int TST_NUMBER = 4;
		String CONST_NAME = "testOrderBy";
		Collection<EntityModel> testOrderCollection = new ArrayList<>();
		Query nameQuery = new Query.Field("name").equal(new String(CONST_NAME)).build();
		// delete records before begin
		nga.entityList("themes").delete().query(nameQuery).execute();
		
		Collection<EntityModel> phases = nga.entityList("phases").get().execute();
    	EntityModel phase = phases.iterator().next();
    	Query rootQuery = new Query().field("logical_name").equal(new String("work_item.root")).build();
    	Collection<EntityModel> root = nga.entityList("work_items").get().query(rootQuery).execute();
    	EntityModel workItemRoot = root.iterator().next();
		
		for (int i = 0; i < TST_NUMBER; i++){
			 Set<FieldModel> fields = new HashSet<>();		        
		        FieldModel rank = new LongFieldModel("rank",new Long(TST_NUMBER - i));
		        FieldModel description = new StringFieldModel("description", "korentec_Desc");
		        FieldModel name = new StringFieldModel("name", CONST_NAME);
		        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
		        FieldModel parentField = new ReferenceFieldModel("parent",workItemRoot);
		        fields.add(rank);
		        fields.add(name);
		        fields.add(description);
		        fields.add(phaseField);
		        fields.add(parentField);
		        testOrderCollection.add(new EntityModel(fields)); 
		}
		entityList.create().entities(testOrderCollection).execute();
		// get the entities queried by "name" ordered by "rank"
		Collection<EntityModel> getEntity = entityList.get().query(nameQuery).addOrderBy("rank", true).execute();
		
		ArrayList<Integer> rankList = new ArrayList<>();
		getEntity.forEach(ent -> {
			Set<FieldModel> fieldModelSet = ent.getValues();
	        for (FieldModel fm : fieldModelSet) {
	            if (fm.getName().equals("rank")) {
	            	rankList.add(Integer.parseInt(fm.getValue().toString()));	                
	                break;
	            }
	        }
		});
		
		int prevRank = -1;
		for (int i = 0; i < rankList.size() -1; i++){
			assertTrue(rankList.get(i) > prevRank);
			prevRank = rankList.get(i);
		}
       
	}
	@Test
	
	public void testExceptions(){
		final EntityList defects = nga.entityList("defects");
		try {
			Collection<EntityModel> ColEntityList = defects.get().addFields("wrong_field").execute();
			fail("Supposed to fail with unknown field request");
		} catch (Exception e) {
			
		}
	}
}
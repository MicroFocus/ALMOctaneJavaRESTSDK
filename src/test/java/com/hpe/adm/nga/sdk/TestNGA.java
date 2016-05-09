package com.hpe.adm.nga.tests;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.attachments.AttachmentList.Attachments;
import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import com.hpe.adm.nga.sdk.exception.NgaException;
import com.hpe.adm.nga.sdk.exception.NgaPartialException;
import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;



/**
 * Created by brucesp on 22/02/2016.
 */
public class TestNGA {

	public static void main(String[] args) {
		
		final String MY_APP_ID = "moris@korentec.co.il";
	    final String MY_APP_SECRET = "Moris4095";
	    final String CREATE_DEMO = "{\"total_count\":158,\"data\":[{\"type\":\"defect\",\"parent\":{\"type\":\"feature\",\"id\":1010},\"defect_root_level\":{\"type\":\"list_node\",\"id\":1034},\"item_type\":{\"type\":\"list_node\",\"id\":1026},\"release\":{\"type\":\"release\",\"id\":1005},\"sprint\":{\"type\":\"sprint\",\"id\":1019},\"description\":\"<html><body>\\nGo sur onderen pe ozdedte wumarda jemduris zah imun diegezo zeklup ob numit sacibfil sautma temata. Zomake fub po kigam uf sebapzal sat se kubuw enjane hezgauhi awmaj. Fi piko sinola zosfe mewokinuw mezzer otanaok onegomeg sincepo vojuj kigoc rag dutuc gawpogog zaw vib. Ki ficnelit af wa podpuk de docdu leghi ke kokorce arfeuz genob vasub ve ku gevis fug. Pepib jekwumi oseludre rik mofovize bote riz uje ceplitpo zuib amdo gatah octuf robzir owu uhove nu. Zargasoz sa oro jipuko sinmiz muob okewevdut zibtoka jikre jo isuevo nevgi icvehum segmaj ivgiwi cu vamhoaz.\\n</body></html>\",\"fixed_in_build\":1,\"detected_in_release\":{\"type\":\"release\",\"id\":1003},\"item_origin\":{\"type\":\"list_node\",\"id\":1023},\"detected_by\":{\"type\":\"workspace_user\",\"id\":1001},\"qa_owner\":{\"type\":\"workspace_user\",\"id\":1004},\"closed_on\":\"2016-03-05T20:57:18Z\",\"rank\":3,\"id\":1,\"phase\":{\"type\":\"phase\",\"id\":1006},\"severity\":{\"type\":\"list_node\",\"id\":1005},\"owner\":{\"type\":\"workspace_user\",\"id\":1006},\"fixed_on\":\"2016-03-05T20:57:18Z\",\"author\":{\"type\":\"workspace_user\",\"id\":1004},\"story_points\":9,\"product_areas\":{\"total_count\":3,\"data\":[{\"type\":\"product_area\",\"id\":1027},{\"type\":\"product_area\",\"id\":1034},{\"type\":\"product_area\",\"id\":1065}]},\"team\":null,\"priority\":{\"type\":\"list_node\",\"id\":1017},\"user_tags\":{\"total_count\":0,\"data\":[]},\"taxonomies\":{\"total_count\":3,\"data\":[{\"type\":\"taxonomy_item_node\",\"id\":1010},{\"type\":\"taxonomy_item_node\",\"id\":1019},{\"type\":\"taxonomy_item_node\",\"id\":1053}]},\"name\":\"moris99\",\"detected_in_build\":1},{\"type\":\"defect\",\"parent\":{\"type\":\"feature\",\"id\":3021},\"defect_root_level\":{\"type\":\"list_node\",\"id\":1035},\"item_type\":{\"type\":\"list_node\",\"id\":1025},\"release\":{\"type\":\"release\",\"id\":3002},\"sprint\":{\"type\":\"sprint\",\"id\":3006},\"description\":\"<html><body>\\nGuit bi emeaza boconbuj saudmof pi buzlut reb uwiza ocabo mu fot amsorib seraj. Juzu ilsasguc gemhaora tihta rihup tog oci ehfuk bazube cifi iw orcu acrogu kuzjiw pupgugbe cabjese nego wuv. Ugukuwaf su sik wo jipum cef satsekcen wodoage azomimkip daneew fimwir rog junzunom ezlieli opide si. Wajgaj zurrir we daga nezke uwbi abwabmen lade ag jerowog wapoeko bugkos sowmiog ezaawiim. Radac deowe map pima kubovu rut detoco puadbif tuzorzid paje nukpohha hecdupja zogiim curvinlo fop fanocu. Kaocu zozouw tupja jogmuafi ekdu mo lec cu depmidbob seilu nicotis mo sesavkic puckiv edikive zowsivaw igve. Nodo kugtevi zo jewjiz jet wu meroc niz toledil asuziv nigij gosokome goaha joce soh tu po celidur.\\n</body></html>\",\"fixed_in_build\":9,\"detected_in_release\":{\"type\":\"release\",\"id\":3002},\"item_origin\":{\"type\":\"list_node\",\"id\":1020},\"detected_by\":{\"type\":\"workspace_user\",\"id\":1001},\"qa_owner\":{\"type\":\"workspace_user\",\"id\":3003},\"closed_on\":\"2016-03-15T20:45:34Z\",\"rank\":12,\"id\":2,\"phase\":{\"type\":\"phase\",\"id\":1004},\"severity\":{\"type\":\"list_node\",\"id\":1006},\"owner\":{\"type\":\"workspace_user\",\"id\":3005},\"fixed_on\":\"2016-03-15T20:45:34Z\",\"author\":{\"type\":\"workspace_user\",\"id\":3003},\"story_points\":18,\"product_areas\":{\"total_count\":3,\"data\":[{\"type\":\"product_area\",\"id\":3032},{\"type\":\"product_area\",\"id\":3062},{\"type\":\"product_area\",\"id\":3014}]},\"team\":null,\"priority\":{\"type\":\"list_node\",\"id\":1015},\"user_tags\":{\"total_count\":0,\"data\":[]},\"taxonomies\":{\"total_count\":3,\"data\":[{\"type\":\"taxonomy_item_node\",\"id\":1044},{\"type\":\"taxonomy_item_node\",\"id\":1043},{\"type\":\"taxonomy_item_node\",\"id\":1016}]},\"name\":\"moris22\",\"detected_in_build\":12}],\"exceeds_total_count\":false}\n";
	    final String CREATE_DEMO2 = "{\"total_count\":22,\"data\":[{\"type\":\"defect\",\"parent\":{\"type\":\"feature\",\"id\":7001},\"defect_root_level\":{\"type\":\"list_node\",\"id\":1031},\"release\":{\"type\":\"release\",\"id\":3001},\"item_type\":{\"type\":\"list_node\",\"id\":1026},\"sprint\":{\"type\":\"sprint\",\"id\":3001},\"description\":\"desc1\",\"fixed_in_build\":5,\"detected_in_release\":{\"type\":\"release\",\"id\":3005},\"item_origin\":{\"type\":\"list_node\",\"id\":1020},\"detected_by\":{\"type\":\"workspace_user\",\"id\":1001},\"qa_owner\":{\"type\":\"workspace_user\",\"id\":1001},\"closed_on\":\"2016-04-04T11:42:21Z\",\"rank\":17,\"id\":1,\"phase\":{\"type\":\"phase\",\"id\":1007},\"severity\":{\"type\":\"list_node\",\"id\":1004},\"owner\":{\"type\":\"workspace_user\",\"id\":1001},\"fixed_on\":\"2016-04-04T11:42:21Z\",\"author\":{\"type\":\"workspace_user\",\"id\":1001},\"story_points\":0,\"product_areas\":{\"total_count\":3,\"data\":[{\"type\":\"product_area\",\"id\":5018},{\"type\":\"product_area\",\"id\":4034},{\"type\":\"product_area\",\"id\":5015}]},\"team\":null,\"priority\":{\"type\":\"list_node\",\"id\":1015},\"user_tags\":{\"total_count\":0,\"data\":[]},\"taxonomies\":{\"total_count\":3,\"data\":[{\"type\":\"taxonomy_item_node\",\"id\":1025},{\"type\":\"taxonomy_item_node\",\"id\":1017},{\"type\":\"taxonomy_item_node\",\"id\":1019}]},\"name\":\"moris1\",\"detected_in_build\":19},{\"type\":\"defect\",\"parent\":{\"type\":\"feature\",\"id\":7003},\"defect_root_level\":{\"type\":\"list_node\",\"id\":1031},\"release\":{\"type\":\"release\",\"id\":3005},\"item_type\":{\"type\":\"list_node\",\"id\":1025},\"sprint\":{\"type\":\"sprint\",\"id\":3013},\"description\":\"desc2\",\"fixed_in_build\":0,\"detected_in_release\":{\"type\":\"release\",\"id\":3003},\"item_origin\":{\"type\":\"list_node\",\"id\":1021},\"detected_by\":{\"type\":\"workspace_user\",\"id\":1001},\"qa_owner\":{\"type\":\"workspace_user\",\"id\":1001},\"closed_on\":\"2016-04-04T11:42:20Z\",\"rank\":0,\"id\":2,\"phase\":{\"type\":\"phase\",\"id\":1004},\"severity\":{\"type\":\"list_node\",\"id\":1006},\"owner\":{\"type\":\"workspace_user\",\"id\":1001},\"fixed_on\":\"2016-04-04T11:42:20Z\",\"author\":{\"type\":\"workspace_user\",\"id\":1001},\"story_points\":0,\"product_areas\":{\"total_count\":3,\"data\":[{\"type\":\"product_area\",\"id\":4045},{\"type\":\"product_area\",\"id\":5014},{\"type\":\"product_area\",\"id\":4053}]},\"team\":null,\"priority\":{\"type\":\"list_node\",\"id\":1014},\"user_tags\":{\"total_count\":0,\"data\":[]},\"taxonomies\":{\"total_count\":3,\"data\":[{\"type\":\"taxonomy_item_node\",\"id\":1017},{\"type\":\"taxonomy_item_node\",\"id\":1002},{\"type\":\"taxonomy_item_node\",\"id\":1012}]},\"name\":\"moris2\",\"detected_in_build\":10}],\"exceeds_total_count\":false}";
	    final String CREATE_DEMO3 = "{\"data\":[{\"parent\":{\"id\":7002,\"type\":\"feature\"},\"phase\":{\"id\":1007,\"type\":\"phase\"},\"severity\":{\"id\":1004,\"type\":\"list_node\"},\"id\":1,\"name\":\"moris2\"}],\"total_count\":1}";
	    final Logger logger = LogManager.getLogger(TestNGA.class.getName());
	   
	   
		NGA nga;
		try {
			nga = (new NGA.Builder(
					new BasicAuthorisation(){
						@Override
						public String getUsername(){
							
							return MY_APP_ID;
						}
						
						@Override
						public String getPassword(){
							
							return MY_APP_SECRET;
						}
						
					}
					)).Server("https://mqast001pngx.saas.hpe.com").sharedSpace(4063).workSpace(1002).build();
			
			if (nga==null)
			{
				throw new RuntimeException("NGA.Builder Failed");
			}
			
			final EntityList defects = nga.entityList("defects"); // product_areas

			// ************************** TESTED ****************************
			// EntityList examples
			Collection<EntityModel> ColEntityList =  defects.get().execute();
			ColEntityList = defects.get().addFields("version_stamp", "item_type").limit(10).offset(1).addOrderBy("version_stamp",false).execute();
			Collection<EntityModel> entityModelsIn = defects.testGetEntityModels(CREATE_DEMO3);
			Collection<EntityModel> entityModelsOut = defects.create().entities(entityModelsIn).execute();
			
			String newJasonDemo = CREATE_DEMO3.replace("\"id\":1,", "\"id\":6030,");
			entityModelsIn = defects.testGetEntityModels(newJasonDemo);
			
			ColEntityList = defects.update().entities(entityModelsIn).execute();
			
			Query query = new Query.Field("creation_time").less(new Date()).or().field("id").equal(new String("5028")).or().field("id").equal(new String("5015")).build();
			ColEntityList = defects.get().query(query).execute();
			
			EntityModel entityModel  = defects.at(8024).get().addFields("description").execute();
			entityModel = defects.at(8024).update().entity(entityModel).execute();
			
			query = new Query.Field("id",true).equal(new Long("8024")).build();
			ColEntityList = defects.get().query(query).execute();
			
			query = new Query.Field("user_tags").equal(new Query.Field("id").equal(new String("8024")).or().field("id").equal(new String("8026")).build()).build();
			ColEntityList= defects.get().query(query).execute();
			
			query = new Query.Field("team").equal(null).build();
			ColEntityList = defects.get().query(query).execute();
			
			// Entityies examples
			entityModel  = defects.at(8024).get().addFields("description").execute();
			entityModel = defects.at(8024).update().entity(entityModel).execute();
			//defects.at(5044).delete().execute();
			
			
			
			// metadata
			Metadata metadata = nga.metadata();
			// all entities
			Collection<EntityMetadata> colEntityMetadata  = metadata.entities().execute();
			colEntityMetadata  = metadata.entities("runs","taxonomy_item_node","test_suite","theme").execute();
			
			
			// fields
			Collection<FieldMetadata> colFieldMetadat = metadata.fields().execute();
						// only fields of defects and tests
			colFieldMetadat = metadata.fields("run", "business_rule").execute();
					
			// Attachmnts
			AttachmentList attachmnts = nga.AttachmentList();
			Collection<EntityModel> collEntityModel = attachmnts.get().execute();
			
			
			entityModelsIn = new HashSet<EntityModel>();
			Set<FieldModel> data = new HashSet<FieldModel>();
			FieldModel FieldModel1 = new StringFieldModel("type", "attachment");
			FieldModel FieldModel2 = new StringFieldModel("description", "description test1");
			FieldModel FieldModel3 = new LongFieldModel("id",new Long(5001));
			data.add(FieldModel1);
			data.add(FieldModel2);
			data.add(FieldModel3);
			entityModel = new EntityModel(data);
			entityModelsIn.add(entityModel);
			FieldModel1 = new StringFieldModel("type", "attachment");
			FieldModel2 = new StringFieldModel("description", "description test2");
			FieldModel3 = new LongFieldModel("id",new Long(5002));
			data = new HashSet<FieldModel>();
			data.add(FieldModel1);
			data.add(FieldModel2);
			data.add(FieldModel3);
			entityModel = new EntityModel(data);
			entityModelsIn.add(entityModel);
			
			
			collEntityModel = attachmnts.update().entities(entityModelsIn).execute();
			
			
			
			// get entity data of attachmnet with id=1001 
			EntityModel entityModel2 = attachmnts.at(5001).get().execute();
			data = entityModel2.getValue();
			String strFileName = "";
			for (FieldModel fieldModel : data) {

				if (fieldModel.getName().equals("name"))
				{
					strFileName = ((StringFieldModel)(fieldModel)).getValue();
					break;
				}	
		     }
			// get binary data of attachmnet with id=1001 
			InputStream is = attachmnts.at(5001).getBinary().execute();
			FileOutputStream fos = new FileOutputStream(new File(strFileName)); 
			int inByte;
			while((inByte = is.read()) != -1)
			     fos.write(inByte);
			is.close();
			fos.close();
			
			entityModel = attachmnts.at(5001).update().entity(entityModel).execute();
			entityModelsOut = attachmnts.create().entities(entityModelsIn,strFileName).execute();
			//attachmnts.at(5006).delete().execute();
			//attachmnts.delete().execute();
			//defects.delete().execute();
		
		} 
		catch (Exception e) {
			
			if (e instanceof NgaException)
			{
				ErrorModel errorModel = ((NgaException) e).getError();
			}
			else if(e instanceof NgaPartialException){
				
				Collection<EntityModel> entities = ((NgaPartialException)e).getEntitiesModels();
				Collection<ErrorModel> errors = ((NgaPartialException)e).getErrorModels();
			}
			
		logger.fatal("Error description",e);
			
		}
			

}
	
	

}

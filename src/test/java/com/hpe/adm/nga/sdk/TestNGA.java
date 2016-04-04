package com.hpe.adm.nga.test;

import com.hpe.adm.nga.sdk.EntityList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel.ReferenceModel;

import java.io.File;
import java.io.IOException;
import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;

/**
 * Created by brucesp on 22/02/2016.
 */
public class TestNGA {

	public static void main(String[] args) {
		
		final String MY_APP_ID = "morris@korentec.co.il";
	    final String MY_APP_SECRET = "korentec";
	    final String CREATE_DEMO = "{\"total_count\":158,\"data\":[{\"type\":\"defect\",\"parent\":{\"type\":\"feature\",\"id\":1010},\"defect_root_level\":{\"type\":\"list_node\",\"id\":1034},\"item_type\":{\"type\":\"list_node\",\"id\":1026},\"release\":{\"type\":\"release\",\"id\":1005},\"sprint\":{\"type\":\"sprint\",\"id\":1019},\"description\":\"<html><body>\\nGo sur onderen pe ozdedte wumarda jemduris zah imun diegezo zeklup ob numit sacibfil sautma temata. Zomake fub po kigam uf sebapzal sat se kubuw enjane hezgauhi awmaj. Fi piko sinola zosfe mewokinuw mezzer otanaok onegomeg sincepo vojuj kigoc rag dutuc gawpogog zaw vib. Ki ficnelit af wa podpuk de docdu leghi ke kokorce arfeuz genob vasub ve ku gevis fug. Pepib jekwumi oseludre rik mofovize bote riz uje ceplitpo zuib amdo gatah octuf robzir owu uhove nu. Zargasoz sa oro jipuko sinmiz muob okewevdut zibtoka jikre jo isuevo nevgi icvehum segmaj ivgiwi cu vamhoaz.\\n</body></html>\",\"fixed_in_build\":1,\"detected_in_release\":{\"type\":\"release\",\"id\":1003},\"item_origin\":{\"type\":\"list_node\",\"id\":1023},\"detected_by\":{\"type\":\"workspace_user\",\"id\":1001},\"qa_owner\":{\"type\":\"workspace_user\",\"id\":1004},\"closed_on\":\"2016-03-05T20:57:18Z\",\"rank\":3,\"id\":3053,\"phase\":{\"type\":\"phase\",\"id\":1006},\"severity\":{\"type\":\"list_node\",\"id\":1005},\"owner\":{\"type\":\"workspace_user\",\"id\":1006},\"fixed_on\":\"2016-03-05T20:57:18Z\",\"author\":{\"type\":\"workspace_user\",\"id\":1004},\"story_points\":9,\"product_areas\":{\"total_count\":3,\"data\":[{\"type\":\"product_area\",\"id\":1027},{\"type\":\"product_area\",\"id\":1034},{\"type\":\"product_area\",\"id\":1065}]},\"team\":null,\"priority\":{\"type\":\"list_node\",\"id\":1017},\"user_tags\":{\"total_count\":0,\"data\":[]},\"taxonomies\":{\"total_count\":3,\"data\":[{\"type\":\"taxonomy_item_node\",\"id\":1010},{\"type\":\"taxonomy_item_node\",\"id\":1019},{\"type\":\"taxonomy_item_node\",\"id\":1053}]},\"name\":\"moris99\",\"detected_in_build\":1},{\"type\":\"defect\",\"parent\":{\"type\":\"feature\",\"id\":3021},\"defect_root_level\":{\"type\":\"list_node\",\"id\":1035},\"item_type\":{\"type\":\"list_node\",\"id\":1025},\"release\":{\"type\":\"release\",\"id\":3002},\"sprint\":{\"type\":\"sprint\",\"id\":3006},\"description\":\"<html><body>\\nGuit bi emeaza boconbuj saudmof pi buzlut reb uwiza ocabo mu fot amsorib seraj. Juzu ilsasguc gemhaora tihta rihup tog oci ehfuk bazube cifi iw orcu acrogu kuzjiw pupgugbe cabjese nego wuv. Ugukuwaf su sik wo jipum cef satsekcen wodoage azomimkip daneew fimwir rog junzunom ezlieli opide si. Wajgaj zurrir we daga nezke uwbi abwabmen lade ag jerowog wapoeko bugkos sowmiog ezaawiim. Radac deowe map pima kubovu rut detoco puadbif tuzorzid paje nukpohha hecdupja zogiim curvinlo fop fanocu. Kaocu zozouw tupja jogmuafi ekdu mo lec cu depmidbob seilu nicotis mo sesavkic puckiv edikive zowsivaw igve. Nodo kugtevi zo jewjiz jet wu meroc niz toledil asuziv nigij gosokome goaha joce soh tu po celidur.\\n</body></html>\",\"fixed_in_build\":9,\"detected_in_release\":{\"type\":\"release\",\"id\":3002},\"item_origin\":{\"type\":\"list_node\",\"id\":1020},\"detected_by\":{\"type\":\"workspace_user\",\"id\":1001},\"qa_owner\":{\"type\":\"workspace_user\",\"id\":3003},\"closed_on\":\"2016-03-15T20:45:34Z\",\"rank\":12,\"id\":3043,\"phase\":{\"type\":\"phase\",\"id\":1004},\"severity\":{\"type\":\"list_node\",\"id\":1006},\"owner\":{\"type\":\"workspace_user\",\"id\":3005},\"fixed_on\":\"2016-03-15T20:45:34Z\",\"author\":{\"type\":\"workspace_user\",\"id\":3003},\"story_points\":18,\"product_areas\":{\"total_count\":3,\"data\":[{\"type\":\"product_area\",\"id\":3032},{\"type\":\"product_area\",\"id\":3062},{\"type\":\"product_area\",\"id\":3014}]},\"team\":null,\"priority\":{\"type\":\"list_node\",\"id\":1015},\"user_tags\":{\"total_count\":0,\"data\":[]},\"taxonomies\":{\"total_count\":3,\"data\":[{\"type\":\"taxonomy_item_node\",\"id\":1044},{\"type\":\"taxonomy_item_node\",\"id\":1043},{\"type\":\"taxonomy_item_node\",\"id\":1016}]},\"name\":\"moris22\",\"detected_in_build\":12}],\"exceeds_total_count\":false}\n";
	    
	   
		NGA nga;
		try {
			nga = new NGA.Builder(
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
					).Server("https://sandbox.nextgenalm.com").sharedSpace(1001).workSpace(1002).build();
		
			final EntityList defects = nga.entityList("defects");


			// EntityList examples
			Collection<EntityModel> ColEntityList =  defects.get().execute();
			ColEntityList = defects.get().addFields("version_stamp", "item_type").limit(10).offset(1).addOrderBy("version_stamp",false).execute();
			Collection<EntityModel> entityModelsIn = defects.testGetEntityModels(CREATE_DEMO);
			Collection<EntityModel> entityModelsOut = defects.create().entities(entityModelsIn).execute();
			ColEntityList = defects.update().entities(entityModelsIn).execute();
			
			/*Query query = new Query.Field("creation_time").less(new Date()).build();//or().field("id").equal(new String("3053")).or().field("id").equal(new String("3095")).build();
			ColEntityList = defects.get().query(query).execute();
			
			query = new Query.Field("date").less(new Query.Field("id").equal(new String("3053")).or().field("id").equal(new String("3053")).build()).build();
			ColEntityList = defects.get().query(query).execute();
			defects.get().query(query).execute();*/
			
			// Entityies examples
			EntityModel entityModel  = defects.at(3053).get().addFields("description").execute();
			entityModel = defects.at(3053).update().entity(entityModel).execute();
			defects.at(3492).delete().execute();
			

			// Attachmnts
			/*final AttachmentList attachmnts = nga.AttachmentList();	
			// get all attachemnts
			attachmnts.get().execute();
			// get binary data of attachmnet with id=1001 
			attachmnts.at(1001).getBinary().execute();
			// get entity data of attachmnet with id=1001 
			attachmnts.at(1001).get().execute();
			
			//update 
			attachmnts.update().entity(new EntityModel(fieldsModels)).execute();
			
			// metadata
			final Metadata metadata = nga.metadata();
			// all entities
			metadata.entities().execute();
			// only defects
			metadata.entities("defects").execute();

			// fields
			metadata.fields().execute();
			// only fields of defects and tests
			metadata.fields("defects", "tests").execute();*/
			
			
			// Last Check
			//defects.delete().execute();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

}
	
	

}

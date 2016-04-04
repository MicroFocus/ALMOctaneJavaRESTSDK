package java.com.hpe.adm.nga.sdk.metadata;

import java.com.hpe.adm.nga.sdk.Entity.Entities;
import java.com.hpe.adm.nga.sdk.Entity.EntityList;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Create;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Delete;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Get;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Update;

/**
 * Created by brucesp on 23/02/2016.
 */
public class HierarchyFeature extends Feature{
	
	
	public String[] childTypes(){};
	public String[] parentTypes(){};
	public Root rootEnt;
	
	public static class Root {
		public String type(){};
		public String id(){};
	}
}




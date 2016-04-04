package com.hpe.adm.nga.sdk.metadata.Features;

/**
 * Created by brucesp on 23/02/2016.
 */
public class HierarchyFeature extends Feature{
	
	
	public String[] childTypes(){return null;};
	public String[] parentTypes(){return null;};
	public Root rootEnt;
	
	public static class Root {
		public String type(){return null;};
		public String id(){return null;};
	}
}




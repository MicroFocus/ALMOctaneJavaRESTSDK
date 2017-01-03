package com.hpe.adm.nga.sdk.metadata.features;

/**
 * This class hold the HierarchyFeature object and serve all functionality concern to Hierarchy Feature
 *
 */
public class HierarchyFeature extends Feature{
	
	private String[] child_types;
	private String[] parent_types;
	private Root root;
	
	/**
	 * get Child Types
	 * @return
	 */
	public String[] getChildTypes(){return child_types;}

    /**
	 * get Parent Types
	 * @return
	 */
	public String[] getParentTypes(){return parent_types;}

	/**
	 * get Root Types
	 * @return
	 */
	public Root getRootEnt(){return root;}

	/**
	 * Root Data structure
	 * @author Moris Oz
	 *
	 */
	public static class Root {
		private String type;
		private String id;
		
		/**
		 * get type
		 * @return
		 */
		public String getType(){return type;}

		/**
		 * get id
		 * @return
		 */
		public String getId(){return id;}
	}
}




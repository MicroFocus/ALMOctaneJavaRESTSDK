package com.hpe.adm.nga.sdk.metadata.features;

/**
 *
 * This class hold the HierarchyFeature object and serve all functionality concern to Hierarchy Feature
 *
 */
public class HierarchyFeature extends Feature{
	
	private String[] child_types;
	private String[] parent_types;
	private Root root;
	
	/**
	 * get Child Types
	 * @return an array of child types
	 */
	public String[] getChildTypes(){return child_types;}

    /**
	 * get Parent Types
	 * @return an array of parent types
	 */
	public String[] getParentTypes(){return parent_types;}

	/**
	 * get Root Types
	 * @return the root type
	 */
	public Root getRootEnt(){return root;}

	/**
	 * Root Data structure
	 *
	 */
	public static class Root {
		private String type;
		private String id;
		
		/**
		 * get type
		 * @return the type of the root
		 */
		public String getType(){return type;}

		/**
		 * get id
		 * @return the id of the root
		 */
		public String getId(){return id;}
	}
}




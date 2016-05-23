package main.java.com.hpe.adm.nga.sdk.metadata.Features;

/**
 * This class hold the HierarchyFeature object and serve all functionality concern to Hierarchy Feature
 * @autho Moris oz
 *
 */
public class HierarchyFeature extends Feature{
	
	private String[] childTypes;
	private String[] parentTypes;
	private Root rootEnt;
	
	/**
	 * get Child Types
	 * @return
	 */
	public String[] getChildTypes(){return childTypes;};
	
	/**
	 * get Parent Types
	 * @return
	 */
	public String[] getParentTypes(){return parentTypes;};
	
	/**
	 * get Root Types
	 * @return
	 */
	public Root getRootEnt(){return rootEnt;};
	
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
		public String getType(){return type;};
		
		/**
		 * get id
		 * @return
		 */
		public String getId(){return id;};
	}
}




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




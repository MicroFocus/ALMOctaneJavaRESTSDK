package main.java.com.hpe.adm.nga.sdk.metadata.Features;


/**
 * This class hold the RestFeature object and serve all functionality concern to rest Feature
 * @autho Moris oz
 *
 */
public class RestFeature extends Feature{
	
	private String url; 
	private String[] methods;
	
	/**
	 * get url
	 * @return
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * get supported methods
	 * @return
	 */
	public String[] getMethods(){
		return methods;
	}
}



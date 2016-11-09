package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.network.HttpClient;
import com.hpe.adm.nga.sdk.network.HttpRequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * This class represents the main Octane builder
 *
 * @author Moris Oz
  */
public class Octane {

	//Constants
	private static final String SITE_ADMIN_DOMAIN_FORMAT= "/api/siteadmin/";
	private static final String SHARED_SPACES_DOMAIN_FORMAT= "%s/api/shared_spaces/%s/"; 
	private static final String WORKSPACES_DOMAIN_FORMAT= "workspaces/%s/"; 
	private static final String METADATA_DOMAIN_FORMAT= "metadata";
	private static final String ATTACHMENT_LIST_DOMAIN_FORMAT = "attachments";
	
	
	//private memebers
	private HttpRequestFactory requestFactory = null;
	private String urlDomain = "";
	public String idsharedSpaceId = null;
	private long workSpaceId = 0;
	
	// functions
	public Octane(HttpRequestFactory reqFactory, String domain, String sharedSpaceId, long workId ) {
		
		requestFactory = reqFactory;
		urlDomain = domain;
		idsharedSpaceId = sharedSpaceId;
		workSpaceId = workId;
	}

	/**
	   * EntityList Getter
	   * 
	   * @param entityName - Entity Name
	   * @return  A new EntityList object that list of entities 
	   */
	public EntityList entityList(String entityName) {
		
		String entityListDomain =  getBaseDomainFormat() + entityName; 
		return new EntityList(requestFactory, entityListDomain);
	}
	

	/**
	   * Metadata Getter
	   * 
	   * @return  A new Metadata object that hold the metadata
	   */
	public Metadata metadata() {
		String metadataDomain =  getBaseDomainFormat()+METADATA_DOMAIN_FORMAT;
		return new Metadata(requestFactory, metadataDomain) ;
	}

	/**
	   * AttachmentList Getter
	   * 
	   * @return  A new AttachmentList object that hold the Attachments
	   */
	public AttachmentList AttachmentList() {
		
		String attachmentListDomain =  getBaseDomainFormat()+ATTACHMENT_LIST_DOMAIN_FORMAT; 
		return new AttachmentList(requestFactory, attachmentListDomain);
	}
	
	/**
	 * get the base domain based on workSpaceId and idsharedSpaceId
	 * @return base domain
	 */
	protected String getBaseDomainFormat(){

		String baseDomain = urlDomain + SITE_ADMIN_DOMAIN_FORMAT;
		
		if (idsharedSpaceId!=null && !idsharedSpaceId.isEmpty())
		{
			baseDomain = String.format(SHARED_SPACES_DOMAIN_FORMAT,urlDomain, idsharedSpaceId);
			
			if (workSpaceId!=0)
				baseDomain = baseDomain + String.format(WORKSPACES_DOMAIN_FORMAT,String.valueOf(workSpaceId));
		}
			
		return baseDomain;
	}
	
	/**
	 * This class is in charge on the builder functionality 
	 *
	 * @author Moris Oz
	  */
	public static class Builder {


		//Constants
		private static final String OAUTH_AUTH_URL = "/authentication/sign_in";
		private static final String SET_COOKIE = "set-cookie";
		private static final String HPSSO_COOKIE_CSRF = "HPSSO_COOKIE_CSRF";
		private static final String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
		private static final String HPSSO_HEADER_CSRF = "HPSSO_HEADER_CSRF";
		private static final String HPE_CLIENT_TYPE = "HPECLIENTTYPE";
		private static final String HPE_MQM_UI = "HPE_MQM_UI";
		private static final String LOGGER_REQUEST_FORMAT = "Request: %s - %s - %s";
		private static final String LOGGER_RESPONSE_FORMAT = "Response: %d - %s - %s";


		//Private
		private Logger logger = LogManager.getLogger(Octane.class.getName());
		private String hppsValue = "";
		private HttpClient httpClient = new HttpClient();
		private String urlDomain = "";
		public String idsharedSpaceId = null;
		private long workSpaceId = 0;
		private final Authorisation authorisation;


		//Functions

		/**
		 * Creates a new Builder object
		 *
		 * @param authorisation - hold the details of Authorisation
		 */
		public Builder(Authorisation authorisation) {
			this.authorisation = authorisation;
		}

		/**
		 * Setter of the sharedSpace id ( UUID )
		 *
		 * @param ssID - sharedSpace id
		 * @return Builder object with new sharedSpace id
		 */
		public Builder sharedSpace(UUID ssID) {

			idsharedSpaceId = ssID.toString();
			return this;
		}

		/**
		 * Setter of the sharedSpace id ( long )
		 *
		 * @param ssID - sharedSpace id
		 * @return Builder object with new sharedSpace id
		 */
		public Builder sharedSpace(long ssID) {

			idsharedSpaceId = String.valueOf(ssID);
			return this;
		}

		/**
		 * Setter of the workSpace id
		 *
		 * @param lId - workSpace id
		 * @return Builder object with new workSpace id
		 */
		public Builder workSpace(long lId) {

			workSpaceId = lId;
			return this;
		}

		/**
		 * Setter of the Server domain name and port
		 *
		 * @param domain - domain name
		 * @param port   - port number
		 * @return Builder object with new Server domain info
		 */
		public Builder Server(String domain, int port) {

			urlDomain = domain + ":" + String.valueOf(port);

			return this;
		}

		/**
		 * Setter of the Server domain name
		 *
		 * @param domain - domain name
		 * @return Builder object with new Server domain name
		 */
		public Builder Server(String domain) {

			urlDomain = domain;

			return this;
		}

		/**
		 * The main build procedure which create the Octane objects,based on this steps :
		 * 1. Build an HTTP request
		 * 2. Handle response - Initialize Cookies keys
		 * 3. Create a new Octane objects.
		 *
		 * @return a new Octane object
		 * @throws RuntimeException
		 */
		public Octane build() throws RuntimeException {

			Octane objOctane = null;

			HttpRequestFactory requestFactory = httpClient.getRequestFactory(urlDomain, authorisation);
			if (httpClient.checkAuthentication()) {
				objOctane = new Octane(requestFactory, urlDomain, idsharedSpaceId, workSpaceId);
			}

			return objOctane;
		}
	}
}

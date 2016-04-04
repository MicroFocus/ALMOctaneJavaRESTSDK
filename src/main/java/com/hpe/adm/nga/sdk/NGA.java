package com.hpe.adm.nga.sdk;

import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.authorisation.BasicAuthorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
//import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;


/**
 * This class represents the main NGA builder
 *
 * @author Moris Oz
  */
public class NGA {

	//Constants
	private static String ENTITY_LIST_DOMAIN_FORMAT= "%s/api/shared_spaces/%s/workspaces/%s/%s";
		
	/**
	   * EntityList Getter
	   * 
	   * @param entityName - Entity Name
	   * @return  A new EntityList object that list of entities 
	   */
	public EntityList entityList(String entityName) {
		
		String strEntityListDomain =  String.format(ENTITY_LIST_DOMAIN_FORMAT, Builder.strUrlDomain, Builder.strIdsharedSpaceId.toString(),String.valueOf(Builder.lworkSpaceId),entityName); 
		return new EntityList(Builder.requestFactory,strEntityListDomain);
	}
	
	/**
	   * Metadata Getter
	   * 
	   * @return  A new Metadata object that hold the metadata
	   */
	public Metadata metadata() {
		return null;
	}

	/**
	   * AttachmentList Getter
	   * 
	   * @return  A new AttachmentList object that hold the Attachments
	   */
	public AttachmentList AttachmentList() {
		return null;
	}

	/**
	 * This class is in charge on the builder functionality 
	 *
	 * @author Moris Oz
	  */
	public static class Builder {

		
		//Constants
		private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		private static final String OAUTH_AUTH_URL = "/authentication/sign_in";
		private static final String SET_COOKIE = "set-cookie";
		private static final String HPSSO_COOKIE_CSRF = "HPSSO_COOKIE_CSRF";
		private static final String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
		private static final String HPSSO_HEADER_CSRF = "HPSSO_HEADER_CSRF";

		
		//Private
		private static String strLWSSOValue = "";
		private static String strHPPSValue = "";
		private static HttpRequestFactory requestFactory = null;
		private static String strUrlDomain = "";
		private static String strIdsharedSpaceId = null;
		private static long lworkSpaceId = 0;
		private static String strUserName = "";
		private static String strPass = "";
		
		
		//Functions
		
		/** Creates a new Builder object 
		 * 
		 * @param basicAuthorisation - hold the details of Authorisation
		 * @throws IOException
		 */
		public Builder(BasicAuthorisation basicAuthorisation) throws IOException {

	    	strUserName = basicAuthorisation.getUsername();
	    	strPass = basicAuthorisation.getPassword();
		}
		
		/**
		 *  Setter of the sharedSpace id ( UUID )
		 * @param ssID - sharedSpace id
		 * @return Builder object with new sharedSpace id
		 */
		public Builder sharedSpace(UUID ssID) {
			
			strIdsharedSpaceId = ssID.toString();
			return this;
		}
		
		/**
		 * Setter of the sharedSpace id ( long )
		 * @param ssID - sharedSpace id
		 * @return Builder object with new sharedSpace id
		 */
		public Builder sharedSpace(long ssID) {
			
			strIdsharedSpaceId = String.valueOf(ssID);
			return this;
		}
		/**
		 * Setter of the workSpace id
		 * @param lId - workSpace id
		 * @return Builder object with new workSpace id
		 */
		public Builder workSpace(long lId) {
			
			lworkSpaceId = lId;
			return this;
		}
		
		/**
		 * Setter of the Server domain name and port
		 * @param domain - domain name
		 * @param port - port number 
		 * @return Builder object with new Server domain info
		 */
		public Builder Server(String domain, int port) {
			
			strUrlDomain = domain + ":" + String.valueOf(port);
			
			return this;
		}
		/**
		 *  Setter of the Server domain name 
		 * @param domain - domain name
		 * @return Builder object with new Server domain name
		 */
		public Builder Server(String domain) {
			
			strUrlDomain = domain;
			
			return this;
		}
		/**
		 * The main build procedure which create the NGA objects,based on this steps :
		 * 1. Build an HTTP request
		 * 2. Handle response - Initialize Cookies keys
		 * 3. Create a new NGA objects.
		 *  
		 * @return a new NGA object
		 * @throws IOException
		 * @throws JSONException
		 */
		public NGA build() throws IOException, JSONException{
			
			NGA objNga = null;
			
			requestFactory = HTTP_TRANSPORT
	                .createRequestFactory(new HttpRequestInitializer() {
	                    @Override
	                    public void initialize(HttpRequest request) {
	                    	
	                    	// BasicAuthentication needed only in first initialization
	                    	if (strHPPSValue=="" && strLWSSOValue=="")
	                    	{
	                    		request.getHeaders().setBasicAuthentication(strUserName,strPass);
	                    	}
	                    	else
	                    	{
	                    		request.getHeaders().set(HPSSO_HEADER_CSRF,strHPPSValue); 
		                        request.getHeaders().setCookie(LWSSO_COOKIE_KEY+"="+strLWSSOValue); 
	                    	}
	                    		
	                        
	                        
	                    }
	                });
			
			GenericUrl urlDomain = new GenericUrl(strUrlDomain + OAUTH_AUTH_URL);
			HttpRequest httpRequest = requestFactory.buildPostRequest(urlDomain, null);
			HttpResponse response = httpRequest.execute();
			
			// Initialize Cookies keys
			if (response.isSuccessStatusCode()) {

				HttpHeaders hdr1 = response.getHeaders();	
				List<String> strHPSSOCookieCsrf1 = hdr1.getHeaderStringValues(SET_COOKIE);
				String strCookies = strHPSSOCookieCsrf1.toString();
				List<HttpCookie> Cookies = java.net.HttpCookie.parse(strCookies.substring(1, strCookies.length()-1));
				 for (HttpCookie ck : Cookies) {
					 
					 if (ck.getName().equals(HPSSO_COOKIE_CSRF))
						 strHPPSValue = ck.getValue();
					 
					 if (ck.getName().equals(LWSSO_COOKIE_KEY))
						 strLWSSOValue = ck.getValue();
						 
				 }
				
	           objNga = new NGA();
	        } 
   	       	        
	        return objNga;
		}
	}

}

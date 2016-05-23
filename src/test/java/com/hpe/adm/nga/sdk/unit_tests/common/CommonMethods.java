package test.java.com.hpe.adm.nga.sdk.unit_tests.common;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;

public class CommonMethods {
	public static HttpRequestFactory getRequestfactory(){
		final String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
		final String HPSSO_HEADER_CSRF = "HPSSO_HEADER_CSRF";
		final String HPE_CLIENT_TYPE = "HPECLIENTTYPE";
		final String HPE_MQM_UI ="HPE_MQM_UI";
		return new NetHttpTransport().createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) {
            		String lastResponseCoockie = request.getHeaders().getCookie()!=null ? request.getHeaders().getCookie() : LWSSO_COOKIE_KEY+"="+"";
            		request.getHeaders().setCookie(lastResponseCoockie); 
            		request.getHeaders().set(HPSSO_HEADER_CSRF,""); 
            		request.getHeaders().set(HPE_CLIENT_TYPE,HPE_MQM_UI); 
            }
        });
	}
}

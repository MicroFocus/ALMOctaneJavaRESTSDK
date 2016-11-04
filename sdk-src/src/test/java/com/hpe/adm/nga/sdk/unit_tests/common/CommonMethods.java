package com.hpe.adm.nga.sdk.unit_tests.common;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.network.HttpRequestFactory;

import java.util.Collection;
import java.util.Set;

public class CommonMethods {
	private final static String urlDomain = "https://mqast001pngx.saas.hpe.com";
	private final static String sharedSpace = "21025";
	private final static int workSpace = 1002;
	
	public static HttpRequestFactory getRequestfactory(){
		final String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
		final String HPSSO_HEADER_CSRF = "HPSSO_HEADER_CSRF";
		final String HPE_CLIENT_TYPE = "HPECLIENTTYPE";
		final String HPE_MQM_UI ="HPE_MQM_UI";
		return new HttpRequestFactory(new NetHttpTransport().createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) {
            		String lastResponseCoockie = request.getHeaders().getCookie()!=null ? request.getHeaders().getCookie() : LWSSO_COOKIE_KEY+"="+"";
            		request.getHeaders().setCookie(lastResponseCoockie); 
            		request.getHeaders().set(HPSSO_HEADER_CSRF,""); 
            		request.getHeaders().set(HPE_CLIENT_TYPE,HPE_MQM_UI); 
            }
        }));
	}
	
	public static String getDomain(){
		return urlDomain;
	}
	
	public static String getSharedSpace(){
		return sharedSpace;
	}
	
	public static int getWorkSpace(){
		return workSpace;
	}
	
	
	public static boolean isErrorAInErrorB(ErrorModel entityA, ErrorModel entityB) {
        if (entityA == null) {
            return true;
        }
        Set<FieldModel> fieldsA = entityA.getValues();
        Set<FieldModel> fieldsB = entityB.getValues();
        boolean isMatch;
        for (FieldModel fieldA : fieldsA) {
            if (fieldA.getClass().equals(MultiReferenceFieldModel.class) || fieldA.getClass().equals(ReferenceFieldModel.class)) {
                continue;
            }
            isMatch = false;
            for (FieldModel fieldB : fieldsB) {
                if (fieldA.getName().equals(fieldB.getName())
                        && (fieldA.getValue() == null
                        || fieldA.getValue().equals(fieldB.getValue()))) {
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                return false;
            }
        }
        return true;
    }

    public static boolean isErrorCollectionAInErrorCollectionB(Collection<ErrorModel> collectionA, Collection<ErrorModel> collectionB) {
        boolean isMatch;
        for (ErrorModel entityA : collectionA) {
            isMatch = false;
            for (ErrorModel entityB : collectionB) {
                if (isErrorAInErrorB(entityA, entityB)) {
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                return false;
            }
        }
        return true;
    }
	
}

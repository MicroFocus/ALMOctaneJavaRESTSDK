package com.hpe.adm.nga.sdk.authentication;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationUtil {

    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String CLIENT_PASSWORD_KEY = "password";
    private static final String PLACEHOLDER = "******";

    /**
     * Utility method that will attempt to filter out any sensitive information out of a log message. <br>
     * Used by the SDK's default implemtentation of the {@link com.hpe.adm.nga.sdk.network.OctaneHttpClient},
     * the {@link com.hpe.adm.nga.sdk.network.google.GoogleHttpClient} to print debug log messages of the POST body. <br>
     * This currently only works string witch are valid JSON, since all authentication methods use JSON inside the POST body
     * @param logMessage string intended to be used for logging
     * @return the same string if the param is not valid JSON or JSON string with placeholder values for password and client_secret
     */
    public static String removeAuthDataForLogger(String logMessage){
        //Currently only works for JSON strings, all auth rest endpoints use JSON though
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(logMessage);
        } catch (JSONException ex){
            return logMessage;
        }

        replaceKey(jsonObject, CLIENT_SECRET_KEY);
        replaceKey(jsonObject, CLIENT_PASSWORD_KEY);

        return jsonObject.toString();
    }

    private static void replaceKey(JSONObject jsonObject, String key){
        //if it is a JSON object, try to remove the sensitive information
        if(jsonObject.has(key)){
            jsonObject.put(key, PLACEHOLDER);
        }
    }

}
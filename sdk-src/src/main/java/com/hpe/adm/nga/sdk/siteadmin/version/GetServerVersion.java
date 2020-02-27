package com.hpe.adm.nga.sdk.siteadmin.version;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetServerVersion {
    private final Logger logger = LoggerFactory.getLogger(GetServerVersion.class.getName());

    private final OctaneRequest octaneRequest;
    private final OctaneHttpClient octaneHttpClient;

    public GetServerVersion(final OctaneHttpClient octaneHttpClient, final String urlDomain) {
        this.octaneHttpClient = octaneHttpClient;
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain + "version");
    }

    public Version execute() {
        final OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl());
        final OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        if (response.isSuccessStatusCode()) {
            final String json = response.getContent();
            logger.debug(String.format("Response_Json: %s", json));

            final JSONTokener tokener = new JSONTokener(json);
            final JSONObject jsonObj = new JSONObject(tokener);
            return new Version(jsonObj.getString("version"),
                    jsonObj.getString("build_date"),
                    jsonObj.getString("build_revision"),
                    jsonObj.getString("build_number"),
                    jsonObj.getString("display_version"));
        } else {
            throw new RuntimeException("Unexpected Version reponse!");
        }
    }
}

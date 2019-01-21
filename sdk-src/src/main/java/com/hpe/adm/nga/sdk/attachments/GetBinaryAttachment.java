package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.io.InputStream;

/**
 * This class hold the GetBinary objects (handle the binary data of a unique Attachment model )
 */
public class GetBinaryAttachment {

    private final OctaneRequest octaneRequest;
    private final OctaneHttpClient octaneHttpClient;

    protected GetBinaryAttachment(OctaneHttpClient octaneHttpClient, String urlDomain, String entityId) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain, entityId);
        this.octaneHttpClient = octaneHttpClient;
    }

    /**
     * GetEntities Request execution of binary data
     * @return a stream with binary data
     */
    public InputStream execute()  {
        InputStream inputStream = null;

        OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(octaneRequest.getFinalRequestUrl())
                .setAcceptType(OctaneHttpRequest.OCTET_STREAM_CONTENT_TYPE);
        OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

        if (response.isSuccessStatusCode()) {
            inputStream = response.getInputStream();
        }

        return inputStream;
    }
}

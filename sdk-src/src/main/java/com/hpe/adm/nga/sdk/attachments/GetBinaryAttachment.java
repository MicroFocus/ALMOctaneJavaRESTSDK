package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

import java.io.InputStream;

/**
 * This class hold the GetBinary objects (handle the binary data of a unique Attachment model )
 */
public class GetBinaryAttachment extends OctaneRequest {

    GetBinaryAttachment(OctaneHttpClient octaneHttpClient, String urlDomain, int entityId) {
        super(octaneHttpClient, urlDomain, entityId);
    }

    /**
     * GetEntities Request execution of binary data
     * return a stream with binary data
     */
    public InputStream execute() throws RuntimeException {

        return executeBinary();
    }

    /**
     * GetEntities binary data
     *
     * @return - Stream with binary data
     */
    private InputStream executeBinary() {
        InputStream inputStream = null;
        try {
            OctaneHttpRequest octaneHttpRequest = new OctaneHttpRequest.GetOctaneHttpRequest(getFinalRequestUrl())
                    .setAcceptType(OctaneHttpRequest.OCTET_STREAM_CONTENT_TYPE);
            OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

            if (response.isSuccessStatusCode()) {

                inputStream = response.getInputStream();
            }
        } catch (Exception e) {

            handleException(e, false);

        }

        return inputStream;
    }
}

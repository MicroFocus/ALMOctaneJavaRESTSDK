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

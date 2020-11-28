/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.APIMode;

import java.io.InputStream;
import java.util.*;

/**
 * HTTP request.
 * <p>
 * Created by leufl on 2/11/2016.
 */
public abstract class OctaneHttpRequest {

    public enum OctaneRequestMethod {
        GET,
        POST,
        PUT,
        DELETE,
        POST_BINARY
    }

    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";

    private final String requestUrl;
    private final OctaneRequestMethod octaneRequestMethod;
    private Set<APIMode> httpHeaders = new HashSet<>();

    private OctaneHttpRequest(String requestUrl, OctaneRequestMethod octaneRequestMethod) {
        this.requestUrl = requestUrl;
        this.octaneRequestMethod = octaneRequestMethod;
    }

    public final void setHeaders(Set<APIMode> headers) {
        httpHeaders = headers;
    }

    public Set<APIMode> getHeaders() {
        return httpHeaders;
    }

    public final String getRequestUrl() {
        return requestUrl;
    }

    public final OctaneRequestMethod getOctaneRequestMethod() {
        return octaneRequestMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof OctaneHttpRequest)) return false;
        final OctaneHttpRequest that = (OctaneHttpRequest) obj;
        return octaneRequestMethod == that.octaneRequestMethod && requestUrl.equals(that.requestUrl);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{
                octaneRequestMethod,
                requestUrl
        });
    }

    public static class DeleteOctaneHttpRequest extends OctaneHttpRequest {
        public DeleteOctaneHttpRequest(final String url) {
            super(url, OctaneRequestMethod.DELETE);
        }
    }

    private abstract static class HasAcceptOctaneHttpRequest<E extends HasAcceptOctaneHttpRequest> extends OctaneHttpRequest {
        private String acceptType;

        private HasAcceptOctaneHttpRequest(final String url, OctaneRequestMethod octaneRequestMethod) {
            super(url, octaneRequestMethod);
        }

        public E setAcceptType(String acceptType) {
            this.acceptType = acceptType;
            //noinspection unchecked
            return (E) this;
        }

        public final String getAcceptType() {
            return acceptType;
        }
    }

    public static class GetOctaneHttpRequest extends HasAcceptOctaneHttpRequest<GetOctaneHttpRequest> {
        public GetOctaneHttpRequest(final String url) {
            super(url, OctaneRequestMethod.GET);
        }
    }

    private static abstract class HasContentOctaneHttpRequest<F extends HasContentOctaneHttpRequest> extends HasAcceptOctaneHttpRequest<F> {
        private final String contentType;
        private final String content;

        private HasContentOctaneHttpRequest(final String url, OctaneRequestMethod octaneRequestMethod, String contentType, String content) {
            super(url, octaneRequestMethod);
            this.contentType = contentType;
            this.content = content;
        }

        public final String getContentType() {
            return contentType;
        }

        public final String getContent() {
            return content;
        }
    }

    public static class PutOctaneHttpRequest extends HasContentOctaneHttpRequest<PutOctaneHttpRequest> {
        public PutOctaneHttpRequest(final String url, String contentType, String content) {
            super(url, OctaneRequestMethod.PUT, contentType, content);
        }
    }

    public static class PostOctaneHttpRequest extends HasContentOctaneHttpRequest<PostOctaneHttpRequest> {
        public PostOctaneHttpRequest(final String url, String contentType, String content) {
            super(url, OctaneRequestMethod.POST, contentType, content);
        }
    }

    public static class PostBinaryOctaneHttpRequest extends HasContentOctaneHttpRequest<PostBinaryOctaneHttpRequest> {

        private final InputStream binaryInputStream;
        private final String binaryContentName;
        private final String binaryContentType;

        public PostBinaryOctaneHttpRequest(final String url, final InputStream binaryInputStream,
                                           String content, String binaryContentName, String binaryContentType) {
            super(url, OctaneRequestMethod.POST_BINARY, OCTET_STREAM_CONTENT_TYPE, content);
            this.binaryInputStream = binaryInputStream;
            this.binaryContentName = binaryContentName;
            this.binaryContentType = binaryContentType;
        }

        public InputStream getBinaryInputStream() {
            return binaryInputStream;
        }

        public String getBinaryContentName() {
            return binaryContentName;
        }

        public String getBinaryContentType() {
            return binaryContentType;
        }
    }
}

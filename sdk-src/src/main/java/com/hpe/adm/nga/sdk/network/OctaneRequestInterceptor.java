package com.hpe.adm.nga.sdk.network;

public interface OctaneRequestInterceptor {
    OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest);
}

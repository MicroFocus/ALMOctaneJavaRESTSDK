/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package com.hpe.adm.nga.sdk.utils;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;

/**
 *
 * Created by Dmitry Zavyalov on 03/05/2016.
 */
public class ContextUtils {

    public static Octane getContextSiteAdmin(String url, Authentication authentication) {
        return getContext(url, authentication, "", "");
    }

    public static Octane getContextSharedSpace(String url, Authentication authentication, String sharedSpaceId) {
        return getContext(url, authentication, sharedSpaceId, "");
    }

    public static Octane getContextWorkspace(String url, Authentication authentication, String sharedSpaceId, String workspaceId) {
        return getContext(url, authentication, sharedSpaceId, workspaceId);
    }

    private static Octane getContext(String url, Authentication authentication, String sharedSpaceId, String workspaceId) {
        Octane octane = null;
        try {
            Octane.Builder builder = new Octane.Builder(authentication).Server(url);

            if (!sharedSpaceId.isEmpty()) {
                builder = builder.sharedSpace(Long.valueOf(sharedSpaceId));
            }

            if (!workspaceId.isEmpty()) {
                builder = builder.workSpace(Long.valueOf(workspaceId));
            }

            octane = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return octane;
    }
}
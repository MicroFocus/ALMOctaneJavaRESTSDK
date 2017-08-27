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

package com.hpe.adm.nga.sdk.extension;

/**
 * Used to story some basic values that you can change to run the examples
 */
public interface OctaneConnectionConstants {

    //Server
    String urlDomain = "http://myd-vm10632.hpeswlab.net:8081";
    Long sharedspaceId = 1001L;
    Long workspaceId = 1002L;

    //Auth
    String username = "sa@nga";
    String password = "Welcome1";

}
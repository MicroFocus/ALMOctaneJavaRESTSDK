package com.hpe.adm.nga.sdk.authentication;

/**
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Interface of Authentication , hold contract functions.
 *
 */
public interface Authentication {

    /**
     * Holds the string that will be sent in the body of the authentication post
     * @return The authentication string.  Either user/pass or client/secret
     */
    String getAuthenticationString();

    /**
     * Returns the HPECLIENTTYPE header that is added to all calls to the REST API.  See the REST API documentation for
     * further information
     * This will only be used if it is non-empty
     * @return The String that will be sent as the HPECLIENTTYPE.  If none is sent then this should be null
     */
    String getClientHeader();
}



/**
 * Copyright 2016-2023 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk;

/**
 * An interface that represents the API mode that should be used.  This normally determines whether the technical preview
 * mode should be used as detailed in the REST API documentation. For this purpose two classes have been implemented to
 * enable the technical preview modes
 * This interface can be implemented to enable other non-documented modes as necessary
 * Note:  Only one API mode can be set at one time (as supported by the REST API itself)
 */
public interface APIMode {
    /**
     * The header value that is sent
     *
     * @return The header value
     */
    String getHeaderValue();

    /**
     * The header key that is sent
     *
     * @return The header key
     */
    String getHeaderKey();

    /**
     * Enables the technical preview mode
     */
    APIMode TechnicalPreviewAPIMode = new APIMode() {
        @Override
        public String getHeaderValue() {
            return "true";
        }

        @Override
        public String getHeaderKey() {
            return "ALM-OCTANE-TECH-PREVIEW";
        }
    };

    /**
     * Enables technical preview mode for versions of CP7 and below
     *
     * @deprecated CP7 is nearing the end of its support and should not be used
     */
    @Deprecated
    APIMode CP7TechnicalPreviewAPIMode = new APIMode() {

        @Override
        public String getHeaderValue() {
            return "ALM_OCTANE_TECH_PREVIEW";
        }

        @Override
        public String getHeaderKey() {
            return "HPECLIENTTYPE";
        }
    };
}

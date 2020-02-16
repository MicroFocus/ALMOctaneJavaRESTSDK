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
            return "ALM_OCTANE_TECH_PREVIEW";
        }

        @Override
        public String getHeaderKey() {
            return "true";
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
            return "HPECLIENTTYPE";
        }

        @Override
        public String getHeaderKey() {
            return "ALM_OCTANE_TECH_PREVIEW";
        }
    };
}

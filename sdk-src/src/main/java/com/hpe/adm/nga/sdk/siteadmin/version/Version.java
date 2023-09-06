/*
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
package com.hpe.adm.nga.sdk.siteadmin.version;

import java.text.SimpleDateFormat;

/**
 * An object corresponding to the information that can be obtained from the Octane API
 */
public final class Version {
    /**
     * Represents the format that the version date is returned as
     */
    public static final SimpleDateFormat VERSION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final String version;
    private final String buildDate;
    private final String buildRevision;
    private final String buildNumber;
    private final String displayVersion;

    /**
     * Creates a new instance
     *
     * @param version        EG 15.0.20.74
     * @param buildDate      EG 2020-02-11 17:54
     * @param buildRevision  EG 97371aa8e504cbc83a7c02f7099be578108a59a7
     * @param buildNumber    EG 74
     * @param displayVersion EG 15.0.20.74
     */
    Version(String version, String buildDate, String buildRevision, String buildNumber, String displayVersion) {
        this.version = version;
        this.buildDate = buildDate;
        this.buildRevision = buildRevision;
        this.buildNumber = buildNumber;
        this.displayVersion = displayVersion;
    }

    /**
     * Gets the version as a MAJOR.MINOR.PATH.BUILD string
     *
     * @return the version (eg 15.0.40.76)
     */
    public String getVersion() {
        return version;
    }

    /**
     * The build date returned as a string.  This can be converted to a {@link java.util.Date} by using the
     * {@link #VERSION_DATE_FORMAT} member of this class
     *
     * @return The date in the pattern "yyyy-MM-dd HH:mm"
     */
    public String getBuildDate() {
        return buildDate;
    }

    /**
     * The internal revision string
     *
     * @return EG 97371aa8e504cbc83a7c02f7099be578108a59a7
     */
    public String getBuildRevision() {
        return buildRevision;
    }

    /**
     * The internal build number
     *
     * @return eg 76
     */
    public String getBuildNumber() {
        return buildNumber;
    }

    /**
     * The final displayable version
     *
     * @return eg 15.0.40.76
     */
    public String getDisplayVersion() {
        return displayVersion;
    }
}

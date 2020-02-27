package com.hpe.adm.nga.sdk.siteadmin.version;

public final class Version {
    private final String version;
    private final String buildDate;
    private final String buildRevision;
    private final String buildNumber;
    private final String displayVersion;

    Version(String version, String buildDate, String buildRevision, String buildNumber, String displayVersion) {
        this.version = version;
        this.buildDate = buildDate;
        this.buildRevision = buildRevision;
        this.buildNumber = buildNumber;
        this.displayVersion = displayVersion;
    }

    public String getVersion() {
        return version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public String getBuildRevision() {
        return buildRevision;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }
}

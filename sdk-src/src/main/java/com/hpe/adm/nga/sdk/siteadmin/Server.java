package com.hpe.adm.nga.sdk.siteadmin;

import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.siteadmin.version.GetServerVersion;

public class Server {

    // private members
    private final OctaneHttpClient octaneHttpClient;
    private final String urlDomain;

    /**
     * Creates a new Metadata object
     *
     * @param octaneHttpClient  - Http Request Factory
     * @param siteAdminDomain - siteAdmin Domain Name
     */
    public Server(OctaneHttpClient octaneHttpClient, String siteAdminDomain) {
        urlDomain = siteAdminDomain + "server/";
        this.octaneHttpClient = octaneHttpClient;
    }

    public GetServerVersion getServerVersion() {
        return new GetServerVersion(octaneHttpClient, urlDomain);
    }

}

package com.hpe.adm.nga.sdk.extension.entities;

import com.hpe.adm.nga.sdk.entities.get.GetEntities;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extension for {@link GetEntities}
 */
public class ExtendedGetEntities extends GetEntities {

    protected ExtendedGetEntities(OctaneHttpClient octaneHttpClient, String urlDomain) {
        super(octaneHttpClient, urlDomain);
    }

    /**
     * Allows setting the expand query param of the request, controls what fields are returned for each relation field of base entity
     * @param relationExpandFields map of relation field name to fields of the related entity type
     * @return GetEntities Object with new expand parameter
     */
    public GetEntities expand(Map<String, Set<String>> relationExpandFields) {
        String expandQueryString =
                relationExpandFields.keySet().stream().map(relationFieldName -> {

                    String relationFieldExpandValue = "";
                    if(relationExpandFields.get(relationFieldName).size() > 0){
                        relationFieldExpandValue =
                                relationExpandFields.get(relationFieldName)
                                        .stream()
                                        .collect(Collectors.joining(","));

                        relationFieldExpandValue = "fields=" + relationFieldExpandValue;
                    }

                    return relationFieldName + "{" + relationFieldExpandValue +"}";
                }).collect(Collectors.joining(","));

        octaneRequest.getOctaneUrl().setParam("expand", expandQueryString);

        return this;
    }

}
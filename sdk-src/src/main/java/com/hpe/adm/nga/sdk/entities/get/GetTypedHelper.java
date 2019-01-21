package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * A helper class for shared functionality for get typed entities
 */
final class GetTypedHelper {

    /**
     * Set Fields Parameters
     *
     * @param octaneRequest The request to be sent to the server
     * @param fields An array or comma separated list of fields to be retrieved
     */
    static <F extends TypedEntityList.AvailableFields> void addFields(final OctaneRequest octaneRequest, final F... fields) {
        final String fieldsAsString[] = new String[fields.length];
        for (int i = 0; i < fields.length; ++i) {
            fieldsAsString[i] = fields[i].getFieldName();
        }
        octaneRequest.getOctaneUrl().addFieldsParam(fieldsAsString);
    }

}

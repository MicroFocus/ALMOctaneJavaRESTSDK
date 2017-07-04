package com.hpe.adm.nga.sdk.entities.get;

import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.network.OctaneRequest;

/**
 * Created by brucesp on 04-Jul-17.
 */
final class GetTypedHelper {

    /**
     * Set Fields Parameters
     *
     * @param fields An array or comma separated list of fields to be retrieved
     * @return a new GetEntities object with new Fields Parameters
     */
    static <F extends TypedEntityList.AvailableFields> void addFields(final OctaneRequest octaneRequest, final F... fields) {
        final String fieldsAsString[] = new String[fields.length];
        for (int i = 0; i < fields.length; ++i) {
            fieldsAsString[i] = fields[i].getFieldName();
        }
        octaneRequest.getOctaneUrl().addFieldsParam(fieldsAsString);
    }

}

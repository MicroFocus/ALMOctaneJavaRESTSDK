package com.hpe.adm.nga.sdk.metadata;

import com.google.gson.Gson;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * This class hold the field metadata object
 */
public final class Field extends MetadataOctaneRequest {

    private final Logger logger = LogManager.getLogger(Field.class.getName());
    private static final String TYPE_NAME_FIELDS_NAME = "fields";
    private static final String QUERY_NAME_FIELD_NAME = "entity_name";

    /**
     * Creates a new Field object
     */
    public Field(OctaneHttpClient octaneHttpClient, String urlDomain) {

        super(octaneHttpClient, urlDomain + "/" + TYPE_NAME_FIELDS_NAME);
    }

    Field addEntities(String...entities) {
        return super.addEntities(QUERY_NAME_FIELD_NAME, entities);
    }

    /**
     * GetEntities Request execution of metadata's field info
     * Collection object
     */
    public Collection<FieldMetadata> execute() throws RuntimeException {

        Collection<FieldMetadata> colEntitiesMetadata = null;
        String json = "";
        try {

            OctaneHttpRequest octaneHttpRequest =
                    new OctaneHttpRequest.GetOctaneHttpRequest(getFinalRequestUrl()).setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);
            OctaneHttpResponse response = octaneHttpClient.execute(octaneHttpRequest);

            if (response.isSuccessStatusCode()) {

                json = response.getContent();
                colEntitiesMetadata = getFieldMetadata(json);
            }

            logger.debug(String.format(LOGGER_RESPONSE_JSON_FORMAT, json));
        } catch (Exception e) {

            handleException(e, false);
        }

        return colEntitiesMetadata;

    }

    /**
     * get a fields metadata collection based on a given json string
     *
     * @param json the json to parse
     * @return fields metadata collection based on a given json string
     */
    private Collection<FieldMetadata> getFieldMetadata(String json) {

        JSONTokener tokener = new JSONTokener(json);
        JSONObject jsonObj = new JSONObject(tokener);
        JSONArray jsonDataArr = jsonObj.getJSONArray(JSON_DATA_FIELD_NAME);

        // prepare entity collection
        Collection<FieldMetadata> fieldsMetadata = new ArrayList<>();
        IntStream.range(0, jsonDataArr.length()).forEach((i) -> fieldsMetadata.add(new Gson().fromJson(jsonDataArr.getJSONObject(i).toString(), FieldMetadata.class)));

        return fieldsMetadata;
    }
}

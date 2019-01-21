package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * This class hold the CreateEntities objects and serve all functions concern to REST
 * GetEntities.
 */
public class CreateAttachment {

    private String contentType = "";
    private String contentName = "";
    private InputStream inputStream = null;
    private EntityModel entity = null;
    private final OctaneRequest octaneRequest;

    protected CreateAttachment(OctaneHttpClient octaneHttpClient, String urlDomain) {
        octaneRequest = new OctaneRequest(octaneHttpClient, urlDomain);
    }

    /**
     * Setter of new entities to create and file to upload
     *
     * @param entity - new entity to create
     * @param stream - file stream
     * @param type   The content type
     * @param name   The content name
     * @return - An Object with new data
     */
    public CreateAttachment attachment(EntityModel entity, InputStream stream, String type, String name) {

        this.entity = entity;
        inputStream = stream;
        contentType = type;
        contentName = name;

        return this;
    }

    /**
     * Request Post Execution with Multipart content type
     * @return a collection of entities models that have been created
     */
    public OctaneCollection execute()  {
        JSONObject data = ModelParser.getInstance().getEntityJSONObject(entity);

        OctaneHttpRequest octaneHttpRequest =
                new OctaneHttpRequest.PostBinaryOctaneHttpRequest(octaneRequest.getFinalRequestUrl(), inputStream, data.toString(), contentName, contentType)
                        .setAcceptType(OctaneHttpRequest.JSON_CONTENT_TYPE);

        return  octaneRequest.getEntitiesResponse(octaneHttpRequest);
    }
}

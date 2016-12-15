package com.hpe.adm.nga.sdk.network;

import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Thread-safe light-weight HTTP request factory layer on top of the HTTP transport that has an optional HTTP request initializer for initializing requests.
 *
 * Created by leufl on 2/11/2016.
 */
public class HttpRequestFactory {

    private static final String HTTP_MEDIA_TYPE_MULTIPART_NAME = "multipart/form-data";
    private static final String HTTP_MULTIPART_BOUNDARY_NAME = "boundary";
    private static final String HTTP_MULTIPART_BOUNDARY_VALUE = "---------------------------92348603315617859231724135434";
    private static final String HTTP_MULTIPART_PART_DISPOSITION_NAME = "Content-Disposition";
    private static final String HTTP_MULTIPART_PART1_DISPOSITION_FORMAT = "form-data; name=\"%s\"; filename=\"blob\"";
    private static final String HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE = "entity";
    private static final String HTTP_MULTIPART_PART2_DISPOSITION_FORMAT = "form-data; name=\"content\"; filename=\"%s\"";

    private com.google.api.client.http.HttpRequestFactory requestFactory;

    /**
     * Creates a HTTP request factory.
     * @param requestFactory - HTTP request factory
     */
    public HttpRequestFactory(com.google.api.client.http.HttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    /**
     * Initializes a request.
     * @param request - HTTP request
     * @throws IOException
     */
    public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {
        requestFactory.getInitializer().initialize(request);
    }

    /**
     * Builds a request for the given HTTP method, URL, and content.
     * @param requestMethod - HTTP request method
     * @param url - HTTP request URL or null for none
     * @param content - HTTP request content or null for none
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildRequest(String requestMethod, String url, HttpContent content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildRequest(requestMethod, domain, content));
    }

    /**
     * Builds a DELETE request for the given URL.
     * @param url - HTTP request URL as String
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildDeleteRequest(String url) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildDeleteRequest(domain));
    }

    /**
     * Builds a GET request for the given URL.
     * @param url - HTTP request URL as String
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildGetRequest(String url) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildGetRequest(domain));
    }

    /**
     * Builds a POST request for the given URL and content.
     * @param url - HTTP request URL as String
     * @param content - HTTP request content as String
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildPostRequest(String url, String content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPostRequest(domain, ByteArrayContent.fromString(null, content)));
    }

    /**
     * Builds a POST request for the given URL and content.
     * @param url - HTTP request URL as String
     * @param content - HTTP request content or null for none
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildPostRequest(String url, HttpContent content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPostRequest(domain, content));
    }

    /**
     * Builds a PUT request for the given URL and content.
     * @param url - HTTP request URL as String
     * @param content - HTTP request content as String
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildPutRequest(String url, String content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPutRequest(domain, ByteArrayContent.fromString(null, content)));
    }

    /**
     * Builds a PATCH request for the given URL and content.
     * @param url - HTTP request URL as String
     * @param content - HTTP request content as String
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildPatchRequest(String url, String content) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildPatchRequest(domain, ByteArrayContent.fromString(null, content)));
    }

    /**
     * Builds a HEAD request for the given URL.
     * @param url - HTTP request URL as String
     * @return - new HTTP request
     * @throws IOException
     */
    public HttpRequest buildHeadRequest(String url) throws IOException {
        GenericUrl domain = new GenericUrl(url);
        return new HttpRequest(requestFactory.buildHeadRequest(domain));
    }

    /**
     * Generates HTTP content based on input parameters and stream.
     * @param strJasonEntityModel - JSON entity model.
     * @param inputStream - Content input stream
     * @param contentType - HTTP content type
     * @param contentName - HTTP content name
     * @return - Generated HTTP content.
     */
    public MultipartContent generateMultiPartContent(Object strJasonEntityModel, InputStream inputStream, String contentType, String contentName) {
        // Add parameters
        MultipartContent content = new MultipartContent()
                .setMediaType(new HttpMediaType(HTTP_MEDIA_TYPE_MULTIPART_NAME)
                        .setParameter(HTTP_MULTIPART_BOUNDARY_NAME, HTTP_MULTIPART_BOUNDARY_VALUE));

        MultipartContent.Part part1 = new MultipartContent.Part(new JsonHttpContent(new JacksonFactory(), strJasonEntityModel));
        part1.setHeaders(new HttpHeaders().set(HTTP_MULTIPART_PART_DISPOSITION_NAME,
                String.format(HTTP_MULTIPART_PART1_DISPOSITION_FORMAT,
                        HTTP_MULTIPART_PART1_DISPOSITION_ENTITY_VALUE)));
        content.addPart(part1);

        // Add Stream
        InputStreamContent inputStreamContent = new InputStreamContent(contentType, inputStream);
        MultipartContent.Part part2 = new MultipartContent.Part(inputStreamContent);
        part2.setHeaders(new HttpHeaders().set(HTTP_MULTIPART_PART_DISPOSITION_NAME, String.format(HTTP_MULTIPART_PART2_DISPOSITION_FORMAT, contentName)));
        content.addPart(part2);
        return content;
    }
}

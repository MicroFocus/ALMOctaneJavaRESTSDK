package com.hpe.adm.nga.sdk.network;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpStatusCodes;

import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP response.
 * Callers should call disconnect() when the HTTP response object is no longer needed. However, disconnect() does not have to be called if the response stream is properly closed.
 *
 * Created by leufl on 2/11/2016.
 */
public class HttpResponse {
    private com.google.api.client.http.HttpResponse response;

    /**
     * Creates a HTTP response.
     * @param response - HTTP response
     */
    public HttpResponse(com.google.api.client.http.HttpResponse response) {
        this.response = response;
    }

    /**
     * @return - Returns whether received a successful HTTP status code >= 200 && < 300 (see getStatusCode()).
     */
    public boolean isSuccessStatusCode() {
        return response.isSuccessStatusCode();
    }

    /**
     * @return - Returns the HTTP status code or 0 for none.
     */
    public int getStatusCode() {
        return response.getStatusCode();
    }

    /**
     * @return - Returns the HTTP status message or null for none.
     */
    public String getStatusMessage() {
        return response.getStatusMessage();
    }

    /**
     * @return - Returns the HTTP response headers.
     */
    public HttpHeaders getHeaders() {
        return response.getRequest().getResponseHeaders();
    }

    /**
     * Parses the content of the HTTP response from getContent() and reads it into a string.
     * Since this method returns "" for no content, a simpler check for no content is to check if getContent() is null.
     * All content is read from the input content stream rather than being limited by the Content-Length. For the character set, it follows the specification by parsing the "charset" parameter of the Content-Type header or by default "ISO-8859-1" if the parameter is missing.
     * @return - parsed string or "" for no content
     * @throws IOException - I/O exception
     */
    public String parseAsString() throws IOException {
        return response.parseAsString();
    }

    /**
     * Returns the content of the HTTP response.
     * The result is cached, so subsequent calls will be fast.
     * Callers should call InputStream.close() after the returned InputStream is no longer needed.
     * disconnect() does not have to be called if the content is closed.
     * @return - input stream content of the HTTP response or null for none
     * @throws IOException
     */
    public InputStream getContent() throws IOException {
        return response.getContent();
    }
}

package com.dev9.webtest.sauce;

import java.net.URL;

/**
 * Request object used to package requests sent through SauceREST
 *
 * User: yurodivuie
 * Date: 2/29/12
 * Time: 11:05 AM
 */
public class SauceRESTRequest {

    public URL requestUrl;
    public String method;
    public String jsonParameters;

    /**
     * Constructs complete object.  To build incrementally, use SauceRESTRequestBuilder.  Most
     * requests that are possible in the API are coded as methods in SauceREST.
     *
     * @param url               Complete url for the RESTful request
     * @param method            HttpMethod to use (get, post, delete, or put supported by SauceREST).
     * @param jsonParameters    A JSON string to be used as parameters for a post or put.
     */
    public SauceRESTRequest(URL url, String method, String jsonParameters) {
        this.requestUrl = url;
        this.method = method;
        this.jsonParameters = jsonParameters;
    }

    public URL getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(URL requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getJsonParameters() {
        return jsonParameters;
    }

    public void setJsonParameters(String jsonParameters) {
        this.jsonParameters = jsonParameters;
    }
}

package com.dynacrongroup.webtest.sauce;

import java.net.URL;

/**
 * User: yurodivuie
 * Date: 2/29/12
 * Time: 11:05 AM
 */
public class SauceRESTRequest {

    public URL requestUrl;
    public String method;
    public String jsonParameters;

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

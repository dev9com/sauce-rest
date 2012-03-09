package com.dynacrongroup.webtest.sauce;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Based on Sauce Labs code, on github: https://github.com/saucelabs/saucerest-java.
 * <p/>
 * Used to set the pass/fail status on tests.
 */
public class SauceREST {

    protected String username;
    protected ClientFactory clientFactory;


    private static final Logger LOG = LoggerFactory.getLogger(SauceREST.class);

    public SauceREST(String username, String accessKey) {
        this.username = username;
        this.clientFactory = new ClientFactory(username, accessKey);
    }

    public JSONObject getAccountDetails() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUsersToPath()
                .addUserIdToPath(username)
                .build();

        return (JSONObject) sendRestRequest(request);
    }


    public JSONObject getUsageData() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUsersToPath()
                .addUserIdToPath(username)
                .addGenericSuffix("/usage")
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    public JSONArray getAllJobs() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUserIdToPath(username)
                .addJobsToPath()
                .build();

        return (JSONArray) sendRestRequest(request);
    }

    public JSONObject getJobStatus(String jobId) {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUserIdToPath(username)
                .addJobsToPath()
                .addJobIdToPath(jobId)
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    public JSONObject jobPassed(String jobId) {
        return updateJob(jobId, "passed", true);
    }

    public JSONObject jobFailed(String jobId) {
        return updateJob(jobId, "passed", false);
    }

    public JSONObject updateJob(String jobId, String jsonKey, Object jsonValue) {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .addJSON(jsonKey, jsonValue)
                .setHTTPMethod("PUT")
                .addUserIdToPath(username)
                .addJobsToPath()
                .addJobIdToPath(jobId)
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    /**
     * Stop a currently running job.  Should not be necessary to use in WebDriverBase
     *
     * @param jobId
     * @return
     */
    public JSONObject stopJob(String jobId) {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("PUT")
                .addUserIdToPath(username)
                .addJobsToPath()
                .addJobIdToPath(jobId)
                .addGenericSuffix("/stop")
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    public boolean isTunnelPresent() {
        JSONArray tunnels = getAllTunnels();
        return (!tunnels.isEmpty());
    }

    public JSONArray getAllTunnels() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUserIdToPath(username)
                .addGenericSuffix("/tunnels")
                .build();

        return (JSONArray) sendRestRequest(request);
    }

    public JSONObject getTunnelStatus(String tunnelId) {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUserIdToPath(username)
                .addGenericSuffix("/tunnels/")
                .addGenericSuffix(tunnelId)
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    public JSONObject deleteTunnel(String tunnelId) {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("DELETE")
                .addUserIdToPath(username)
                .addGenericSuffix("/tunnels/")
                .addGenericSuffix(tunnelId)
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    public JSONObject getSauceStatus() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addGenericSuffix("/info/status")
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    public JSONArray getSauceBrowsers() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addGenericSuffix("/info/browsers")
                .build();

        return (JSONArray) sendRestRequest(request);
    }

    public Object sendRestRequest(SauceRESTRequest request) {

        Object result = null;

        HttpClient client = clientFactory.getClient();
        HttpMethod method = MethodFactory.getMethod(request.getMethod(),
                request.getRequestUrl().toExternalForm(),
                request.getJsonParameters());
        String response = getResponse(client, method);

        if (response != null) {
            result = JSONValue.parse(response);
        }

        return result;
    }

    static String getResponse(HttpClient client, HttpMethod method) {

        String response = null;

        try {
            Integer responseCode = client.executeMethod(method);
            if (responseCode == 200) {
                response = method.getResponseBodyAsString();
                if (response != null) {
                    LOG.trace("Raw result: {}", response);
                }
            }
            else {
                LOG.error("Request [{}] failed: {} error: {}",
                        new Object[]{method.getURI().toString(),
                                responseCode,
                                response});
            }
        } catch (IOException e) {
            LOG.error("Exception while trying to execute rest request: {}\n{}",
                    new Object[]{e.getMessage(), e.getStackTrace()});
        }

        return response;
    }

}
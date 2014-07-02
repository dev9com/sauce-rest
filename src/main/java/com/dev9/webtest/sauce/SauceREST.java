package com.dev9.webtest.sauce;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.IOException;


/**
 * Used to access the Sauce Labs rest API.
 *
 * <p/>
 * Based on Sauce Labs code, on github: https://github.com/saucelabs/saucerest-java.
 */
public class SauceREST {

    protected String username;
    protected String accessKey;

    private static final Logger LOG = LoggerFactory.getLogger(SauceREST.class);

    /**
     * Constructs a SauceREST object with all necessary authentication information.
     *
     * @param username  A Sauce Labs user name.
     * @param accessKey The Sauce Labs key corresponding to the user name.
     */
    public SauceREST(String username, String accessKey) {
        this.username = username;
        this.accessKey = accessKey;
    }

    /**
     * Returns details for a given Sauce Labs account, including minutes used.
     * @return
     */
    public JSONObject getAccountDetails() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUsersToPath()
                .addUserIdToPath(username)
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    /**
     * Returns detailed usage data for the Sauce Labs account, including minutes
     * used on a daily basis.
     *
     * @return
     */
    public JSONObject getUsageData() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUsersToPath()
                .addUserIdToPath(username)
                .addGenericSuffix("/usage")
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    /**
     * Gets a list of all jobs ever for the user, not just current jobs.
     * @return
     */
    public JSONArray getAllJobs() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUserIdToPath(username)
                .addJobsToPath()
                .build();

        return (JSONArray) sendRestRequest(request);
    }

    /**
     * Returns status details for a given job, including pass/fail and
     * all tags.
     *
     * @param jobId The session id for a Sauce job.
     * @return
     */
    public JSONObject getJobStatus(String jobId) {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addUserIdToPath(username)
                .addJobsToPath()
                .addJobIdToPath(jobId)
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    /**
     * Marks a job as passed; will appear as such in jobs list in Sauce Labs.
     * @param jobId The session id for the Sauce Job that passed.
     * @return
     */
    public JSONObject jobPassed(String jobId) {
        return updateJob(jobId, "passed", true);
    }

    /**
     * Marks a job as failed; will appear as such in jobs list in Sauce Labs.
     * @param jobId The session id for the Sauce Job that failed.
     * @return
     */
    public JSONObject jobFailed(String jobId) {
        return updateJob(jobId, "passed", false);
    }

    /**
     * Updates a job in Sauce Labs with an arbitrary JSON key/value pair.  Note that
     * Sauce Labs will not accept arbitrary values; see documentation on their site.
     *
     * @param jobId     The session id for the Sauce job to update.
     * @param jsonKey   A String key for the json object.
     * @param jsonValue A value for the json object (discrete, a list, or a map, for instance).
     * @return
     */
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
     * Stop a currently running job.  Should not be necessary to use in parallel-webtest.
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

    /**
     * Verifies that one or more tunnels exist (but does not check tunnel state).
     * @return
     */
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

    /**
     * Not currently supported by Sauce Labs, but will one day return status of Sauce Labs services.
     *
     * @return
     */
    public JSONObject getSauceStatus() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addGenericSuffix("/info/status")
                .build();

        return (JSONObject) sendRestRequest(request);
    }

    /**
     * Gets a list of all currently supported browser/os combinations.
     * @return
     */
    public JSONArray getSauceBrowsers() {
        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setHTTPMethod("GET")
                .addGenericSuffix("/info/browsers")
                .build();

        return (JSONArray) sendRestRequest(request);
    }

    /**
     * Send a request to Sauce Labs configured using a SauceRESTRequest object.
     *
     * @param request   An object containing all details for the REST request.
     * @return
     */
    public Object sendRestRequest(SauceRESTRequest request) {

        Object result = null;
        HttpClient client = HttpClients.createDefault();
        HttpUriRequest httpRequest = MethodFactory.getRequest(request.getMethod(),
                request.getRequestUrl().toExternalForm(),
                request.getJsonParameters());
        String auth = username + ":" + accessKey;
        BASE64Encoder encoder = new BASE64Encoder();
        auth = "Basic " + encoder.encode(auth.getBytes());
        httpRequest.addHeader("Authorization", auth);
        String response = getResponse(client, httpRequest);

        if (response != null) {
            result = JSONValue.parse(response);
        }

        return result;
    }

    static String getResponse(HttpClient client, HttpUriRequest request) {

        String responseMessage = null;

        try {
            HttpResponse response = client.execute(request);
            Integer responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                responseMessage = IOUtils.toString(response.getEntity().getContent());
                if (responseMessage != null) {
                    LOG.trace("Raw result: {}", response);
                }
            }
            else {
                LOG.error("Request [{}] failed: {} error: {}",
                        request.getURI().toString(),
                        responseCode,
                        response);
            }
        } catch (IOException e) {
            LOG.error("Exception while trying to execute rest request: {}\n{}",
                    new Object[]{e.getMessage(), e.getStackTrace()});
        }

        return responseMessage;
    }

}
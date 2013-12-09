package com.dynacrongroup.webtest.sauce;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class InSauceRestTest {

    private static final Logger log = LoggerFactory
            .getLogger(InSauceRestTest.class);

    private static String USER = System.getenv("SAUCELABS_USER");
    private static String KEY = System.getenv("SAUCELABS_KEY");

    private static WebDriver driver = null;
    SauceREST sauceREST = new SauceREST(USER, KEY);


    @Before
    public void createConnection() throws Exception {

        if (driver == null) {

            DesiredCapabilities capabillities = new DesiredCapabilities("iexplore",
                    "7", Platform.WINDOWS);
            capabillities.setCapability("name", "InSauceRestTest");
            capabillities.setCapability("tags", "test-tag");
            capabillities.setCapability("build", "12345");

            log.info("capabilities: "
                    + new BeanToJsonConverter().convert(capabillities));
            driver = new RemoteWebDriver(getConnectionString(), capabillities);
        }
    }

    @Test
    public void getAllJobs() {
        JSONArray allJobs = sauceREST.getAllJobs();

        assertNotNull(allJobs);

        assertThat(allJobs.toString(), containsString(getJobID()));
    }

    @Test
    public void getJobStatus() {
        JSONObject jobStatus = sauceREST.getJobStatus(getJobID());

        assertNotNull(jobStatus);
        assertThat(jobStatus.toJSONString(), containsString("in progress"));
    }


    @Test
    public void setJobStatusPassed() {
        sauceREST.jobPassed(getJobID());

        JSONObject jobStatus = sauceREST.getJobStatus(getJobID());
        assertNotNull(jobStatus);
        Boolean status = (Boolean) jobStatus.get("passed");
        log.info("Job status: {}", jobStatus.toString());
        assertThat(status, equalTo(true));
    }

    @Test
    public void setJobStatusFailed() {
        sauceREST.jobFailed(getJobID());

        JSONObject jobStatus = sauceREST.getJobStatus(getJobID());
        assertNotNull(jobStatus);
        Boolean status = (Boolean) jobStatus.get("passed");
        assertThat(status, equalTo(false));
    }

    @Test
    public void sendRequestWithoutVersion() throws MalformedURLException {

        SauceRESTRequest request = new SauceRESTRequestBuilder()
                .setVersion(null)
                .setHTTPMethod("GET")
                .addGenericSuffix("/info/browsers")
                .build();

        assertThat(request.getRequestUrl(), equalTo(new URL("https://saucelabs.com/rest/info/browsers")));

        JSONObject jobStatus = (JSONObject) sauceREST.sendRestRequest(request);
        assertNull(jobStatus);
    }

    @Test
    public void checkTunnelStatus() {
        JSONArray allTunnels = sauceREST.getAllTunnels();
        assertNotNull(allTunnels);
        assertThat(sauceREST.isTunnelPresent(), not(equalTo(allTunnels.isEmpty())));
    }


    @AfterClass
    public static void shutDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private String getJobID() {
        return ((RemoteWebDriver) driver).getSessionId().toString();
    }

    private URL getConnectionString() throws MalformedURLException {
        return new URL("http://" + USER + ":" + KEY + "@"
                + "ondemand.saucelabs.com/wd/hub");
    }
}

package com.dev9.webtest.sauce;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * User: yurodivuie
 * Date: 3/9/12
 * Time: 2:39 PM
 */
public class SauceRestRequestTest {

    private final URL defaultUrl;
    private final String defaultMethod = "get";
    private final String defaultJson = null;

    private SauceRESTRequest sauceRESTRequest;

    public SauceRestRequestTest() throws Exception {
        defaultUrl = new URL("http://www.dev9.com/");
    }

    @Before
    public void createDefault() {
        sauceRESTRequest = new SauceRESTRequest(defaultUrl, defaultMethod, defaultJson);
    }

    @Test
    public void testSetUrl() throws MalformedURLException {

        assertThat(sauceRESTRequest.getRequestUrl(), equalTo(defaultUrl));

        URL newUrl = new URL("http://www.google.com/");
        sauceRESTRequest.setRequestUrl(newUrl);
        assertThat(sauceRESTRequest.getRequestUrl(), equalTo(newUrl));
    }

    @Test
    public void testSetMethod() {

        assertThat(sauceRESTRequest.getMethod(), equalTo(defaultMethod));

        String newMethod = "delete";
        sauceRESTRequest.setMethod(newMethod);
        assertThat(sauceRESTRequest.getMethod(), equalTo(newMethod));
    }

    @Test
    public void testSetJson() {

        assertThat(sauceRESTRequest.getJsonParameters(), equalTo(defaultJson));

        String newJson = "json";
        sauceRESTRequest.setJsonParameters(newJson);
        assertThat(sauceRESTRequest.getJsonParameters(), equalTo(newJson));
    }

}

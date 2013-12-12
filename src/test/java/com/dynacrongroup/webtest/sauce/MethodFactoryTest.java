package com.dynacrongroup.webtest.sauce;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 4:00 PM
 */
public class MethodFactoryTest {

    /**
     * This is a special bit of JUnit magic to get the name of the test
     */
    @Rule
    public TestName name = new TestName();

    private static final Logger LOG = LoggerFactory.getLogger(MethodFactoryTest.class);
    private String testSite = "http://www.dynacrongroup.com/";

    @Test
    public void testGet() {
        LOG.info("Starting [{}]", name.getMethodName());
        HttpUriRequest request = MethodFactory.getRequest("get", testSite, null);

        assertThat(request.getMethod(), equalToIgnoringCase("get"));
        assertThat(request.getURI().toString(), equalToIgnoringCase(testSite));
    }

    @Test
    public void testPutNoParam() {
        LOG.info("Starting [{}]", name.getMethodName());
        HttpUriRequest request = MethodFactory.getRequest("put", testSite, null);

        assertThat(request.getMethod(), equalToIgnoringCase("put"));
        assertThat(request.getURI().toString(), equalToIgnoringCase(testSite));
        assertThat(((HttpEntityEnclosingRequestBase)request).getEntity(), nullValue());
    }

    @Test
    public void testPutWithJson() throws IOException {
        LOG.info("Starting [{}]", name.getMethodName());
        HttpUriRequest request = MethodFactory.getRequest("put", testSite, "json");

        assertThat(request.getMethod(), equalToIgnoringCase("put"));
        assertThat(request.getURI().toString(), equalToIgnoringCase(testSite));

        HttpEntity entity = ((HttpEntityEnclosingRequestBase) request).getEntity();


        assertThat(entity, not(nullValue()));
        assertThat(entity.getContentType().getValue(), containsString("application/json"));
        assertThat(IOUtils.toString(((StringEntity) entity).getContent()), equalToIgnoringCase("json"));
    }

    @Test
    public void testPostNoJson() {
        LOG.info("Starting [{}]", name.getMethodName());
        HttpUriRequest request = MethodFactory.getRequest("post", testSite, null);

        assertThat(request.getMethod(), equalToIgnoringCase("post"));
        assertThat(request.getURI().toString(), equalToIgnoringCase(testSite));
        assertThat(((HttpEntityEnclosingRequestBase)request).getEntity(), nullValue());
    }

    @Test
    public void testPostWithJson() throws IOException {
        LOG.info("Starting [{}]", name.getMethodName());
        HttpUriRequest request = MethodFactory.getRequest("post", testSite, "json");

        assertThat(request.getMethod(), equalToIgnoringCase("post"));
        assertThat(request.getURI().toString(), equalToIgnoringCase(testSite));

        final HttpEntity entity = ((HttpEntityEnclosingRequestBase) request).getEntity();

        assertThat(entity, not(nullValue()));
        assertThat(entity.getContentType().getValue(), containsString("application/json"));
        assertThat(IOUtils.toString(((StringEntity) entity).getContent()), equalToIgnoringCase("json"));
    }

    @Test
    public void testDelete() {
        LOG.info("Starting [{}]", name.getMethodName());
        HttpUriRequest request = MethodFactory.getRequest("delete", testSite, null);

        assertThat(request.getMethod(), equalToIgnoringCase("delete"));
        assertThat(request.getURI().toString(), equalToIgnoringCase(testSite));
    }


    @Test
    public void testInvalidMethod() {
        LOG.info("Starting [{}]", name.getMethodName());
        HttpUriRequest request = MethodFactory.getRequest("garbage", testSite, null);

        assertThat(request, is(nullValue()));
    }
}

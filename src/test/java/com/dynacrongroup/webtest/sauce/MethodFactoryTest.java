package com.dynacrongroup.webtest.sauce;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 4:00 PM
 */
public class MethodFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodFactoryTest.class);
    private String testSite = "http://www.dynacrongroup.com/";

    @Test
    public void testGet() throws URIException {
        HttpMethod method = MethodFactory.getMethod("get", testSite, null);

        assertThat(method.getName(), equalToIgnoringCase("get"));
        assertThat(method.getURI().toString(), equalToIgnoringCase(testSite));
    }

    @Test
    public void testPutNoParam() throws URIException {
        HttpMethod method = MethodFactory.getMethod("put", testSite, null);

        assertThat(method.getName(), equalToIgnoringCase("put"));
        assertThat(method.getURI().toString(), equalToIgnoringCase(testSite));
        assertThat(((EntityEnclosingMethod)method).getRequestEntity(), nullValue());
    }

    @Test
    public void testPutWithJson() throws URIException {
        HttpMethod method = MethodFactory.getMethod("put", testSite, "json");

        assertThat(method.getName(), equalToIgnoringCase("put"));
        assertThat(method.getURI().toString(), equalToIgnoringCase(testSite));

        RequestEntity entity = ((EntityEnclosingMethod) method).getRequestEntity();


        assertThat(entity, not(nullValue()));
        assertThat(entity.getContentType(), containsString("application/json"));
        assertThat(((StringRequestEntity)entity).getContent(), equalToIgnoringCase("json"));
    }

    @Test
    public void testPostNoJson() throws URIException {
        HttpMethod method = MethodFactory.getMethod("post", testSite, null);

        assertThat(method.getName(), equalToIgnoringCase("post"));
        assertThat(method.getURI().toString(), equalToIgnoringCase(testSite));
        assertThat(((EntityEnclosingMethod)method).getRequestEntity(), nullValue());
    }

    @Test
    public void testPostWithJson() throws URIException {
        HttpMethod method = MethodFactory.getMethod("post", testSite, "json");

        assertThat(method.getName(), equalToIgnoringCase("post"));
        assertThat(method.getURI().toString(), equalToIgnoringCase(testSite));

        RequestEntity entity = ((EntityEnclosingMethod) method).getRequestEntity();


        assertThat(entity, not(nullValue()));
        assertThat(entity.getContentType(), containsString("application/json"));
        assertThat(((StringRequestEntity)entity).getContent(), equalToIgnoringCase("json"));
    }

    @Test
    public void testDelete() throws URIException {
        HttpMethod method = MethodFactory.getMethod("delete", testSite, null);

        assertThat(method.getName(), equalToIgnoringCase("delete"));
        assertThat(method.getURI().toString(), equalToIgnoringCase(testSite));
    }

}

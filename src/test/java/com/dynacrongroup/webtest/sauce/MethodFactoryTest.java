package com.dynacrongroup.webtest.sauce;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Test
    public void testGet() throws URIException {
        HttpMethod method = MethodFactory.getMethod("get", "http://www.dynacrongroup.com/", null);

        assertThat(method.getName(), equalToIgnoringCase("get"));
        assertThat(method.getURI().toString(), equalToIgnoringCase("http://www.dynacrongroup.com/"));
    }

    @Test
    public void testPutNoParam() throws URIException {
        HttpMethod method = MethodFactory.getMethod("put", "http://www.dynacrongroup.com/", null);

        assertThat(method.getName(), equalToIgnoringCase("put"));
        assertThat(method.getURI().toString(), equalToIgnoringCase("http://www.dynacrongroup.com/"));
        assertThat(((EntityEnclosingMethod)method).getRequestEntity(), nullValue());
    }

    @Test
    public void testPutWithJson() throws URIException {
        HttpMethod method = MethodFactory.getMethod("put", "http://www.dynacrongroup.com/", "json");

        assertThat(method.getName(), equalToIgnoringCase("put"));
        assertThat(method.getURI().toString(), equalToIgnoringCase("http://www.dynacrongroup.com/"));

        RequestEntity entity = ((EntityEnclosingMethod) method).getRequestEntity();


        assertThat(entity, not(nullValue()));
        assertThat(((StringRequestEntity)entity).getContent(), equalToIgnoringCase("json"));
    }

    @Test
    public void testPostNoJson() throws URIException {
        HttpMethod method = MethodFactory.getMethod("post", "http://www.dynacrongroup.com/", null);

        assertThat(method.getName(), equalToIgnoringCase("post"));
        assertThat(method.getURI().toString(), equalToIgnoringCase("http://www.dynacrongroup.com/"));
        assertThat(((EntityEnclosingMethod)method).getRequestEntity(), nullValue());
    }

    @Test
    public void testPostWithJson() throws URIException {
        HttpMethod method = MethodFactory.getMethod("post", "http://www.dynacrongroup.com/", "json");

        assertThat(method.getName(), equalToIgnoringCase("post"));
        assertThat(method.getURI().toString(), equalToIgnoringCase("http://www.dynacrongroup.com/"));

        RequestEntity entity = ((EntityEnclosingMethod) method).getRequestEntity();


        assertThat(entity, not(nullValue()));
        assertThat(((StringRequestEntity)entity).getContent(), equalToIgnoringCase("json"));
    }

    @Test
    public void testDelete() throws URIException {
        HttpMethod method = MethodFactory.getMethod("delete", "http://www.dynacrongroup.com/", null);

        assertThat(method.getName(), equalToIgnoringCase("delete"));
        assertThat(method.getURI().toString(), equalToIgnoringCase("http://www.dynacrongroup.com/"));
    }

}

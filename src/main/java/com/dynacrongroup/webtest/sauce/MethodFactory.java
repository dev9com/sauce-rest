package com.dynacrongroup.webtest.sauce;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 3:35 PM
 * <p/>
 * Created to isolate more of the HTTPMethod logic.
 */
class MethodFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SauceREST.class);

    private MethodFactory() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class should not be constructed");
    }

    static HttpMethod getMethod(String methodType, String url, String optionalJson) {

        HttpMethod method;

        if (methodType.equalsIgnoreCase("get")) {
            method = new GetMethod(url);
        } else if (methodType.equalsIgnoreCase("delete")) {
            method = new DeleteMethod(url);
        } else {

            EntityEnclosingMethod eMethod = null;

            if (methodType.equalsIgnoreCase("post")) {
                eMethod = new PostMethod(url);
            } else if (methodType.equalsIgnoreCase("put")) {
                eMethod = new PutMethod(url);
            }

            if (optionalJson != null) {
                try {
                    eMethod.setRequestEntity(new StringRequestEntity(optionalJson, "application/json", "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.error(e.getMessage());
                }
            }

            method = eMethod;
        }

        return method;
    }

}

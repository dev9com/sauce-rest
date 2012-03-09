package com.dynacrongroup.webtest.sauce;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 3:35 PM
 *
 * Created to provide a better way to inject HTTPClient into tests, hopefully.
 */
public class ClientFactory {

    String user;
    String key;

    public ClientFactory(String user, String key) {
        this.user = user;
        this.key = key;
    }

    public HttpClient getClient() {

        HttpClient client = new HttpClient();

        Credentials defaultcreds = new UsernamePasswordCredentials(user, key);
        client.getState().setCredentials(AuthScope.ANY, defaultcreds);

        return client;
    }

}

package com.fedii.requests.builders;

import com.fedii.tools.Config;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfedii on 4/6/16.
 */
public class GetBuilder {
    private HttpGet request;
    private List<NameValuePair> urlParameters = new ArrayList<>();
    public GetBuilder(String function) {
        request = new HttpGet(Config.getRecruiterUrl() + File.separatorChar + function);
    }

    public GetBuilder setParameter(String key,
                                   String value) {
        urlParameters.add(new BasicNameValuePair(key, value));

        return this;
    }

    public HttpGet generate() {
        return request;
    }
}

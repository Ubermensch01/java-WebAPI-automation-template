package com.fedii.requests.builders;

import com.fedii.tools.Config;
import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfedii on 4/4/16.
 */
public class PostBuilder {
    private HttpPost request;
    private List<NameValuePair> urlParameters = new ArrayList<>();
    private String url = Config.getRecruiterUrl();
    private String function;
    public PostBuilder(String function) {
        request = new HttpPost(url + File.separatorChar + function);
    }

    public PostBuilder setUrl(String url) {
        this.url = url;

        return this;
    }

    public PostBuilder setParameter(String key,
                                    String value) {
        try {
            urlParameters.add(new BasicNameValuePair(key,
                    URLEncoder.encode(value, "UTF-8")
//                                               value
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public PostBuilder setContentType(String contentType) {
        request.setHeader("Content-Type", contentType);

        return this;
    }

    public PostBuilder setEntity(Object data) {
        return setEntity(new Gson().toJson(data));
    }

    public PostBuilder setEntity(String data) {
        try {
            StringEntity entity = new StringEntity(data);
            entity.setContentType("application/json");
            request.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return this;
    }

    public PostBuilder setHeader(String name,
                                 String value) {
        if (request.getHeaders(name).length == 0) {
            request.setHeader(new BasicHeader(name, value));
        } else {
            request.setHeader(name, request.getHeaders(name)[0].getValue() + ";" + value);
        }

        return this;
    }

    public HttpPost generate() {

        try {
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return request;
    }
}

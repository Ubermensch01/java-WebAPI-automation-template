package com.fedii;

import com.fedii.requests.builders.PostBuilder;
import com.fedii.tools.Config;
import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.testng.annotations.BeforeMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sfedii on 4/5/16.
 */
public class BaseTest {
    private HttpClient client;
    private CookieStore cookieStore;

    @BeforeMethod
    public void setup() {
        cookieStore = new BasicCookieStore();
        client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }

    public String[] getCookies(HttpResponse response) {
        Header[] headerCookies = response.getHeaders("Set-Cookie");
        List<String> cookieList = new LinkedList<>();
        for (Header cookie : headerCookies) {
            cookieList.add(cookie.getValue());
        }

        return (String[]) cookieList.toArray();
    }

    public HttpResponse loginAs(String username,
                                String password) {
        PostBuilder builder = new PostBuilder("j_acegi_security_check")
                .setParameter("LOGIN_USER", username)
                .setParameter("LOGIN_PASS", password);
        HttpPost request = builder.generate();
        HttpResponse response = execute(request);
        if (response.getHeaders("Set-Cookie").length != 0) {
            setCookie(response.getHeaders("Set-Cookie")[0]);
        }

        return response;
    }

    public HttpResponse loginAsDefaultUser() {
        return loginAs(Config.getDefaultCredentials().getUsername(), Config.getDefaultCredentials().getPassword());
    }

    public HttpResponse execute(HttpUriRequest request) {
        HttpResponse response = null;

        try {
            System.out.println(client);
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void setCookie(Header cookie) {
        BasicClientCookie result = new BasicClientCookie(cookie.getName(), cookie.getValue());
        result.setDomain(Config.getRecruiterUrl());
        cookieStore.addCookie(result);
//    client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }

    public String getContent(HttpResponse response) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String getStatus(HttpResponse response) {
        return String.valueOf(response.getStatusLine());
    }

    public String jsonify(Object object) {
        return new Gson().toJson(object);
    }

    public <T> T objectify(String json,
                           Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }
}

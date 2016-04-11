package com.fedii.tools;

import com.fedii.entities.Credentials;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sfedii on 2/29/16.
 */
public class Config {
    static Properties properties = new Properties();
    private static Config instance;

    static {
        instance = new Config();
    }

    private Config() {
        properties = new Properties();
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Credentials getDefaultCredentials() {
        return new Credentials(getProperty("username"), getProperty("password"));
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getRecruiterUrl() {
        return properties.getProperty("recruiterurl");
    }

    public static String getJobSeekerUrl() {
        return properties.getProperty("jobseekerurl");
    }
}

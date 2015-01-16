package com.m11n.jdbc.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class HystrixConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HystrixConfiguration.class);

    public static final String CONFIG = "jdbc.hystrix.config";
    public static final String DRIVER_PREFIX = "jdbc:hystrix:";

    private Properties config;

    private final Set<String> allowed = new HashSet<>();

    public HystrixConfiguration() {
        importAllowedPropertyNames();

        config = new Properties();

        try {
            InputStream is;

            String path = System.getProperty(CONFIG)==null ? "hystrix.properties" : System.getProperty(CONFIG);

            File f = new File(path);

            if(f.exists()) {
                is = new FileInputStream(f);
            } else {
                is = HystrixConfiguration.class.getClassLoader().getResourceAsStream(path);
            }

            if(is!=null) {
                config.load(is);
            }

            check(config);

            config = setDefaults(config);
        } catch(Exception e) {
            logger.warn(e.toString(), e);
        }
    }

    public HystrixConfiguration(Properties c) {
        this();

        // TODO: implement this

        for(String key : allowed) {
            if(c.getProperty(key)!=null && !"".equals(c.getProperty(key).trim())) {
                config.setProperty(key, c.getProperty(key));
            }
        }

        if(logger.isDebugEnabled()) {
            logger.debug("Configuration: {}", config);
        }
    }

    private void importAllowedPropertyNames() {
        try {
            allowed.clear();

            BufferedReader reader = new BufferedReader(new InputStreamReader(HystrixConfiguration.class.getClassLoader().getResourceAsStream("PROPERTIES")));

            String line;

            while((line=reader.readLine())!=null) {
                allowed.add(line);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Properties setDefaults(Properties c) {
        // TODO: implement this

        return c;
    }

    private void check(Properties c) {
        Enumeration<Object> keys = c.keys();
        while(keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            if(!allowed.contains(key)) {
                logger.warn("Skipping property: {}", key);
                c.remove(key);
            }
        }
    }

    private String getSystemPropertyOrDefault(String name, String defaultValue) {
        return System.getProperty(name)==null ? defaultValue : System.getProperty(name);
    }

    public Properties getProperties() {
        return config;
    }

    public String getProperty(String name) {
        return config.getProperty(name);
    }
}

package com.m11n.jdbc.hystrix;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import static com.m11n.jdbc.hystrix.HystrixConfiguration.*;

public class HystrixDriver implements Driver {
    private static final int VERSION_MAJOR = 1;
    private static final int VERSION_MINOR = 0;

    static
    {
        try
        {
            java.sql.DriverManager.registerDriver(new HystrixDriver());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return (url != null && url.startsWith(DRIVER_PREFIX));
    }

    private String extractUrl(String url) {
        return url.startsWith(DRIVER_PREFIX) ? url.replace("hystrix:", "") : url;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (url == null) {
            throw new SQLException("URL is required");
        }

        if( !acceptsURL(url) ) {
            return null;
        }

        HystrixConfiguration config = configure(url, info);

        // TODO: check if this is enough for most common drivers
        String realUrl = extractUrl(url);
        Driver driver = findDriver(realUrl);

        return driver.connect(realUrl, config.getProperties());
    }

    private HystrixConfiguration configure(String url, Properties info) throws SQLException {
        HystrixConfiguration config;

        try {
            URL u = toURL(url);

            Properties properties = new Properties();

            if(info!=null && !info.isEmpty()) {
                properties.putAll(info);
            }

            if(u.getQuery()!=null) {
                String[] parts = u.getQuery().split("&");

                for(String part : parts) {
                    String[] pair = part.split("=");
                    if(pair!=null && pair.length==2) {
                        properties.setProperty(pair[0], pair[1]);
                    }
                }
            }

            config = new HystrixConfiguration(properties);
        } catch (Exception e) {
            throw new SQLException(e);
        }

        return config;
    }

    private URL toURL(String url) throws MalformedURLException {
        // NOTE: trick to parse multipart scheme of most common JDBC URLs
        String sanitizedString = null;
        int schemeEndOffset = url.indexOf("://");
        if (-1 == schemeEndOffset) {
            // couldn't find one? try our best here.
            sanitizedString = "http://" + url;
        } else {
            sanitizedString = "http" + url.substring(schemeEndOffset);
        }

        return new URL(sanitizedString);
    }

    private Driver findDriver(String url) throws SQLException {
        Driver realDriver = null;

        for(Enumeration<Driver> drivers = DriverManager.getDrivers(); drivers.hasMoreElements();) {
            try {
                Driver driver = drivers.nextElement();

                if (driver.acceptsURL(url)) {
                    realDriver = driver;
                    break;
                }
            } catch (SQLException e) {
            }
        }

        if( realDriver == null ) {
            throw new SQLException("Unable to find a driver that accepts " + url);
        }

        return realDriver;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return findDriver(url).getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return VERSION_MAJOR;
    }

    @Override
    public int getMinorVersion() {
        return VERSION_MINOR;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }
}

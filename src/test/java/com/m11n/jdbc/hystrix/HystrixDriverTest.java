package com.m11n.jdbc.hystrix;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Enumeration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class HystrixDriverTest {
    private static final Logger logger = LoggerFactory.getLogger(HystrixDriverTest.class);

    protected String hystrixUrl;

    protected String realUrl;

    protected String sql;

    @Test
    public void testDriverRegistration() throws SQLException {
        boolean found = false;

        for(Enumeration<Driver> drivers = DriverManager.getDrivers(); drivers.hasMoreElements();) {
            Driver driver = drivers.nextElement();

            if(driver.getClass().equals(HystrixDriver.class)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    public void testHystrixDriver() throws Exception {
        Connection connection = DriverManager.getConnection(hystrixUrl);

        logger.info("Info: {}", connection.getClientInfo());

        /**
        DatabaseMetaData metadata = connection.getMetaData();

        // Get all the tables and views
        String[] tableType = {"TABLE"};
        java.sql.ResultSet tables = metadata.getTables(null, null, "%", tableType);

        assertNotNull(tables);

        String tableName;
        while (tables.next()) {
            tableName = tables.getString(3);

            logger.info("Table: {}", tableName);
        }
         */
    }

    @Test
    public void testRealDriver() throws Exception {
        Connection connection = DriverManager.getConnection(realUrl);

        Statement s = connection.createStatement();
        s.execute(sql);

        DatabaseMetaData metadata = connection.getMetaData();

        // Get all the tables and views
        String[] tableType = {"TABLE"};
        java.sql.ResultSet tables = metadata.getTables(null, null, "%", tableType);

        assertNotNull(tables);

        String tableName;
        while (tables.next()) {
            tableName = tables.getString(3);

            logger.info("Table: {}", tableName);
        }
    }
}

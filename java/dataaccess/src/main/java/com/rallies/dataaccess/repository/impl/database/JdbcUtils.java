package com.rallies.dataaccess.repository.impl.database;

import com.rallies.dataaccess.repository.impl.database.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class JdbcUtils {
    private Logger logger = LogManager.getLogger();
    private Connection connection;
    private Properties databaseConnectionProperties;

    public JdbcUtils(Properties databaseConnectionProperties) {
        this.databaseConnectionProperties = databaseConnectionProperties;
    }

    public Connection getConnection() {
        logger.traceEntry("Requesting com.rallies.dataaccess.repository.impl.database connection...");
        try {
            if (connection == null || connection.isClosed())
                connection = createNewConnection();
            logger.traceExit();
            return connection;
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(e);
        }
    }

    private Connection createNewConnection() {
        logger.traceEntry("Creating new com.rallies.dataaccess.repository.impl.database connection...");
        String url = databaseConnectionProperties.getProperty("jdbc.url");
        String user = databaseConnectionProperties.getProperty("jdbc.user");
        String password = databaseConnectionProperties.getProperty("jdbc.password");
        logger.info("url {}", url);
        logger.info("user {}", user);
        logger.info("password {}", password);
        try {
            Connection connectionToBeReturned;
            if (user != null && password != null)
                connectionToBeReturned = DriverManager.getConnection(url, user, password);
            else
                connectionToBeReturned = DriverManager.getConnection(url);
            logger.traceExit();
            return connectionToBeReturned;
        } catch (SQLException exception) {
            logger.error(exception);
            throw new DatabaseException(exception);
        }
    }
}

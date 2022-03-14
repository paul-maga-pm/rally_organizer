package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Logger logger = LogManager.getLogger();
    private Connection connection;
    private Properties databaseConnectionProperties;

    public JdbcUtils(Properties databaseConnectionProperties) {
        this.databaseConnectionProperties = databaseConnectionProperties;
    }

    public Connection getConnection() {
        logger.traceEntry("Requesting database connection...");
        try {
            if (connection == null || connection.isClosed())
                connection = createNewConnection();
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit();
        return connection;
    }

    private Connection createNewConnection() {
        logger.traceEntry("Creating new database connection...");
        String url = databaseConnectionProperties.getProperty("jdbc.url");
        String user = databaseConnectionProperties.getProperty("jdbc.user");
        String password = databaseConnectionProperties.getProperty("jdbc.password");
        logger.info("url {}", url);
        logger.info("user {}", user);
        logger.info("password {}", password);
        try {
            if (user != null && password != null)
                return DriverManager.getConnection(url, user, password);
            return DriverManager.getConnection(url);
        } catch (SQLException exception) {
            logger.error(exception);
        }
        logger.traceExit("Returned connection is null");
        return null;
    }
}

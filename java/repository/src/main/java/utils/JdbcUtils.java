package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Connection connection;
    private Properties databaseConnectionProperties;

    public JdbcUtils(Properties databaseConnectionProperties) {
        this.databaseConnectionProperties = databaseConnectionProperties;
    }

    public Connection getConnection() {
        if (connection == null)
            connection = createNewConnection();
        return connection;
    }

    private Connection createNewConnection() {
        String url = databaseConnectionProperties.getProperty("jdbc.url");
        String user = databaseConnectionProperties.getProperty("jdbc.user");
        String password = databaseConnectionProperties.getProperty("jdbc.password");
        try {
            if (user != null && password != null)
                return DriverManager.getConnection(url, user, password);
            return DriverManager.getConnection(url);
        } catch (SQLException exception) {

        }
        return null;
    }
}

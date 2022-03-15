package database;

import interfaces.UserRepository;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class UserDatabaseRepository implements UserRepository {
    private JdbcUtils utils;
    private Logger log = LogManager.getLogger();

    private static final String INSERT_USER_SQL_STRING = "insert into users(username, password) values(?, ?);";
    private static final String FIND_BY_USERNAME_SQL_STRING = "select user_id, username, password from users where username=?";

    public UserDatabaseRepository(Properties databaseConnectionProperties) {
        log.traceEntry("Creating user database repository...");
        utils = new JdbcUtils(databaseConnectionProperties);
        log.traceExit();
    }

    @Override
    public User save(User model) {
        log.traceEntry("Adding new user " + model);

        User addedUser = null;

        try (var connection = utils.getConnection();
            var insertPreparedStatement = connection.prepareStatement(INSERT_USER_SQL_STRING, Statement.RETURN_GENERATED_KEYS)){

            insertPreparedStatement.setString(1, model.getUserName());
            insertPreparedStatement.setString(2, model.getPassword());

            insertPreparedStatement.executeUpdate();

            try(var generatedKeys = insertPreparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    addedUser = new User(model);
                    addedUser.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException exception) {
            log.error(exception);
        }

        return addedUser;
    }

    @Override
    public User findUserByUserName(String username) {
        try(var connection = utils.getConnection();
            var findPreparedStatement = connection.prepareStatement(FIND_BY_USERNAME_SQL_STRING)) {

            findPreparedStatement.setString(1, username);

            try(var resultSet = findPreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var userId = resultSet.getLong(1);
                    var existingUsername = resultSet.getString(2);
                    var password = resultSet.getString(3);
                    User user = new User(existingUsername, password);
                    user.setId(userId);
                    return user;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public User findOne(Long modelID) {
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }
}

package com.rallies.dataaccess.impl.database;

import com.rallies.exceptions.NotImplementedMethodException;
import com.rallies.dataaccess.api.UserRepository;
import com.rallies.domain.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;


public class UserDatabaseRepository implements UserRepository {
    private JdbcUtils utils;
    private Logger log = LogManager.getLogger();

    private static final String INSERT_USER_SQL_STRING = "insert into users(username, password) values(?, ?);";
    private static final String FIND_BY_USERNAME_SQL_STRING = "select user_id, username, password from users where username=?";

    public UserDatabaseRepository(Properties databaseConnectionProperties) {
        log.traceEntry("Creating user com.rallies.dataaccess.impl.database com.rallies.dataaccess.repository...");
        utils = new JdbcUtils(databaseConnectionProperties);
        log.traceExit();
    }

    @Override
    public User save(User model) {
        log.traceEntry("Adding new user " + model);

        var existingUserOptional = getUserByUsername(model.getUserName());
        if (existingUserOptional.isPresent()) {
            log.info("User {} already exists", model.getUserName());
            log.traceExit();
            return existingUserOptional.get();
        }

        try (var connection = utils.getConnection();
             var insertPreparedStatement = connection.prepareStatement(INSERT_USER_SQL_STRING, Statement.RETURN_GENERATED_KEYS)) {

            insertPreparedStatement.setString(1, model.getUserName());
            insertPreparedStatement.setString(2, model.getPassword());

            insertPreparedStatement.executeUpdate();

            try (var generatedKeys = insertPreparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    User addedUser = new User(model);
                    addedUser.setId(generatedKeys.getLong(1));
                    log.info("Added user {}", addedUser.getUserName());
                    return addedUser;
                } else throw new DatabaseException("DatabaseException occurred while searching for the generated id for user" + model);

            }
        } catch (SQLException exception) {
            log.error(exception);
            throw new DatabaseException("DatabaseException occurred while saving user " + model);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        log.traceEntry("Searching for user with username {}", username);
        try (var connection = utils.getConnection();
             var findPreparedStatement = connection.prepareStatement(FIND_BY_USERNAME_SQL_STRING)) {

            findPreparedStatement.setString(1, username);

            try (var resultSet = findPreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var userId = resultSet.getLong(1);
                    var existingUsername = resultSet.getString(2);
                    var password = resultSet.getString(3);
                    User user = new User(existingUsername, password);
                    user.setId(userId);
                    log.info("Found user");
                    log.traceExit();
                    return Optional.of(user);
                } else {
                    log.info("User not found");
                    log.traceExit();
                    return Optional.empty();
                }
            }
        } catch (SQLException exception) {
            log.error(exception);
            throw new DatabaseException("DatabaseException occurred while searching for user with username " + username);
        }
    }

    @Override
    public User getById(Long modelID) {
        throw new NotImplementedMethodException();
    }

    @Override
    public Collection<User> getAll() {
        throw new NotImplementedMethodException();
    }
}

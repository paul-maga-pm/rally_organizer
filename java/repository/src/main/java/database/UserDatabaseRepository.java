package database;

import exceptions.DatabaseException;
import exceptions.NotImplementedMethodException;
import interfaces.UserRepository;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;


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
        Function<User, User> saveUserFunction = user -> {
            log.traceEntry("Adding new user " + model);

            var existingUserOptional = findUserByUserName(model.getUserName());
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
                    }
                }
            } catch (SQLException exception) {
                log.error(exception);
            }
            log.traceExit();
            return null;
        };

        var savedUser = saveUserFunction.apply(model);

        if (savedUser == null)
            throw new DatabaseException("DatabaseException occurred while saving user {} " +  model.getUserName());

        return savedUser;
    }

    @Override
    public Optional<User> findUserByUserName(String username) {
        Function<String, Optional<User>> findUserFunction = usrname -> {
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
            }
            log.traceExit();
            return null;
        };

        var foundUserOptional = findUserFunction.apply(username);

        if (foundUserOptional == null)
            throw new DatabaseException("DatabaseException occurred while searching for user with username " + username);

        return foundUserOptional;
    }

    @Override
    public User findOne(Long modelID) {
        throw new NotImplementedMethodException();
    }

    @Override
    public Iterable<User> findAll() {
        throw new NotImplementedMethodException();
    }
}

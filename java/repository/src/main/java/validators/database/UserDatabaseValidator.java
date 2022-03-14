package validators.database;

import models.User;
import utils.JdbcUtils;
import validators.interfaces.UserValidator;

import java.sql.SQLException;
import java.util.Properties;

public class UserDatabaseValidator implements UserValidator {
    private JdbcUtils utils;

    private static final String FIND_USER_BY_USERNAME_SQL_STRING = "select user_id from users where username=?";

    public UserDatabaseValidator(Properties databaseConnectionProperties) {
        utils = new JdbcUtils(databaseConnectionProperties);
    }


    @Override
    public void validate(User model) {
        String errors = "";

        if (isUsernameAlreadyUsed(model.getUserName())) {
            errors = "Username is already used!";
        } else {
            if (model.getUserName().equals(""))
                errors = "Username can't be empty!\n";

            if (model.getPassword().equals(""))
                errors += "Password can't be empty!\n";
        }

        if (!errors.equals(""))
            throw new InvalidModelException(errors);
    }

    private Boolean isUsernameAlreadyUsed(String username) {
        try(var connection = utils.getConnection();
            var findPreparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME_SQL_STRING)) {

            findPreparedStatement.setString(1, username);

            try(var resultSet = findPreparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}

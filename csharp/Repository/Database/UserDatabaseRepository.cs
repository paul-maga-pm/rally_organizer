using Domain.Models;
using log4net;
using Repository.Interfaces;
using Repository.Utils;
using Repository.Validators.Database;

namespace Repository.Database
{
    public class UserDatabaseRepository : IUserRepository
    {
        private DatabaseUtils databaseUtils;

        private UserDatabaseValidator validator;

        private ILog log = LogManager.GetLogger(typeof(UserDatabaseRepository));

        private static readonly String INSERT_USER_SQL_STRING = "insert into users(username, password) values(@username_param, @password_param);";
        private static readonly String FIND_BY_USERNAME_SQL_STRING = "select user_id, username, password from users where username=@username_param";

        public UserDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            log.Info("Creating user database repository...");
            databaseUtils = new DatabaseUtils(databaseConnectionProperties);
            validator = new UserDatabaseValidator(databaseConnectionProperties);
            log.Info("Created user dabase repository!");
        }

        public User Add(User model)
        {
            log.Info("Adding user " + model.ToString());
            validator.Validate(model);

            User addedUser = null;
            try
            {
                using (var connection = databaseUtils.GetConnection())
                using (var insertCommand = connection.CreateCommand())
                {
                    insertCommand.CommandText = INSERT_USER_SQL_STRING;

                    var usernameParam = insertCommand.CreateParameter();
                    usernameParam.ParameterName = "@username_param";
                    usernameParam.Value = model.UserName;

                    var passwordParam = insertCommand.CreateParameter();
                    passwordParam.ParameterName = "@password_param";
                    passwordParam.Value = model.Password;

                    insertCommand.Parameters.Add(usernameParam);
                    insertCommand.Parameters.Add(passwordParam);

                    insertCommand.ExecuteNonQuery();

                    addedUser = FindUserByUsername(model.UserName);
                }
            }
            catch(Exception ex)
            {
                log.Error(ex.ToString());
            }

            return addedUser;
        }
        public User FindUserByUsername(string username)
        {
            try
            {
                using (var connection = databaseUtils.GetConnection())
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = FIND_BY_USERNAME_SQL_STRING;

                    var usernameParam = command.CreateParameter();
                    usernameParam.ParameterName = "@username_param";
                    usernameParam.Value = username;

                    command.Parameters.Add(usernameParam);

                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            long userId = reader.GetInt64(0);
                            String foundUsername = reader.GetString(1);
                            String password = reader.GetString(2);
                            User user = new User(username, password);
                            user.Id = userId;
                            return user;
                        }
                        else
                            return null;
                    }
                }
            } 
            catch (Exception ex)
            {
                log.Error(ex.Message);
                return null;
            }
        }

        public ICollection<User> FindAll()
        {
            throw new NotImplementedException();
        }

        public User FindOne(long id)
        {
            throw new NotImplementedException();
        }

    }
}

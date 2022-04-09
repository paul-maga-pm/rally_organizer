using Domain.Models;
using log4net;
using Repository.Interfaces;
using Repository.Utils;
using System.Data.Common;

namespace Repository.Database
{
    public class UserDatabaseRepository : IUserRepository
    {
        private DatabaseUtils databaseUtils;


        private ILog log = LogManager.GetLogger(typeof(UserDatabaseRepository));

        private static readonly String INSERT_USER_SQL_STRING = "insert into users(username, password) values(@username_param, @password_param);";
        private static readonly String FIND_BY_USERNAME_SQL_STRING = "select user_id, username, password from users where username=@username_param";

        public UserDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            log.Info("Creating user database repository...");
            databaseUtils = new DatabaseUtils(databaseConnectionProperties);
            log.Info("Created user dabase repository!");
        }

        public User Add(User model)
        {
            log.Info("Adding user " + model.ToString());

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

                    User? addedUser = GetByUsername(model.UserName);
                    if (addedUser == null)
                        throw new DatabaseException("Couldn't add user " + addedUser);
                    return addedUser;
                }
            }
            catch(DbException ex)
            {
                log.Error(ex.ToString());
                throw new DatabaseException("Error occured while adding user " + model);
            }

        }
        public User? GetByUsername(string username)
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
            catch (DbException ex)
            {
                log.Error(ex.Message);
                throw new DatabaseException("Error occured when searching for user " + username);
            }
        }

        public ICollection<User> GetAll()
        {
            throw new NotImplementedException();
        }

        public User GetById(long id)
        {
            throw new NotImplementedException();
        }

    }
}

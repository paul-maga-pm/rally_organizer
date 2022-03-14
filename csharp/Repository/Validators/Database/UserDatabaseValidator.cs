using Domain.Models;
using Repository.Utils;
using Repository.Validators.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Validators.Database
{
    public class UserDatabaseValidator : IUserValidator
    {
        private DatabaseUtils utils;

        private static readonly String FIND_USER_BY_USERNAME_SQL_STRING = "select user_id from users where username=@username_param";
        public UserDatabaseValidator(IDictionary<String, String> databaseConnectionProperties)
        {
            utils = new DatabaseUtils(databaseConnectionProperties);
        }

        public void Validate(User model)
        {
            String errors = "";

            if (IsUsernameAlreadyUsed(model.UserName))
            {
                errors = "Username is already used!";
            }
            else
            {
                if (model.UserName.Equals(""))
                    errors += "Username can't be empty!\n";

                if (model.Password.Equals(""))
                    errors += "Password can't be empty!";
            }

            if (!errors.Equals(""))
                throw new IUserValidator.InvalidModelException(errors);
        }

        private Boolean IsUsernameAlreadyUsed(String userName)
        {
            using(var connection = utils.GetConnection())
            using(var command = connection.CreateCommand())
            {
                command.CommandText = FIND_USER_BY_USERNAME_SQL_STRING;

                var usernameParam = command.CreateParameter();
                usernameParam.ParameterName = "@username_param";
                usernameParam.Value = userName;

                command.Parameters.Add(usernameParam);

                using(var reader = command.ExecuteReader())
                {
                    return reader.Read();
                }
            }
        }
    }
}

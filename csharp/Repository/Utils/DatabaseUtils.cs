using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Utils
{
    public class DatabaseUtils
    {
        private IDbConnection connection;
        private IDictionary<String, String> databaseConnectionProperties;

        public DatabaseUtils(IDictionary<string, string> databaseConnectionProperties)
        {
            this.databaseConnectionProperties = databaseConnectionProperties;
        }

        public IDbConnection GetConnection()
        {
            if (connection == null || connection.State == System.Data.ConnectionState.Closed)
            {
                connection = CreateNewConnection();
                connection.Open();
            }
            return connection;
        }

        private IDbConnection? CreateNewConnection()
        {
            return DatabaseConnectionUtils.ConnectionFactory.GetInstance().CreateConnection(databaseConnectionProperties);
        }
    }
}

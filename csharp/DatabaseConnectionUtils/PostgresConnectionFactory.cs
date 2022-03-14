using Npgsql;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DatabaseConnectionUtils
{
    public class PostgresConnectionFactory : ConnectionFactory
    {
        public override IDbConnection CreateConnection(IDictionary<string, string> databaseProperties)
        {
            String connectionString = databaseProperties["ConnectionString"];
            return new NpgsqlConnection(connectionString);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace DatabaseConnectionFactories
{
    public abstract class ConnectionFactory
    {
        private static ConnectionFactory instance;

        protected ConnectionFactory()
        {

        }

        public static ConnectionFactory GetInstance()
        {
            if (instance == null)
            {
                Assembly assembly = Assembly.GetExecutingAssembly();
                Type[] types = assembly.GetTypes();
                foreach (var type in types)
                    if (type.IsSubclassOf(typeof(ConnectionFactory)))
                        instance = (ConnectionFactory)Activator.CreateInstance(type);
            }
            return instance;
        }

        public abstract IDbConnection CreateConnection(IDictionary<String, String> databaseProperties);
    }
}

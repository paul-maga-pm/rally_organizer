using DatabaseConnectionFactories;
using Domain.Models;
using Repository.Interfaces;
using Repository.Utils;
using Repository.Validators.Database.Postgresql;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Database
{
    public class RallyDatabaseRepository : IRallyRepository
    {
        private DatabaseUtils utils;
        private RallyDatabaseValidator validator;

        private static readonly String INSERT_RALLY_SQL_STRING = "insert into rallies(engine_capacity) values (@engine_capacity_param)";
        private static readonly String FIND_ALL_RALLIES_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies";
        private static readonly String FIND_BY_ENGINE_CAPACITY_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies where engine_capacity=@engine_capacity_param;";

        public RallyDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            this.utils = new DatabaseUtils(databaseConnectionProperties);
            validator = new RallyDatabaseValidator();
        }
        public Rally FindRallyByEngineCapacity(int engineCapacity)
        {
            Rally rally = null;
            using(var connection = utils.GetConnection())
            {
                using(var comamnd = connection.CreateCommand())
                {
                    comamnd.CommandText = FIND_BY_ENGINE_CAPACITY_SQL_STRING;

                    var engineCapacityParam = comamnd.CreateParameter();
                    engineCapacityParam.ParameterName = "@engine_capacity_param";
                    engineCapacityParam.Value = engineCapacity;

                    comamnd.Parameters.Add(engineCapacityParam);
                    using(var reader = comamnd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            rally = GetCurrentRallyFromReader(reader);
                        }
                    }
                }
            }
            return rally;
        }
        public Rally Add(Rally model)
        {
            validator.Validate(model);
            var existingRally = this.FindRallyByEngineCapacity(model.EngineCapacity);
            if (existingRally != null)
                return existingRally;

            using(var connection = utils.GetConnection())
            using(var command = connection.CreateCommand())
            {
                command.CommandText = INSERT_RALLY_SQL_STRING;

                var engineCapacityParameter = command.CreateParameter();
                engineCapacityParameter.ParameterName = "@engine_capacity_param";
                engineCapacityParameter.Value = model.EngineCapacity;
                command.Parameters.Add(engineCapacityParameter);
                command.ExecuteNonQuery();
                return FindRallyByEngineCapacity(model.EngineCapacity);
            }
        }

        public ICollection<Rally> FindAll()
        {
            ICollection<Rally> rallies = new List<Rally>();

            using(var connection = utils.GetConnection())
            {
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = FIND_ALL_RALLIES_SQL_STRING;
                    using (var reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            rallies.Add(GetCurrentRallyFromReader(reader));
                        }
                    }
                }
            }

            return rallies;
        }

        private Rally GetCurrentRallyFromReader(IDataReader reader)
        {
            long rallyId = reader.GetInt64(0);
            int exitingEngineCapacity = reader.GetInt32(1);
            int noOfParticipants = reader.GetInt32(2);
            Rally rally = new Rally(exitingEngineCapacity, noOfParticipants);
            rally.Id = rallyId;
            return rally;
        }



        public Rally FindOne(long rallyId)
        {
            throw new NotImplementedException();
        }

    }
}

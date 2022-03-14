using DatabaseConnectionFactories;
using Domain.Models;
using log4net;
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
        private static readonly ILog log = LogManager.GetLogger("DallyDatabaseRepository");

        private static readonly String INSERT_RALLY_SQL_STRING = "insert into rallies(engine_capacity) values (@engine_capacity_param)";
        private static readonly String FIND_ALL_RALLIES_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies";
        private static readonly String FIND_BY_ENGINE_CAPACITY_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies where engine_capacity=@engine_capacity_param;";

        public RallyDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            log.Info("Creating rally database repository...");
            this.utils = new DatabaseUtils(databaseConnectionProperties);
            validator = new RallyDatabaseValidator();
            log.Info("Rally database repository created!");
        }
        public Rally FindRallyByEngineCapacity(int engineCapacity)
        {
            log.Info("Searching for rally by engine capacity " + engineCapacity);
            Rally rally = null;

            try
            {
                using (var connection = utils.GetConnection())
                {
                    using (var comamnd = connection.CreateCommand())
                    {
                        comamnd.CommandText = FIND_BY_ENGINE_CAPACITY_SQL_STRING;

                        var engineCapacityParam = comamnd.CreateParameter();
                        engineCapacityParam.ParameterName = "@engine_capacity_param";
                        engineCapacityParam.Value = engineCapacity;

                        comamnd.Parameters.Add(engineCapacityParam);
                        using (var reader = comamnd.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                rally = GetCurrentRallyFromReader(reader);
                            }
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                log.Error(ex.Message, ex);
            }
            return rally;
        }
        public Rally Add(Rally model)
        {
            log.Info("Adding new rally " + model.ToString());
            validator.Validate(model);
            var existingRally = this.FindRallyByEngineCapacity(model.EngineCapacity);
            if (existingRally != null)
                return existingRally;

            Rally savedRally = null;
            try
            {
                using (var connection = utils.GetConnection())
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = INSERT_RALLY_SQL_STRING;

                    var engineCapacityParameter = command.CreateParameter();
                    engineCapacityParameter.ParameterName = "@engine_capacity_param";
                    engineCapacityParameter.Value = model.EngineCapacity;
                    command.Parameters.Add(engineCapacityParameter);
                    command.ExecuteNonQuery();
                    savedRally = FindRallyByEngineCapacity(model.EngineCapacity);
                }
            }
            catch(Exception ex)
            {
                log.Error(ex.Message, ex);
            }
            return savedRally;
        }

        public ICollection<Rally> FindAll()
        {
            log.Info("Returning all rallies...");
            ICollection<Rally> rallies = new List<Rally>();

            try
            {
                using (var connection = utils.GetConnection())
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
            }
            catch(Exception exception)
            {
                log.Error(exception.Message, exception);
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

using DatabaseConnectionFactories;
using Domain.Models;
using log4net;
using Repository.Interfaces;
using Repository.Utils;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Database
{
    public class RallyDatabaseRepository : IRallyRepository
    {
        private DatabaseUtils utils;
        private static readonly ILog log = LogManager.GetLogger("DallyDatabaseRepository");

        private static readonly String INSERT_RALLY_SQL_STRING = "insert into rallies(engine_capacity) values (@engine_capacity_param)";
        private static readonly String FIND_ALL_RALLIES_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies";
        private static readonly String FIND_BY_ENGINE_CAPACITY_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies where engine_capacity=@engine_capacity_param;";

        public RallyDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            log.Info("Creating rally database repository...");
            this.utils = new DatabaseUtils(databaseConnectionProperties);
            log.Info("Rally database repository created!");
        }
        public Rally? GetByEngineCapacity(int engineCapacity)
        {
            log.Info("Searching for rally by engine capacity " + engineCapacity);

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
                                return GetCurrentRallyFromReader(reader);
                            }
                            return null;
                        }
                    }
                }
            }
            catch(DbException ex)
            {
                log.Error(ex.Message, ex);
                throw new DatabaseException("Error occured while searching for rally with engine capacity " + engineCapacity);
            }
        }
        public Rally Add(Rally model)
        {
            log.Info("Adding new rally " + model.ToString());
            var existingRally = this.GetByEngineCapacity(model.EngineCapacity);
            if (existingRally != null)
                return existingRally;

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
                    Rally? savedRally = GetByEngineCapacity(model.EngineCapacity);

                    if (savedRally == null)
                        throw new DatabaseException("Error occured while saving rally " + model);
                    return savedRally;
                }
            }
            catch(DbException ex)
            {
                log.Error(ex.Message, ex);
                throw new DatabaseException("Error occured while saving rally " + model);
            }
        }

        public ICollection<Rally> GetAll()
        {
            log.Info("Returning all rallies...");

            try
            {
                using (var connection = utils.GetConnection())
                {
                    using (var command = connection.CreateCommand())
                    {
                        command.CommandText = FIND_ALL_RALLIES_SQL_STRING;
                        using (var reader = command.ExecuteReader())
                        {
                            ICollection<Rally> rallies = new List<Rally>();
                            while (reader.Read())
                            {
                                rallies.Add(GetCurrentRallyFromReader(reader));
                            }
                            return rallies; 
                        }
                    }
                }
            }
            catch(DbException exception)
            {
                log.Error(exception.Message, exception);
                throw new DatabaseException("Error occured while searching for all rallies");
            }

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



        public Rally GetById(long rallyId)
        {
            throw new NotImplementedException();
        }

    }
}

using Domain.Models;
using log4net;
using Repository.Interfaces;
using Repository.Utils;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Database
{
    public class TeamDatabaseRepository : ITeamRepository
    {
        private DatabaseUtils databaseUtils;
        private static readonly ILog log = LogManager.GetLogger("TeamDatabaseRepository");

        private static readonly String INSERT_TEAM_SQL_STRING = "insert into teams(team_name) values (@team_name_param);";
        private static readonly String FIND_TEAM_BY_NAME_SQL_STRING = "select team_id, team_name from teams where team_name=@team_name_param;";


        public TeamDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            log.Info("Creating team database repository...");
            databaseUtils = new DatabaseUtils(databaseConnectionProperties);
            log.Info("Team database repository created!");
        }
        public Team? GetByName(string teamName)
        {
            log.Info("Searching for team by name " + teamName);

            try
            {
                using (var connection = databaseUtils.GetConnection())
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = FIND_TEAM_BY_NAME_SQL_STRING;

                    var teamNameParam = command.CreateParameter();
                    teamNameParam.Value = teamName;
                    teamNameParam.ParameterName = "@team_name_param";

                    command.Parameters.Add(teamNameParam);

                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            long teamId = reader.GetInt64(0);
                            String existingName = reader.GetString(1);
                            Team foundTeam = new Team(existingName);
                            foundTeam.Id = teamId;
                            return foundTeam;
                        }
                        else return null;
                    }
                }
            }
            catch (DbException ex)
            {
                log.Error(ex.Message, ex);
                throw new DatabaseException("Error occured while searching for team with name " + teamName);
            }
        }

        public Team Add(Team model)
        {
            log.Info("Adding new team " + model.ToString());
            
            var existingTeam = GetByName(model.TeamName);
            if (existingTeam != null)
                return existingTeam;

            try
            {
                using (var connection = databaseUtils.GetConnection())
                using (var command = connection.CreateCommand())
                {
                    command.CommandText = INSERT_TEAM_SQL_STRING;

                    var teamNameParam = command.CreateParameter();
                    teamNameParam.ParameterName = "@team_name_param";
                    teamNameParam.Value = model.TeamName;
                    command.Parameters.Add(teamNameParam);
                    command.ExecuteNonQuery();

                    var addedTeam = GetByName(model.TeamName);
                    if (addedTeam == null)
                        throw new DatabaseException("Error occured while adding team " + model);
                    return addedTeam;
                }
            }
            catch(DbException ex)
            {
                log.Error(ex.Message, ex);
                throw new DatabaseException("Error occured while adding team " + model);
            }
        }

        public ICollection<Team> GetAll()
        {
            try
            {
                using(var connection = databaseUtils.GetConnection())
                using(var command = connection.CreateCommand())
                {
                    command.CommandText = "select team_id, team_name from teams";
                    

                    using(var reader = command.ExecuteReader())
                    {
                        var allTeams = new List<Team>();
                        while(reader.Read())
                        {
                            long teamId = reader.GetInt64(0);
                            String teamName = reader.GetString(1);
                            Team team = new Team(teamName);
                            team.Id = teamId;
                            allTeams.Add(team);
                        }
                        return allTeams;
                    }
                }   
            }
            catch(DbException ex)
            {
                log.Error(ex);
                throw new DatabaseException("Error occured while searching for all teams");
            }
        }

        public Team GetById(long id)
        {
            throw new NotImplementedException();
        }

    }
}

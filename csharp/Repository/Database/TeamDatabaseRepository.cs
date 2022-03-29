using Domain.Models;
using log4net;
using Repository.Interfaces;
using Repository.Utils;
using System;
using System.Collections.Generic;
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
        public Team FindTeamByName(string teamName)
        {
            log.Info("Searching for team by name " + teamName);

            Team foundTeam = null;

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
                            foundTeam = new Team(existingName);
                            foundTeam.Id = teamId;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                log.Error(ex.Message, ex);
            }
            return foundTeam;
        }

        public Team Add(Team model)
        {
            log.Info("Adding new team " + model.ToString());
            
            var existingTeam = FindTeamByName(model.TeamName);
            if (existingTeam != null)
                return existingTeam;

            Team addedTeam = null;

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

                    addedTeam = FindTeamByName(model.TeamName);
                }
            }
            catch(Exception ex)
            {
                log.Error(ex.Message, ex);
            }
            return addedTeam;
        }

        public ICollection<Team> FindAll()
        {
            try
            {
                using(var connection = databaseUtils.GetConnection())
                using(var command = connection.CreateCommand())
                {
                    command.CommandText = "select team_id, team_name from teams";
                    
                    var allTeams = new List<Team>();

                    using(var reader = command.ExecuteReader())
                    {
                        while(reader.Read())
                        {
                            long teamId = reader.GetInt64(0);
                            String teamName = reader.GetString(1);
                            Team team = new Team(teamName);
                            team.Id = teamId;
                            allTeams.Add(team);
                        }
                    }
                    return allTeams;
                }   
            }
            catch(Exception ex)
            {
                log.Error(ex);
            }
            return null;
        }

        public Team FindOne(long id)
        {
            throw new NotImplementedException();
        }

    }
}

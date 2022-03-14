using Domain.Models;
using Repository.Interfaces;
using Repository.Utils;
using Repository.Validators.Database.Postgresql;
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
        private TeamDatabaseValidator validator;

        private static readonly String INSERT_TEAM_SQL_STRING = "insert into teams(team_name) values (@team_name_param);";
        private static readonly String FIND_TEAM_BY_NAME_SQL_STRING = "select team_id, team_name from teams where team_name=@team_name_param;";


        public TeamDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            databaseUtils = new DatabaseUtils(databaseConnectionProperties);
            validator = new TeamDatabaseValidator();
        }
        public Team FindTeamByName(string teamName)
        {
            using(var connection = databaseUtils.GetConnection())
            using(var command = connection.CreateCommand())
            {
                command.CommandText = FIND_TEAM_BY_NAME_SQL_STRING;

                var teamNameParam = command.CreateParameter();
                teamNameParam.Value = teamName;
                teamNameParam.ParameterName = "@team_name_param";

                command.Parameters.Add(teamNameParam);

                using(var reader = command.ExecuteReader())
                {
                    if (reader.Read())
                    {
                        long teamId = reader.GetInt64(0);
                        String existingName = reader.GetString(1);
                        Team team = new Team(existingName);
                        team.Id = teamId;
                        return team;
                    }
                }
            }
            return null;
        }

        public Team Add(Team model)
        {
            validator.Validate(model);
            
            var existingTeam = FindTeamByName(model.TeamName);
            if (existingTeam != null)
                return existingTeam;

            using(var connection = databaseUtils.GetConnection())
            using(var command = connection.CreateCommand())
            {
                command.CommandText = INSERT_TEAM_SQL_STRING;

                var teamNameParam = command.CreateParameter();
                teamNameParam.ParameterName = "@team_name_param";
                teamNameParam.Value = model.TeamName;
                command.Parameters.Add(teamNameParam);
                command.ExecuteNonQuery();

                return FindTeamByName(model.TeamName);
            }
        }

        public ICollection<Team> FindAll()
        {
            throw new NotImplementedException();
        }

        public Team FindOne(long id)
        {
            throw new NotImplementedException();
        }

    }
}

using DatabaseConnectionUtils;
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
    public class ParticipantDatabaseRepository : IParticipantRepository
    {
        private ConnectionFactory connectionFactory;
        private ParticipantDatabaseValidator validator;
        private DatabaseUtils databaseUtils;

        private static readonly String INSERT_PARTICIPANT_SQL_STRING =
            "insert into participants(participant_name, team_id, rally_id) " +
            "values (@participant_name_param, @team_id_param, @rally_id_param);";

        private static readonly String INCREMENT_NUMBER_OF_PARTICIPANTS_FOR_RALLY_SQL_STRING =
                "update rallies set number_of_participants=number_of_participants+1 " +
                "where rally_id=@rally_id_param;";

        private static readonly String FIND_PARTICIPANTS_BY_TEAM_NAME_SQL_STRING =
                "select p.participant_id, p.participant_name, t.team_id, t.team_name, r.rally_id, r.engine_capacity, r.number_of_participants " +
                "from participants p " +
                "inner join teams t on t.team_id=p.team_id " +
                "inner join rallies r on r.rally_id=p.rally_id " +
                "where t.team_name=@team_name_param;";

        private static readonly String FIND_PARTICIPANT_BY_NAME_SQL_STRING =
                "select p.participant_id, p.participant_name, t.team_id, t.team_name, r.rally_id, r.engine_capacity, r.number_of_participants " +
                "from participants p " +
                "inner join teams t on t.team_id=p.team_id " +
                "inner join rallies r on r.rally_id=p.rally_id " +
                "where p.participant_name=@participant_name_param;";

        public ParticipantDatabaseRepository(IDictionary<String, String> databaseConnectionProperties)
        {
            this.databaseUtils = new DatabaseUtils(databaseConnectionProperties);
            validator = new ParticipantDatabaseValidator(databaseConnectionProperties);
            connectionFactory = ConnectionFactory.GetInstance();
        }

        public Participant Add(Participant model)
        {
            validator.Validate(model);

            var existingParticipant = FindParticipantByName(model.Name);
            if (existingParticipant != null)
                return existingParticipant;

            using(var connection = databaseUtils.GetConnection())
            {
                using (var insertParticipantCommand = connection.CreateCommand())
                using (var updateParticipantsNoCommand = connection.CreateCommand())
                {
                    var participantNameParam = insertParticipantCommand.CreateParameter();
                    participantNameParam.ParameterName = "@participant_name_param";
                    participantNameParam.Value = model.Name;

                    var teamIdParam = insertParticipantCommand.CreateParameter();
                    teamIdParam.ParameterName = "@team_id_param";
                    teamIdParam.Value = model.Team.Id;

                    var rallyIdParam = insertParticipantCommand.CreateParameter();
                    rallyIdParam.ParameterName = "@rally_id_param";
                    rallyIdParam.Value = model.Rally.Id;

                    insertParticipantCommand.Parameters.Add(participantNameParam);
                    insertParticipantCommand.Parameters.Add(teamIdParam);
                    insertParticipantCommand.Parameters.Add(rallyIdParam);

                    updateParticipantsNoCommand.Parameters.Add(rallyIdParam);

                    var transaction = connection.BeginTransaction();
                    try
                    {
                        insertParticipantCommand.ExecuteNonQuery();
                        updateParticipantsNoCommand.ExecuteNonQuery();

                        return FindParticipantByName(model.Name);
                    }
                    catch (Exception ex)
                    {
                        transaction.Rollback();
                    }
                }
            }
            return null;
        }

        public ICollection<Participant> FindMembersOfTeam(string teamName)
        {
            ICollection<Participant> participants = new List<Participant>();
            using(var connection = databaseUtils.GetConnection())
            {
                using(var command = connection.CreateCommand())
                {
                    var teamNameParam = command.CreateParameter();
                    teamNameParam.ParameterName = "@team_name_param";
                    teamNameParam.Value = teamName;

                    command.Parameters.Add(teamNameParam);
                    command.CommandText = FIND_PARTICIPANTS_BY_TEAM_NAME_SQL_STRING;

                    using(var reader = command.ExecuteReader())
                    {
                        while(reader.Read())
                            participants.Add(GetCurrentParticipantFromReader(reader));
                    }
                }
            }
            return participants;
        }
        public Participant FindParticipantByName(string participantName)
        {
            Participant existingParticipant = null;

            using (var connection = databaseUtils.GetConnection())
            {
                using (var command = connection.CreateCommand())
                {
                    var participantNameParam = command.CreateParameter();
                    participantNameParam.ParameterName = "@participant_name_param";
                    participantNameParam.Value = participantName;

                    command.Parameters.Add(participantNameParam);

                    command.CommandText = FIND_PARTICIPANT_BY_NAME_SQL_STRING;

                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                            existingParticipant = GetCurrentParticipantFromReader(reader);
                    }
                }
            }

            return existingParticipant;
        }


        public IEnumerable<Participant> FindAll()
        {
            throw new NotImplementedException();
        }


        public Participant FindOne(Participant model)
        {
            throw new NotImplementedException();
        }

        private Participant GetCurrentParticipantFromReader(System.Data.IDataReader reader)
        {
            long teamId = reader.GetInt64(2);
            String teamName = reader.GetString(3);
            Team team = new Team(teamName);
            team.Id = teamId;

            long rallyId = reader.GetInt64(4);
            int engineCapacity = reader.GetInt32(5);
            int numberOfParticipants = reader.GetInt32(6);
            Rally rally = new Rally(engineCapacity, numberOfParticipants);
            rally.Id = rallyId;

            long participantId = reader.GetInt64(0);
            String participantName = reader.GetString(1);

            Participant participant = new Participant(rally, team, participantName);
            participant.Id = participantId;

            return participant;
        }
    }
}

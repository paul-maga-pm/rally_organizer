using DatabaseConnectionFactories;
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
    public class ParticipantDatabaseRepository : IParticipantRepository
    {
        private DatabaseUtils databaseUtils;

        private static readonly ILog log = LogManager.GetLogger("ParticipantDatabaseRepository");

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
            log.Info("Creating repository...");
            this.databaseUtils = new DatabaseUtils(databaseConnectionProperties);
            log.Info("Repository created!");
        }

        public Participant Add(Participant model)
        {
            log.Info("Adding participant: " + model.ToString());

            var existingParticipant = GetByName(model.Name);
            if (existingParticipant != null)
                return existingParticipant;

            try
            {
                using (var connection = databaseUtils.GetConnection())
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

                        var rallyUpdateIdParam = updateParticipantsNoCommand.CreateParameter();
                        rallyUpdateIdParam.ParameterName = "@rally_id_param";
                        rallyUpdateIdParam.Value = model.Rally.Id;

                        updateParticipantsNoCommand.Parameters.Add(rallyUpdateIdParam);

                        insertParticipantCommand.CommandText = INSERT_PARTICIPANT_SQL_STRING;
                        updateParticipantsNoCommand.CommandText = INCREMENT_NUMBER_OF_PARTICIPANTS_FOR_RALLY_SQL_STRING;
                        var transaction = connection.BeginTransaction();
                        try
                        {
                            insertParticipantCommand.ExecuteNonQuery();
                            updateParticipantsNoCommand.ExecuteNonQuery();
                            transaction.Commit();
                            var addedParticipant = GetByName(model.Name);
                            if (addedParticipant == null)
                                throw new DatabaseException("Could't find after adding participant " + model);
                            return addedParticipant;
                        }
                        catch (DbException ex)
                        {
                            transaction.Rollback();
                            log.Error(ex.Message);
                            throw new DatabaseException();
                        }
                    }
                }
            }
            catch(DbException ex)
            {
                log.Error(ex.Message, ex);
                throw new DatabaseException();
            }
        }

        public ICollection<Participant> GetMembersOfTeam(string teamName)
        {
            log.Info("Searching for members of team " + teamName);
            try
            {
                using (var connection = databaseUtils.GetConnection())
                {
                    using (var command = connection.CreateCommand())
                    {
                        var teamNameParam = command.CreateParameter();
                        teamNameParam.ParameterName = "@team_name_param";
                        teamNameParam.Value = teamName;

                        command.Parameters.Add(teamNameParam);
                        command.CommandText = FIND_PARTICIPANTS_BY_TEAM_NAME_SQL_STRING;

                        ICollection<Participant> participants = new List<Participant>();
                        using (var reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                                participants.Add(GetCurrentParticipantFromReader(reader));
                        }
                        return participants;
                    }
                }
            }
            catch(DbException exception)
            {
                log.Error(exception.Message, exception);
                throw new DatabaseException("Error occured while searching for members of team " + teamName);
            }

        }
        public Participant? GetByName(string participantName)
        {
            log.Info("Searching for participant by name " + participantName);

            try
            {
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
                                return GetCurrentParticipantFromReader(reader);
                            return null;
                        }
                    }
                }
            }
            catch (DbException exception)
            {
                log.Error(exception.Message, exception);
                throw new DatabaseException("Error occured while searching for participant by name" + participantName);
            }
        }


        public ICollection<Participant> GetAll()
        {
            throw new NotImplementedException();
        }


        public Participant? GetById(long participantId)
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

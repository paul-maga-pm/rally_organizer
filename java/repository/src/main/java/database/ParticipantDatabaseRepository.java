package database;

import interfaces.ParticipantRepository;
import interfaces.Repository;
import models.Participant;
import models.Rally;
import models.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;
import validators.database.ParticipantDatabaseValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDatabaseRepository implements ParticipantRepository {
    private JdbcUtils jdbcUtils;
    private ParticipantDatabaseValidator participantValidator;

    private Logger log = LogManager.getLogger();

    private static final String INSERT_PARTICIPANT_SQL_STRING =
            "insert into participants(participant_name, team_id, rally_id) values (?,?,?)";

    private static final String INCREMENT_NUMBER_OF_PARTICIPANTS_FOR_RALLY_SQL_STRING =
            "update rallies set number_of_participants=number_of_participants+1 where rally_id=?";

    private static final String FIND_PARTICIPANTS_BY_TEAM_NAME_SQL_STRING =
            "select p.participant_id, p.participant_name, t.team_id, t.team_name, r.rally_id, r.engine_capacity, r.number_of_participants " +
            "from participants p " +
            "inner join teams t on t.team_id=p.team_id " +
            "inner join rallies r on r.rally_id=p.rally_id " +
            "where t.team_name=?";

    private static final String FIND_PARTICIPANT_BY_NAME_SQL_STRING =
            "select p.participant_id, p.participant_name, t.team_id, t.team_name, r.rally_id, r.engine_capacity, r.number_of_participants " +
            "from participants p " +
            "inner join teams t on t.team_id=p.team_id " +
            "inner join rallies r on r.rally_id=p.rally_id " +
            "where p.participant_name=?";

    public ParticipantDatabaseRepository(Properties databaseConnectionProperties) {
        log.traceEntry("Creating participant database repository...");
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        this.participantValidator = new ParticipantDatabaseValidator(databaseConnectionProperties);
        log.traceEntry("Participant database repository created");
    }

    @Override
    public Participant save(Participant model) {
        log.traceEntry("Saving participant {}");
        participantValidator.validate(model);

        var existingParticipant = findParticipantByName(model.getParticipantName());
        if (existingParticipant != null) {
            log.traceExit("Participant with equal name already exists: {}", existingParticipant);
            return existingParticipant;
        }

        Connection connection = jdbcUtils.getConnection();
        try(PreparedStatement insertParticipantPreparedStatement =
                    connection.prepareStatement(INSERT_PARTICIPANT_SQL_STRING, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement incrementNumberOfParticipantsForRallyPreparedStatement =
                    connection.prepareStatement(INCREMENT_NUMBER_OF_PARTICIPANTS_FOR_RALLY_SQL_STRING)) {

            log.info("Starting inserting participant transaction...");
            connection.setAutoCommit(false);

            insertParticipantPreparedStatement.setString(1, model.getParticipantName());
            insertParticipantPreparedStatement.setLong(2,model.getTeam().getId());
            insertParticipantPreparedStatement.setLong(3, model.getRally().getId());
            incrementNumberOfParticipantsForRallyPreparedStatement.setLong(1, model.getRally().getId());

            insertParticipantPreparedStatement.executeUpdate();
            incrementNumberOfParticipantsForRallyPreparedStatement.executeUpdate();

            connection.commit();

            log.info("Transaction has been committed!");
            long generatedId;
            try(ResultSet generatedKeys = insertParticipantPreparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                    Participant participantWithId = new Participant(model);
                    participantWithId.setId(generatedId);
                    log.traceExit("Participant {} has been created", participantWithId);
                    return participantWithId;
                }
            } catch (SQLException exception) {
                log.error(exception);
            }

        } catch (SQLException exception) {
            try {
                if (!connection.isClosed()) {
                    log.error("Transaction failed!");
                    log.error(exception);
                    log.info("Attempting rollback...");
                    connection.rollback();
                    log.info("Rollback successful!");
                }
            } catch (SQLException e) {
                log.error("Error while attempting rollback!");
                log.error(e);
            }
        } finally {
                try {
                    if (connection != null) {
                        connection.close();
                        log.info("Closing connection in save participant...");
                    }
                } catch (SQLException e) {
                    log.error("Error while closing connection!");
                    log.error(e);
                }
        }
        log.traceExit("Null participant has been returned!");
        return null;
    }

    @Override
    public Iterable<Participant> findMembersOfTeam(String teamName) {
        log.traceEntry("Searching for participants in team {}", teamName);
        List<Participant> participantsInTeam = new ArrayList<>();

        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findByTeamNamePreparedStatement = connection.prepareStatement(FIND_PARTICIPANTS_BY_TEAM_NAME_SQL_STRING)) {
            findByTeamNamePreparedStatement.setString(1, teamName);

            try(ResultSet resultSet = findByTeamNamePreparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Participant participant = getCurrentParticipantFromResultSet(resultSet);
                    participantsInTeam.add(participant);
                }
            }
        } catch (SQLException e) {
            log.error(e);
        }
        log.traceExit("Participants found");
        return participantsInTeam;
    }

    @Override
    public Participant findParticipantByName(String participantName) {
        log.traceEntry("Searching for participant with name {}", participantName);
        Participant existingParticipant = null;
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findByParticipantNamePreparedStatement = connection.prepareStatement(FIND_PARTICIPANT_BY_NAME_SQL_STRING)) {
            findByParticipantNamePreparedStatement.setString(1, participantName);
            try(ResultSet resultSet = findByParticipantNamePreparedStatement.executeQuery()) {
                if (resultSet.next())
                    existingParticipant = getCurrentParticipantFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            log.error(exception);
        }
        log.traceExit("Returning participant {}", existingParticipant);
        return existingParticipant;
    }

    @Override
    public Participant findOne(Long modelID) {
        throw new Repository.NotImplementedRepositoryMethodException();
    }

    @Override
    public Iterable<Participant> findAll() {
        throw new Repository.NotImplementedRepositoryMethodException();
    }


    private Participant getCurrentParticipantFromResultSet(ResultSet resultSet) throws SQLException {
        log.traceEntry("Extracting current participant from result set");
        long participantId = resultSet.getLong("participant_id");
        String participantName = resultSet.getString("participant_name");

        long teamId = resultSet.getLong("team_id");
        String foundTeamName = resultSet.getString("team_name");

        long rallyId = resultSet.getLong("rally_id");
        int engineCapacity = resultSet.getInt("engine_capacity");
        int numberOfParticipants = resultSet.getInt("number_of_participants");

        Rally rally = new Rally(engineCapacity, numberOfParticipants);
        rally.setId(rallyId);

        Team team = new Team(foundTeamName);
        team.setId(teamId);

        Participant participant = new Participant(team, rally, participantName);
        participant.setId(participantId);
        log.traceExit("Participant {} has been returned", participant);
        return participant;
    }
}

package com.rallies.dataaccess.impl.database;

import com.rallies.exceptions.NotImplementedMethodException;
import com.rallies.dataaccess.api.ParticipantRepository;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class ParticipantDatabaseRepository implements ParticipantRepository {
    private JdbcUtils jdbcUtils;

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
        log.traceEntry("Creating participant com.rallies.dataaccess.impl.database com.rallies.dataaccess.repository...");
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        log.traceEntry("Participant com.rallies.dataaccess.impl.database com.rallies.dataaccess.repository created");
    }

    @Override
    public Participant save(Participant participant) {
        log.traceEntry("Saving participant {}");

        var existingParticipantOptional = getParticipantByName(participant.getParticipantName());
        if (existingParticipantOptional.isPresent()) {
            var existingParticipant = existingParticipantOptional.get();
            log.traceExit("Participant with equal name already exists: {}", existingParticipant);
            return existingParticipant;
        }

        try(Connection connection = jdbcUtils.getConnection()) {
            try(PreparedStatement insertParticipantPreparedStatement =
                        connection.prepareStatement(INSERT_PARTICIPANT_SQL_STRING, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement incrementNumberOfParticipantsForRallyPreparedStatement =
                        connection.prepareStatement(INCREMENT_NUMBER_OF_PARTICIPANTS_FOR_RALLY_SQL_STRING)) {

                log.info("Starting inserting participant transaction...");
                connection.setAutoCommit(false);

                insertParticipantPreparedStatement.setString(1, participant.getParticipantName());
                insertParticipantPreparedStatement.setLong(2,participant.getTeam().getId());
                insertParticipantPreparedStatement.setLong(3, participant.getRally().getId());
                incrementNumberOfParticipantsForRallyPreparedStatement.setLong(1, participant.getRally().getId());

                insertParticipantPreparedStatement.executeUpdate();
                incrementNumberOfParticipantsForRallyPreparedStatement.executeUpdate();

                connection.commit();

                log.info("Transaction has been committed!");
                var addedParticipant = getParticipantByName(participant.getParticipantName());

                if (addedParticipant.isEmpty())
                    throw new SQLException();

                return addedParticipant.get();
            } catch (SQLException exception) {
                log.error(exception);
                connection.rollback();
                throw new DatabaseException("DatabaseException occurred while saving participant " + participant);
            }
        } catch (SQLException exception) {
            log.error(exception);
            throw new DatabaseException("DatabaseException occurred while saving participant " + participant);
        }
    }

    @Override
    public Collection<Participant> getMembersOfTeam(String teamName) {
        log.traceEntry("Searching for participants in team {}", teamName);
        List<Participant> participantsInTeam = new ArrayList<>();

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement findByTeamNamePreparedStatement = connection.prepareStatement(FIND_PARTICIPANTS_BY_TEAM_NAME_SQL_STRING)) {
            findByTeamNamePreparedStatement.setString(1, teamName);

            try (ResultSet resultSet = findByTeamNamePreparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Participant participant = getCurrentParticipantFromResultSet(resultSet);
                    participantsInTeam.add(participant);
                }
                log.info("Returning found members...");
                log.traceExit();
                return participantsInTeam;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseException("DatabaseException occurred while searching for members of team " + teamName);
        }

    }

    @Override
    public Optional<Participant> getParticipantByName(String participantName) {
        log.traceEntry("Searching for participant with name {}", participantName);

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement findByParticipantNamePreparedStatement = connection.prepareStatement(FIND_PARTICIPANT_BY_NAME_SQL_STRING)) {
            findByParticipantNamePreparedStatement.setString(1, participantName);
            try (ResultSet resultSet = findByParticipantNamePreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var participant = getCurrentParticipantFromResultSet(resultSet);
                    log.info("Returning participant {}", participant);
                    log.traceExit();
                    return Optional.of(participant);
                } else
                    return Optional.empty();
            }
        } catch (SQLException exception) {
            log.error(exception);
            throw new DatabaseException("DatabaseException occurred while searching for participant with name " + participantName);
        }
    }

    @Override
    public Participant getById(Long modelID) {
        throw new NotImplementedMethodException();
    }

    @Override
    public Collection<Participant> getAll() {
        throw new NotImplementedMethodException();
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

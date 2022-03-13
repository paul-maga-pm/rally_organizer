package database;

import interfaces.ParticipantRepository;
import interfaces.Repository;
import models.Participant;
import models.Rally;
import models.Team;
import utils.JdbcUtils;
import validators.database.ParticipantDatabaseValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDatabaseRepository implements ParticipantRepository {
    private JdbcUtils jdbcUtils;
    private ParticipantDatabaseValidator participantValidator;

    private PreparedStatement insertParticipantPreparedStatement;
    private PreparedStatement incrementNumberOfParticipantsPreparedStatement;
    private PreparedStatement findByTeamNamePreparedStatement;
    private PreparedStatement findByParticipantNamePreparedStatement;

    public ParticipantDatabaseRepository(Properties databaseConnectionProperties) {
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        this.participantValidator = new ParticipantDatabaseValidator(jdbcUtils);
        initializePreparedStatements();
    }

    @Override
    public Participant save(Participant model) {
        participantValidator.validate(model);

        var existingParticipant = findByParticipantName(model.getParticipantName());
        if (existingParticipant != null)
            return existingParticipant;

        try {
            jdbcUtils.getConnection().setAutoCommit(false);

            insertParticipantPreparedStatement.setString(1, model.getParticipantName());
            insertParticipantPreparedStatement.setLong(2,model.getTeam().getId());
            insertParticipantPreparedStatement.setLong(3, model.getRally().getId());
            incrementNumberOfParticipantsPreparedStatement.setLong(1, model.getRally().getId());

            insertParticipantPreparedStatement.executeUpdate();
            incrementNumberOfParticipantsPreparedStatement.executeUpdate();

            jdbcUtils.getConnection().commit();

            long generatedId;
            try(ResultSet generatedKeys = insertParticipantPreparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                    Participant participantWithId = new Participant(model);
                    participantWithId.setId(generatedId);
                    return participantWithId;
                }
            }
        } catch (SQLException exception) {
            try {
                jdbcUtils.getConnection().rollback();
                exception.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Iterable<Participant> findByTeamName(String teamName) {
        List<Participant> participantsInTeam = new ArrayList<>();

        try {
            findByTeamNamePreparedStatement.setString(1, teamName);

            try(ResultSet resultSet = findByTeamNamePreparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Participant participant = getCurrentParticipantFromResultSet(resultSet);
                    participantsInTeam.add(participant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error code:" + e.getErrorCode());
        }

        return participantsInTeam;
    }

    private Participant getCurrentParticipantFromResultSet(ResultSet resultSet) throws SQLException {
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
        return participant;
    }

    @Override
    public Participant findByParticipantName(String participantName) {
        Participant existingParticipant = null;
        try {
            findByParticipantNamePreparedStatement.setString(1, participantName);

            try(ResultSet resultSet = findByParticipantNamePreparedStatement.executeQuery()) {
                if (resultSet.next())
                    existingParticipant = getCurrentParticipantFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
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

    private void initializePreparedStatements() {
        String insertParticipantSqlString = "insert into participants(participant_name, team_id, rally_id) values (?,?,?)";
        String incrementNumberOfParticipantsSqlString = "update rallies set number_of_participants=number_of_participants+1 where rally_id=?";

        String findByTeamNameSqlString = "select p.participant_id, p.participant_name, t.team_id, t.team_name, r.rally_id, r.engine_capacity, r.number_of_participants " +
                "from participants p " +
                "inner join teams t on t.team_id=p.team_id " +
                "inner join rallies r on r.rally_id=p.rally_id " +
                "where t.team_name=?";

        String findByParticipantNameSqlString = "select p.participant_id, p.participant_name, t.team_id, t.team_name, r.rally_id, r.engine_capacity, r.number_of_participants " +
                "from participants p " +
                "inner join teams t on t.team_id=p.team_id " +
                "inner join rallies r on r.rally_id=p.rally_id " +
                "where p.participant_name=?";

        try {
            insertParticipantPreparedStatement = jdbcUtils.getConnection().prepareStatement(insertParticipantSqlString, Statement.RETURN_GENERATED_KEYS);
            incrementNumberOfParticipantsPreparedStatement = jdbcUtils.getConnection().prepareStatement(incrementNumberOfParticipantsSqlString);
            findByTeamNamePreparedStatement = jdbcUtils.getConnection().prepareStatement(findByTeamNameSqlString);
            findByParticipantNamePreparedStatement = jdbcUtils.getConnection().prepareStatement(findByParticipantNameSqlString);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}

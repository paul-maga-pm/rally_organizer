package database;

import interfaces.Repository;
import interfaces.TeamRepository;
import models.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;
import validators.database.TeamDatabaseValidator;

import java.sql.*;
import java.util.Properties;

public class TeamDatabaseRepository implements TeamRepository {
    private JdbcUtils jdbcUtils;
    private TeamDatabaseValidator teamValidator = new TeamDatabaseValidator();

    private Logger log = LogManager.getLogger();

    private static final String INSERT_TEAM_SQL_STRING = "insert into teams(team_name) values (?)";
    private static final String FIND_TEAM_BY_NAME_SQL_STRING = "select team_id, team_name from teams where team_name=?";


    public TeamDatabaseRepository(Properties databaseConnectionProperties) {
        log.traceEntry("Creating team database repository...");
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        log.traceEntry("Team database repository created");

    }

    @Override
    public Team save(Team model) {
        log.traceEntry("Saving team {}", model);
        teamValidator.validate(model);

        var existingTeam = findByTeamName(model.getTeamName());
        if (existingTeam != null) {
            log.traceExit("Team with same name already exists: {}", existingTeam);
            return existingTeam;
        }

        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement insertTeamPreparedStatement = connection.prepareStatement(INSERT_TEAM_SQL_STRING, Statement.RETURN_GENERATED_KEYS)) {
                insertTeamPreparedStatement.setString(1, model.getTeamName());
                insertTeamPreparedStatement.executeUpdate();
                long generatedId;
                try (ResultSet generatedKeys = insertTeamPreparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getLong(1);
                        Team teamWithId = new Team(model);
                        teamWithId.setId(generatedId);
                        log.traceExit("Team {} successfully created", teamWithId);
                        return teamWithId;
                    }
                }
        } catch (SQLException exception) {
            log.error(exception);
        }
        log.traceExit("Null team has been returned!");
        return null;
    }

    @Override
    public Team findByTeamName(String teamName) {
        log.traceEntry("Searching for team with name {}", teamName);
        Team existingTeam = null;

        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findTeamByNamePreparedStatement = connection.prepareStatement(FIND_TEAM_BY_NAME_SQL_STRING)) {
            findTeamByNamePreparedStatement.setString(1, teamName);
            try (ResultSet resultSet = findTeamByNamePreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long teamId = resultSet.getLong("team_id");
                    String existingTeamName = resultSet.getString("team_name");
                    existingTeam = new Team(existingTeamName);
                    existingTeam.setId(teamId);
                }
            }
        } catch (SQLException exception) {
            log.error(exception);
        }
        log.traceExit("Found team is {}", existingTeam);
        return existingTeam;
    }

    @Override
    public Team findOne(Long modelID) {
        throw new NotImplementedRepositoryMethodException();
    }

    @Override
    public Iterable<Team> findAll() {
        throw new NotImplementedRepositoryMethodException();
    }
}

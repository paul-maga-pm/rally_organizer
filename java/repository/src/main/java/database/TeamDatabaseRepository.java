package database;

import exceptions.DatabaseException;
import exceptions.NotImplementedMethodException;
import interfaces.TeamRepository;
import models.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.*;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class TeamDatabaseRepository implements TeamRepository {
    private JdbcUtils jdbcUtils;

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
        Function<Team, Team> saveTeamFunction = team -> {
            log.traceEntry("Saving team {}", model);
            var existingTeamOptional = findTeamByName(model.getTeamName());
            if (existingTeamOptional.isPresent()) {
                log.info("Team with same name already exists: {}", existingTeamOptional.get());
                log.traceExit();
                return existingTeamOptional.get();
            }

            try (Connection connection = jdbcUtils.getConnection();
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
        };

        var savedTeam = saveTeamFunction.apply(model);

        if (savedTeam == null)
            throw new DatabaseException("DatabaseException occurred while saving team " + model);

        return savedTeam;
    }

    @Override
    public Optional<Team> findTeamByName(String teamName) {

        Function<String, Optional<Team>> findTeamByNameFunction = name -> {
            log.traceEntry("Searching for team with name {}", teamName);

            try (Connection connection = jdbcUtils.getConnection();
                 PreparedStatement findTeamByNamePreparedStatement = connection.prepareStatement(FIND_TEAM_BY_NAME_SQL_STRING)) {
                findTeamByNamePreparedStatement.setString(1, teamName);
                try (ResultSet resultSet = findTeamByNamePreparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Long teamId = resultSet.getLong("team_id");
                        String existingTeamName = resultSet.getString("team_name");
                        var existingTeam = new Team(existingTeamName);
                        existingTeam.setId(teamId);
                        log.info("Found team is {}", existingTeam);
                        log.traceExit();
                        return Optional.of(existingTeam);
                    } else
                        return Optional.empty();
                }
            } catch (SQLException exception) {
                log.error(exception);
            }
            log.traceExit();
            return null;
        };

        var existingTeamOptional = findTeamByNameFunction.apply(teamName);

        if (existingTeamOptional == null)
            throw new DatabaseException("DatabaseException occurred while searching for team with name " + teamName);

        return existingTeamOptional;
    }

    @Override
    public Team findOne(Long modelID) {
        throw new NotImplementedMethodException();
    }

    @Override
    public Iterable<Team> findAll() {
        throw new NotImplementedMethodException();
    }
}

package com.rallies.dataaccess.impl.database;

import com.rallies.exceptions.NotImplementedMethodException;
import com.rallies.dataaccess.api.TeamRepository;
import com.rallies.domain.models.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class TeamDatabaseRepository implements TeamRepository {
    private JdbcUtils jdbcUtils;

    private Logger log = LogManager.getLogger();

    private static final String INSERT_TEAM_SQL_STRING = "insert into teams(team_name) values (?)";
    private static final String FIND_TEAM_BY_NAME_SQL_STRING = "select team_id, team_name from teams where team_name=?";
    private static final String FIND_ALL_TEAMS_SQL_STRING = "select team_id, team_name from teams";


    public TeamDatabaseRepository(Properties databaseConnectionProperties) {
        log.traceEntry("Creating team com.rallies.dataaccess.impl.database com.rallies.dataaccess.repository...");
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        log.traceEntry("Team com.rallies.dataaccess.impl.database com.rallies.dataaccess.repository created");

    }

    @Override
    public Team save(Team model) {
        Function<Team, Team> saveTeamFunction = team -> {
            log.traceEntry("Saving team {}", model);
            var existingTeamOptional = getTeamByName(model.getTeamName());
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
    public Optional<Team> getTeamByName(String teamName) {

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
    public Team getById(Long modelID) {
        throw new NotImplementedMethodException();
    }

    @Override
    public Collection<Team> getAll() {
        log.traceEntry("Searching for all teams...");
        Supplier<Collection<Team>> teamCollectionSupplier = () -> {
            try(Connection connection = jdbcUtils.getConnection();
                PreparedStatement selectAllTeamsPreparedStatement = connection.prepareStatement(FIND_ALL_TEAMS_SQL_STRING);
                ResultSet resultSet = selectAllTeamsPreparedStatement.executeQuery()) {

                var allTeams = new ArrayList<Team>();
                while (resultSet.next()) {
                    long teamId = resultSet.getLong("team_id");
                    String teamName = resultSet.getString("team_name");
                    Team team = new Team(teamName);
                    team.setId(teamId);
                    allTeams.add(team);
                }

                return allTeams;
            } catch (SQLException exception) {
                log.error(exception);
            }

            return null;
        };

        Collection<Team> allTeams = teamCollectionSupplier.get();

        if (allTeams == null)
            throw new DatabaseException("Database exception occurred while returning all teams");

        log.info("Found teams {}", allTeams);
        return allTeams;
    }
}

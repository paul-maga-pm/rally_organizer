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
                    } else throw new DatabaseException("DatabaseException occurred while searching for the generated id for team" + model);
                }
        } catch (SQLException exception) {
            log.error(exception);
            throw new DatabaseException("DatabaseException occurred while saving team " + model);
        }
    }

    @Override
    public Optional<Team> getTeamByName(String teamName) {
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
            throw new DatabaseException("DatabaseException occurred while searching for team with name " + teamName);
        }
    }

    @Override
    public Team getById(Long modelID) {
        throw new NotImplementedMethodException();
    }

    @Override
    public Collection<Team> getAll() {
        log.traceEntry("Searching for all teams...");
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
            log.info("Found teams {}", allTeams);
            return allTeams;
        } catch (SQLException exception) {
            log.error(exception);
            throw new DatabaseException("Database exception occurred while returning all teams");
        }
    }
}

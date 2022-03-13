package database;

import interfaces.Repository;
import interfaces.TeamRepository;
import models.Team;
import utils.JdbcUtils;
import validators.database.TeamDatabaseValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TeamDatabaseRepository implements TeamRepository {
    private JdbcUtils jdbcUtils;

    private TeamDatabaseValidator teamValidator = new TeamDatabaseValidator();

    private PreparedStatement insertTeamPreparedStatement;
    private PreparedStatement findTeamByNamePreparedStatement;

    public TeamDatabaseRepository(Properties databaseConnectionProperties) {
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        initializePreparedStatements();
    }

    @Override
    public Team save(Team model) {
        teamValidator.validate(model);

        var existingTeam = findByTeamName(model.getTeamName());
        if (existingTeam != null)
            return existingTeam;
        try {
            insertTeamPreparedStatement.setString(1, model.getTeamName());
            insertTeamPreparedStatement.executeUpdate();

            long generatedId;

            try (ResultSet generatedKeys = insertTeamPreparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                    Team teamWithId = new Team(model);
                    teamWithId.setId(generatedId);
                    return teamWithId;
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Team findOne(Long modelID) {
        throw new NotImplementedRepositoryMethodException();
    }

    @Override
    public Iterable<Team> findAll() {
        throw new NotImplementedRepositoryMethodException();
    }

    private void initializePreparedStatements() {
        String insertTeamSqlString = "insert into teams(team_name) values (?)";
        String findTeamByNameSqlString = "select team_id, team_name from teams where team_name=?";
        try {
            insertTeamPreparedStatement = jdbcUtils.getConnection().prepareStatement(insertTeamSqlString, Statement.RETURN_GENERATED_KEYS);
            findTeamByNamePreparedStatement = jdbcUtils.getConnection().prepareStatement(findTeamByNameSqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Team findByTeamName(String teamName) {
        Team existingTeam = null;

        try {
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
            exception.printStackTrace();
        }
        return existingTeam;
    }
}

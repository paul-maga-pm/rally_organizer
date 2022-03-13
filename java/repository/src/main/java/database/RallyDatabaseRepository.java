package database;

import interfaces.RallyRepository;
import models.Rally;
import utils.JdbcUtils;
import validators.database.RallyDatabaseValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RallyDatabaseRepository implements RallyRepository {

    private JdbcUtils jdbcUtils;

    private RallyDatabaseValidator rallyValidator = new RallyDatabaseValidator();

    private PreparedStatement insertRallyPreparedStatement;
    private PreparedStatement findAllRalliesPreparedStatement;
    private PreparedStatement findByEngineCapacityPreparedStatement;

    public RallyDatabaseRepository(Properties databaseConnectionProperties) {
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        initializePreparedStatements();
    }

    @Override
    public Rally save(Rally model) {
        rallyValidator.validate(model);

        var existingRally = findByEngineCapacity(model.getEngineCapacity());
        if (existingRally != null)
            return existingRally;

        try {
            insertRallyPreparedStatement.setInt(1, model.getEngineCapacity());
            insertRallyPreparedStatement.executeUpdate();

            try(ResultSet generatedKeys = insertRallyPreparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);
                    Rally rallyWithId = new Rally(model);
                    rallyWithId.setId(generatedId);
                    return rallyWithId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Rally findOne(Long modelID) {
        throw new NotImplementedRepositoryMethodException();
    }

    @Override
    public Iterable<Rally> findAll() {
        List<Rally> allRallies = new ArrayList<>();

        try(ResultSet foundRallies = findAllRalliesPreparedStatement.executeQuery()) {
            while (foundRallies.next()) {
                Long rallyId = foundRallies.getLong("rally_id");
                int engineCapacity = foundRallies.getInt("engine_capacity");
                int numberOfParticipants = foundRallies.getInt("number_of_participants");
                Rally currentRally = new Rally(engineCapacity, numberOfParticipants);
                currentRally.setId(rallyId);
                allRallies.add(currentRally);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return allRallies;
    }

    private void initializePreparedStatements() {
        String insertRallySqlString = "insert into rallies(engine_capacity) values (?)";
        String findAllRalliesSqlString = "select rally_id, engine_capacity, number_of_participants from rallies";
        String findByEngineCapacitySqlString = "select rally_id, engine_capacity, number_of_participants from rallies where engine_capacity=?";

        try {
            insertRallyPreparedStatement = jdbcUtils.getConnection().prepareStatement(insertRallySqlString, Statement.RETURN_GENERATED_KEYS);
            findAllRalliesPreparedStatement = jdbcUtils.getConnection().prepareStatement(findAllRalliesSqlString);
            findByEngineCapacityPreparedStatement = jdbcUtils.getConnection().prepareStatement(findByEngineCapacitySqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Rally findByEngineCapacity(int engineCapacity) {
        Rally existingRally = null;
        try {
            findByEngineCapacityPreparedStatement.setInt(1, engineCapacity);
            try(ResultSet resultSet = findByEngineCapacityPreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long rallyId = resultSet.getLong("rally_id");
                    int existingEngineCapacity = resultSet.getInt("engine_capacity");
                    int numberOfParticipants = resultSet.getInt("number_of_participants");
                    existingRally = new Rally(existingEngineCapacity, numberOfParticipants);
                    existingRally.setId(rallyId);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return existingRally;
    }
}

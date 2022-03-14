package database;

import interfaces.RallyRepository;
import models.Rally;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;
import validators.database.RallyDatabaseValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RallyDatabaseRepository implements RallyRepository {

    private JdbcUtils jdbcUtils;

    private RallyDatabaseValidator rallyValidator = new RallyDatabaseValidator();


    private static final String INSERT_RALLY_SQL_STRING = "insert into rallies(engine_capacity) values (?)";
    private static final String FIND_ALL_RALLIES_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies";
    private static final String FIND_BY_ENGINE_CAPACITY_SQL_STRING = "select rally_id, engine_capacity, number_of_participants from rallies where engine_capacity=?";

    private Logger logger = LogManager.getLogger();

    public RallyDatabaseRepository(Properties databaseConnectionProperties) {
        logger.info("Creating rally database repository...");
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
        logger.info("Rally database repository created");
    }

    @Override
    public Rally save(Rally model) {
        logger.traceEntry("Saving rally {}", model);
        rallyValidator.validate(model);

        var existingRally = findRallyByEngineCapacity(model.getEngineCapacity());
        if (existingRally != null) {
            logger.traceExit("Rally {} already exists", existingRally);
            return existingRally;
        }

        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement insertRallyPreparedStatement =
                    connection.prepareStatement(INSERT_RALLY_SQL_STRING, Statement.RETURN_GENERATED_KEYS)) {

            insertRallyPreparedStatement.setInt(1, model.getEngineCapacity());
            insertRallyPreparedStatement.executeUpdate();
            try(ResultSet generatedKeys = insertRallyPreparedStatement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);
                    Rally rallyWithId = new Rally(model);
                    rallyWithId.setId(generatedId);
                    logger.traceExit("New rally created {}", rallyWithId);
                    return rallyWithId;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Null rally returned");
        return null;
    }


    @Override
    public Iterable<Rally> findAll() {
        logger.traceEntry("Finding all rallies...");
        List<Rally> allRallies = new ArrayList<>();

        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findAllRalliesPreparedStatement = connection.prepareStatement(FIND_ALL_RALLIES_SQL_STRING)) {
            try (ResultSet foundRallies = findAllRalliesPreparedStatement.executeQuery()) {
                while (foundRallies.next()) {
                    Long rallyId = foundRallies.getLong("rally_id");
                    int engineCapacity = foundRallies.getInt("engine_capacity");
                    int numberOfParticipants = foundRallies.getInt("number_of_participants");
                    Rally currentRally = new Rally(engineCapacity, numberOfParticipants);
                    currentRally.setId(rallyId);
                    allRallies.add(currentRally);
                }
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }
        logger.traceExit();
        return allRallies;
    }



    @Override
    public Rally findRallyByEngineCapacity(int engineCapacity) {
        logger.traceEntry("Finding one rally by engine capacity...");
        Rally existingRally = null;
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findByEngineCapacityPreparedStatement = connection.prepareStatement(FIND_BY_ENGINE_CAPACITY_SQL_STRING)) {
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
            logger.error(exception);
        }
        logger.traceExit("{} has been returned");
        return existingRally;
    }

    @Override
    public Rally findOne(Long modelID) {
        throw new NotImplementedRepositoryMethodException();
    }
}

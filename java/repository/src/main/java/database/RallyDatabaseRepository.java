package database;

import exceptions.DatabaseException;
import exceptions.NotImplementedMethodException;
import interfaces.RallyRepository;
import models.Rally;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

public class RallyDatabaseRepository implements RallyRepository {

    private JdbcUtils jdbcUtils;

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
        Function<Rally, Rally> saveRallyFunction = rally -> {

            logger.traceEntry("Saving rally {}", model);

            var existingRallyOptional = findRallyByEngineCapacity(model.getEngineCapacity());
            if (existingRallyOptional.isPresent()) {
                logger.info("Rally {} already exists", existingRallyOptional.get());
                logger.traceExit();
                return existingRallyOptional.get();
            }

            try (Connection connection = jdbcUtils.getConnection();
                 PreparedStatement insertRallyPreparedStatement =
                         connection.prepareStatement(INSERT_RALLY_SQL_STRING, Statement.RETURN_GENERATED_KEYS)) {

                insertRallyPreparedStatement.setInt(1, model.getEngineCapacity());
                insertRallyPreparedStatement.executeUpdate();
                try (ResultSet generatedKeys = insertRallyPreparedStatement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        Long generatedId = generatedKeys.getLong(1);
                        Rally rallyWithId = new Rally(model);
                        rallyWithId.setId(generatedId);
                        logger.info("New rally created {}", rallyWithId);
                        logger.traceExit();
                        return rallyWithId;
                    }
                }
            } catch (SQLException e) {
                logger.error(e);
            }
            logger.traceExit();
            return null;
        };

        var savedRally = saveRallyFunction.apply(model);

        if (savedRally == null)
            throw new DatabaseException("DatabaseException occurred while saving rally " + model);

        return savedRally;
    }


    @Override
    public Iterable<Rally> findAll() {
        Supplier<Iterable<Rally>> findAllSupplier = () -> {
                logger.traceEntry("Returning all rallies...");

                try (Connection connection = jdbcUtils.getConnection();
                     PreparedStatement findAllRalliesPreparedStatement = connection.prepareStatement(FIND_ALL_RALLIES_SQL_STRING)) {
                    try (ResultSet foundRallies = findAllRalliesPreparedStatement.executeQuery()) {
                        List<Rally> allRallies = new ArrayList<>();
                        while (foundRallies.next()) {
                            Long rallyId = foundRallies.getLong("rally_id");
                            int engineCapacity = foundRallies.getInt("engine_capacity");
                            int numberOfParticipants = foundRallies.getInt("number_of_participants");
                            Rally currentRally = new Rally(engineCapacity, numberOfParticipants);
                            currentRally.setId(rallyId);
                            allRallies.add(currentRally);
                        }
                        logger.traceExit();
                        return allRallies;
                    }
                } catch (SQLException exception) {
                    logger.error(exception);
                }
                logger.traceExit();
                return null;
        };

        var rallies = findAllSupplier.get();

        if (rallies == null)
            throw new DatabaseException("DatabaseException occurred while returning all rallies");

        return rallies;
    }



    @Override
    public Optional<Rally> findRallyByEngineCapacity(int engineCapacity) {
        Function<Integer, Optional<Rally>> findRallyByEngineCapacityFunction = capacity -> {
            logger.traceEntry("Finding one rally by engine capacity...");

            try (Connection connection = jdbcUtils.getConnection();
                 PreparedStatement findByEngineCapacityPreparedStatement = connection.prepareStatement(FIND_BY_ENGINE_CAPACITY_SQL_STRING)) {
                findByEngineCapacityPreparedStatement.setInt(1, engineCapacity);
                try (ResultSet resultSet = findByEngineCapacityPreparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Long rallyId = resultSet.getLong("rally_id");
                        int existingEngineCapacity = resultSet.getInt("engine_capacity");
                        int numberOfParticipants = resultSet.getInt("number_of_participants");
                        var existingRally = new Rally(existingEngineCapacity, numberOfParticipants);
                        existingRally.setId(rallyId);
                        logger.info("Found rally {}", existingRally);
                        logger.traceExit();
                        return Optional.of(existingRally);
                    } else
                        return Optional.empty();
                }
            } catch (SQLException exception) {
                logger.error(exception);
            }
            logger.traceExit();
            return null;
        };

        var foundRally = findRallyByEngineCapacityFunction.apply(engineCapacity);

        if (foundRally == null)
            throw new DatabaseException("DatabaseException occurred while searching for rally with engineCapacity " + engineCapacity);

        return foundRally;
    }

    @Override
    public Rally findOne(Long modelID) {
        throw new NotImplementedMethodException();
    }
}

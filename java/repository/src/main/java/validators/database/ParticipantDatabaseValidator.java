package validators.database;

import models.Participant;
import utils.JdbcUtils;
import validators.interfaces.ParticipantValidator;
import validators.interfaces.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ParticipantDatabaseValidator implements ParticipantValidator {
    private JdbcUtils jdbcUtils;

    private PreparedStatement findTeamByIdPreparedStatement;
    private PreparedStatement findRallyByIdPreparedStatement;

    private static final String FIND_TEAM_BY_ID = "select * from teams where team_id=?";
    private static final String FIND_RALLY_BY_ID = "select * from rallies where rally_id=?";

    public ParticipantDatabaseValidator(Properties databaseConnectionProperties) {
        this.jdbcUtils = new JdbcUtils(databaseConnectionProperties);
    }

    @Override
    public void validate(Participant participant) {
        String errors = "";

        if (!teamOfParticipantExists(participant))
            errors += "Selected team doesn't exist!";
        if (!rallyOfParticipantIsAssignedToExists(participant))
            errors += "\nSelected rally doesn't exist!";
        if (participant.getParticipantName().equals(""))
            errors += "\nParticipant's name can't be empty!";

        if (!errors.equals(""))
            throw new Validator.InvalidModelException(errors);
    }


    private boolean teamOfParticipantExists(Participant participant) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findTeamByIdPreparedStatement = connection.prepareStatement(FIND_TEAM_BY_ID)) {
            findTeamByIdPreparedStatement.setLong(1, participant.getTeam().getId());
            try(ResultSet resultSet = findTeamByIdPreparedStatement.executeQuery()) {
              return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean rallyOfParticipantIsAssignedToExists(Participant participant) {
        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement findRallyByIdPreparedStatement = connection.prepareStatement(FIND_RALLY_BY_ID)) {
            findRallyByIdPreparedStatement.setLong(1, participant.getRally().getId());
            try(ResultSet resultSet = findRallyByIdPreparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

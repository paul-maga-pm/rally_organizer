package validators.database;

import models.Participant;
import utils.JdbcUtils;
import validators.interfaces.ParticipantValidator;
import validators.interfaces.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantDatabaseValidator implements ParticipantValidator {
    private JdbcUtils jdbcUtils;

    private PreparedStatement findTeamByIdPreparedStatement;
    private PreparedStatement findRallyByIdPreparedStatement;

    private static final String FIND_TEAM_BY_ID = "select * from teams where team_id=?";
    private static final String FIND_RALLY_BY_ID = "select * from rallies where rally_id=?";

    public ParticipantDatabaseValidator(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
        initializeFindByIdPreparedStatements();
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

    private void initializeFindByIdPreparedStatements() {
        try {
            findTeamByIdPreparedStatement = jdbcUtils.getConnection().prepareStatement(FIND_TEAM_BY_ID);
            findRallyByIdPreparedStatement = jdbcUtils.getConnection().prepareStatement(FIND_RALLY_BY_ID);
        } catch (SQLException exception) {

        }
    }

    private boolean teamOfParticipantExists(Participant participant) {
        try {
            findTeamByIdPreparedStatement.setLong(1, participant.getTeam().getId());
            try(ResultSet resultSet = findTeamByIdPreparedStatement.executeQuery()) {
              return resultSet.next();
            }
        } catch (SQLException e) {

        }
        return false;
    }

    private boolean rallyOfParticipantIsAssignedToExists(Participant participant) {
        try {
            findRallyByIdPreparedStatement.setLong(1, participant.getRally().getId());
            try(ResultSet resultSet = findRallyByIdPreparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {

        }
        return false;

    }


}

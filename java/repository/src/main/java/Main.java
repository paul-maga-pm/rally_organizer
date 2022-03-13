import database.ParticipantDatabaseRepository;
import database.RallyDatabaseRepository;
import database.TeamDatabaseRepository;
import models.Participant;
import models.Rally;
import models.Team;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties databaseConnectionProperties = new Properties();
        try {
            databaseConnectionProperties.load(Main.class.getClassLoader().getResourceAsStream("database_connection_config.properties"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        var participantRepo = new ParticipantDatabaseRepository(databaseConnectionProperties);
        var rallyRepo = new RallyDatabaseRepository(databaseConnectionProperties);
        var teamRepo = new TeamDatabaseRepository(databaseConnectionProperties);

        var team1 = new Team("Honda");
        var team2 = new Team("Suzuki");

        team1 = teamRepo.save(team1);
        team2 = teamRepo.save(team2);

        var rally1 = new Rally(120);
        var rally2 = new Rally(250);

        rally1 = rallyRepo.save(rally1);
        rally2 = rallyRepo.save(rally2);

        var part1 = new Participant(team1, rally1, "Alberto");
        var part2 = new Participant(team1, rally2, "Gigel");
        var part3 = new Participant(team2, rally1, "Michael");
        var part4 = new Participant(team2, rally2, "Hector");

        participantRepo.save(part1);
        participantRepo.save(part2);
        participantRepo.save(part3);
        participantRepo.save(part4);

        participantRepo.findByTeamName("Honda").forEach(System.out::println);
        rallyRepo.findAll().forEach(System.out::println);
    }
}

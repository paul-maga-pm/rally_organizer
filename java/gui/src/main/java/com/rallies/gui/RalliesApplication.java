package com.rallies.gui;

import business.services.ParticipantService;
import business.services.RallyService;
import business.services.TeamService;
import business.services.UserService;
import com.rallies.gui.controllers.AuthenticationController;
import com.rallies.gui.controllers.MainWindowController;
import repository.database.ParticipantDatabaseRepository;
import repository.database.RallyDatabaseRepository;
import repository.database.TeamDatabaseRepository;
import repository.database.UserDatabaseRepository;
import repository.interfaces.ParticipantRepository;
import repository.interfaces.RallyRepository;
import repository.interfaces.TeamRepository;
import repository.interfaces.UserRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class RalliesApplication extends Application {
    private UserService userService;
    private ParticipantService participantService;
    private TeamService teamService;
    private RallyService rallyService;

    private AuthenticationController authenticationController;
    private MainWindowController mainWindowController;

    @Override
    public void start(Stage stage) throws IOException {

        initializeServices();


        FXMLLoader authenticationFxmlLoader = new FXMLLoader(RalliesApplication.class.getResource("authentication-view.fxml"));
        FXMLLoader ralliesFxmlLoader = new FXMLLoader(RalliesApplication.class.getResource("rallies-view.fxml"));
        Scene ralliesScene = new Scene(ralliesFxmlLoader.load());
        Scene authenticationScene = new Scene(authenticationFxmlLoader.load());


        authenticationController = authenticationFxmlLoader.getController();

        authenticationController.setUserService(userService);
        authenticationController.setParticipantService(participantService);
        authenticationController.setRallyService(rallyService);
        authenticationController.setTeamService(teamService);
        authenticationController.setPrimaryStage(stage);
        authenticationController.setRalliesScene(ralliesScene);


        mainWindowController = ralliesFxmlLoader.getController();
        mainWindowController.setRallyService(rallyService);
        mainWindowController.setTeamService(teamService);
        mainWindowController.setParticipantService(participantService);
        mainWindowController.setPrimaryStage(stage);
        mainWindowController.setAuthenticationScene(authenticationScene);

        authenticationController.setRalliesController(mainWindowController);

        stage.setTitle("Login");
        stage.setScene(authenticationScene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static Properties loadRepositoryConfigurationProperties(String resourceFileName) throws IOException {
        Properties properties = new Properties();
        properties.load(RalliesApplication.class.getResourceAsStream(resourceFileName));
        return properties;
    }

    private static UserRepository createUserRepository(Properties repositoryConfigProperties) {
        return new UserDatabaseRepository(repositoryConfigProperties);
    }

    private static UserService createUserService(Properties repositoryConfigProperties) {
        return new UserService(createUserRepository(repositoryConfigProperties));
    }

    private static ParticipantRepository createParticipantRepository(Properties repositoryConfigProperties) {
        return new ParticipantDatabaseRepository(repositoryConfigProperties);
    }

    private static ParticipantService createParticipantService(Properties repositoryConfigProperties) {
        return new ParticipantService(createParticipantRepository(repositoryConfigProperties));
    }
    
    private static RallyRepository createRallyRepository(Properties repositoryConfigProperties) {
        return new RallyDatabaseRepository(repositoryConfigProperties);
    }
    
    private static RallyService createRallyService(Properties repositoryConfigProperties) {
        return new RallyService(createRallyRepository(repositoryConfigProperties));
    }

    private TeamRepository createTeamRepository(Properties repositoryConfigProperties) {
        return new TeamDatabaseRepository(repositoryConfigProperties);
    }

    private TeamService createTeamService(Properties repositoryConfigProperties) {
        return new TeamService(createTeamRepository(repositoryConfigProperties));
    }

    private void initializeServices() throws IOException {
        String repositoryConfigFile = "database_connection_config.properties";
        Properties repositoryConfigProperties = loadRepositoryConfigurationProperties(repositoryConfigFile);

        userService = createUserService(repositoryConfigProperties);
        participantService = createParticipantService(repositoryConfigProperties);
        rallyService = createRallyService(repositoryConfigProperties);
        teamService = createTeamService(repositoryConfigProperties);
    }
}
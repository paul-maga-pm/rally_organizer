package com.rallies.gui.impl;

import com.rallies.business.services.impl.ParticipantService;
import com.rallies.business.services.impl.RallyService;
import com.rallies.business.services.impl.TeamService;
import com.rallies.business.services.impl.UserService;
import com.rallies.gui.impl.controllers.AuthenticationController;
import com.rallies.gui.impl.controllers.MainWindowController;
import com.rallies.dataaccess.repository.impl.database.ParticipantDatabaseRepository;
import com.rallies.dataaccess.repository.impl.database.RallyDatabaseRepository;
import com.rallies.dataaccess.repository.impl.database.TeamDatabaseRepository;
import com.rallies.dataaccess.repository.impl.database.UserDatabaseRepository;
import com.rallies.dataaccess.repository.api.ParticipantRepository;
import com.rallies.dataaccess.repository.api.RallyRepository;
import com.rallies.dataaccess.repository.api.TeamRepository;
import com.rallies.dataaccess.repository.api.UserRepository;
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
        FXMLLoader ralliesFxmlLoader = new FXMLLoader(RalliesApplication.class.getResource("main-window-view.fxml"));
        Scene mainWindowsScene = new Scene(ralliesFxmlLoader.load());
        Scene authenticationScene = new Scene(authenticationFxmlLoader.load());


        authenticationController = authenticationFxmlLoader.getController();

        authenticationController.setUserService(userService);
        authenticationController.setParticipantService(participantService);
        authenticationController.setRallyService(rallyService);
        authenticationController.setTeamService(teamService);
        authenticationController.setPrimaryStage(stage);
        authenticationController.setMainWindowScene(mainWindowsScene);


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
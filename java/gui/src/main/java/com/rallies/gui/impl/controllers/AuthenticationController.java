package com.rallies.gui.impl.controllers;

import com.rallies.business.services.impl.AuthenticationException;
import com.rallies.business.services.impl.ParticipantService;
import com.rallies.business.services.impl.RallyService;
import com.rallies.business.services.impl.TeamService;
import com.rallies.business.services.impl.UserService;
import com.rallies.exceptions.impl.ExceptionBaseClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class AuthenticationController {
    private UserService userService;
    private TeamService teamService;
    private RallyService rallyService;
    private ParticipantService participantService;
    private MainWindowController mainWindowController;

    @FXML
    Button loginButton;
    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label authenticationExceptionsLabel;

    private Stage primaryStage;
    private Scene mainWindowScene;

    @FXML
    void handleClickOnLoginButton(Event event) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        try {
            userService.login(username, password);
            mainWindowController.initializeModels();
            primaryStage.setScene(mainWindowScene);
        } catch (AuthenticationException authenticationException) {
            authenticationExceptionsLabel.setText(authenticationException.getMessage());
        } catch (ExceptionBaseClass exceptionBaseClass) {
            showExceptionMessageBox(exceptionBaseClass);
        }
    }

    private void showExceptionMessageBox(ExceptionBaseClass exceptionBaseClass) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionBaseClass.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    public void setRallyService(RallyService rallyService) {
        this.rallyService = rallyService;
    }

    public void setParticipantService(ParticipantService participantService) {
        this.participantService = participantService;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setMainWindowScene(Scene mainWindowScene) {
        this.mainWindowScene = mainWindowScene;
    }

    public void setRalliesController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}

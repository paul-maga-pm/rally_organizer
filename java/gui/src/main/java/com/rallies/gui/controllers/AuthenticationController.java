package com.rallies.gui.controllers;

import business.exceptions.AuthenticationException;
import business.services.ParticipantService;
import business.services.RallyService;
import business.services.TeamService;
import business.services.UserService;
import exceptions.ExceptionBaseClass;
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


    @FXML
    Button loginButton;
    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label authenticationExceptionsLabel;

    private Stage primaryStage;
    private Scene ralliesScene;
    private MainWindowController mainWindowController;

    @FXML
    void handleClickOnLoginButton(Event event) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        try {
            userService.login(username, password);
            primaryStage.setScene(ralliesScene);
            mainWindowController.initializeModels();
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

    public void setRalliesScene(Scene ralliesScene) {
        this.ralliesScene = ralliesScene;
    }

    public void setRalliesController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}
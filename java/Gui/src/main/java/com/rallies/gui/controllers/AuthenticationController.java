package com.rallies.gui.controllers;

import com.rallies.business.api.AuthenticationException;
import com.rallies.business.api.RallyApplicationServices;
import com.rallies.exceptions.ExceptionBaseClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class AuthenticationController {
    private RallyApplicationServices services;
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
            services.login(username, password);
            mainWindowController.initializeModels();
            services.addObserver(mainWindowController);
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

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setMainWindowScene(Scene mainWindowScene) {
        this.mainWindowScene = mainWindowScene;
    }

    public void setRalliesController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setServices(RallyApplicationServices services) {
        this.services = services;
    }
}

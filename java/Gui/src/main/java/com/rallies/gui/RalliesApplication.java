package com.rallies.gui;

import com.rallies.gui.controllers.AuthenticationController;
import com.rallies.gui.controllers.MainWindowController;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class RalliesApplication extends Application {


    private AuthenticationController authenticationController;
    private MainWindowController mainWindowController;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader authenticationFxmlLoader = new FXMLLoader(RalliesApplication.class.getResource("authentication-view.fxml"));
        FXMLLoader ralliesFxmlLoader = new FXMLLoader(RalliesApplication.class.getResource("main-window-view.fxml"));
        Scene mainWindowsScene = new Scene(ralliesFxmlLoader.load());
        Scene authenticationScene = new Scene(authenticationFxmlLoader.load());


        authenticationController = authenticationFxmlLoader.getController();


        authenticationController.setPrimaryStage(stage);
        authenticationController.setMainWindowScene(mainWindowsScene);


        mainWindowController = ralliesFxmlLoader.getController();

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


}
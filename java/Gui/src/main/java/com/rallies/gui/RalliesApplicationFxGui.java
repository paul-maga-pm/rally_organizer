package com.rallies.gui;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.gui.controllers.AuthenticationController;
import com.rallies.gui.controllers.MainWindowController;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class RalliesApplicationFxGui extends Application {
    private static RallyApplicationServices services;

    private AuthenticationController authenticationController;
    private MainWindowController mainWindowController;

    public static void setServices(RallyApplicationServices services) {
        RalliesApplicationFxGui.services = services;
    }

    public RalliesApplicationFxGui() {

    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader authenticationFxmlLoader = new FXMLLoader(RalliesApplicationFxGui.class.getResource("authentication-view.fxml"));
        FXMLLoader ralliesFxmlLoader = new FXMLLoader(RalliesApplicationFxGui.class.getResource("main-window-view.fxml"));
        Scene mainWindowsScene = new Scene(ralliesFxmlLoader.load());
        Scene authenticationScene = new Scene(authenticationFxmlLoader.load());


        authenticationController = authenticationFxmlLoader.getController();
        authenticationController.setPrimaryStage(stage);
        authenticationController.setMainWindowScene(mainWindowsScene);
        authenticationController.setServices(services);

        mainWindowController = ralliesFxmlLoader.getController();
        mainWindowController.setPrimaryStage(stage);
        mainWindowController.setAuthenticationScene(authenticationScene);
        mainWindowController.setServices(services);

        authenticationController.setRalliesController(mainWindowController);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (stage.getScene() == mainWindowsScene) {
                    services.logout();
                    services.removeObserver(mainWindowController);
                }
            }
        });

        stage.setTitle("Login");
        stage.setScene(authenticationScene);
        stage.setResizable(false);
        stage.show();
    }
}
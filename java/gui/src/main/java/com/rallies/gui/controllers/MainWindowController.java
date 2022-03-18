package com.rallies.gui.controllers;

import business.services.ParticipantService;
import business.services.RallyService;
import business.services.TeamService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Rally;



public class MainWindowController {
    private ParticipantService participantService;
    private TeamService teamService;
    private RallyService rallyService;
    private Stage primaryStage;
    private Scene authenticationScene;

    private ObservableList<Rally> rallyObservableList;

    @FXML
    private TableView<Rally> ralliesTableView;
    @FXML
    Button logoutButton;

    public void setParticipantService(ParticipantService participantService) {
        this.participantService = participantService;
    }

    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    public void setRallyService(RallyService rallyService) {
        this.rallyService = rallyService;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setAuthenticationScene(Scene authenticationScene) {
        this.authenticationScene = authenticationScene;
    }

    @FXML
    void handleClickOnLogoutButton(Event event) {
        primaryStage.setScene(authenticationScene);
    }

    public void initializeModels() {
        if (rallyObservableList == null) {
            rallyObservableList = FXCollections.observableArrayList();
            rallyObservableList.setAll(rallyService.findAllRallies());

            TableColumn<Rally, Integer> engineCapacityColumn = new TableColumn<>("Engine capacity");
            engineCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("engineCapacity"));

            TableColumn<Rally, Integer> numberOfParticipantsColumn = new TableColumn<>("Number of participants");
            numberOfParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));

            ralliesTableView.setItems(rallyObservableList);
            ralliesTableView.getColumns().add(engineCapacityColumn);
            ralliesTableView.getColumns().add(numberOfParticipantsColumn);

            engineCapacityColumn.setMinWidth(100);
            numberOfParticipantsColumn.setMinWidth(200);
        }
    }
}

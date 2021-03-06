package com.rallies.gui.controllers;

import com.rallies.business.api.RalliesObserver;
import com.rallies.business.api.RallyApplicationServices;
import com.rallies.exceptions.ExceptionBaseClass;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;


public class MainWindowController implements RalliesObserver {
    private RallyApplicationServices services;
    private Stage primaryStage;
    private Scene authenticationScene;

    private ObservableList<Rally> rallyObservableList;
    private ObservableList<Team> teamObservableList;

    @FXML
    private TableView<Rally> ralliesTableView;
    @FXML
    private TableColumn<Rally, Integer> engineCapacityOfRallyTableColumn;

    @FXML
    private TableColumn<Rally, Integer> numberOfParticipantsTableColumn;

    @FXML
    Button logoutButton;

    @FXML
    ChoiceBox<Team> teamNamesChoiceBox;

    @FXML
    TableView<Participant> foundTeamMembersTableView;
    @FXML
    TableColumn<Participant, String> participantNameTableColumn;
    @FXML
    TableColumn<Participant, Integer> engineCapacityOfParticipantTableColumn;
    ObservableList<Participant> foundMembersOfTeamObservableList = FXCollections.observableArrayList();

    @FXML
    TextField engineCapacityTextField;
    @FXML
    Label addRallyExceptionLabel;

    @FXML
    TextField teamNameTextField;
    @FXML
    Label addTeamExceptionLabel;
    @FXML
    Label addParticipantExceptionLabel;

    @FXML
    ChoiceBox<Team> teamNameRegistrationChoiceBox;
    @FXML
    ChoiceBox<Rally> engineCapacityRegistrationChoiceBox;

    @FXML
    TextField participantNameTextField;

    @FXML
    Button registerButton;


    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setAuthenticationScene(Scene authenticationScene) {
        this.authenticationScene = authenticationScene;
    }
    public void showExceptionMessageBox(ExceptionBaseClass exceptionBaseClass) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionBaseClass.getMessage(), ButtonType.CLOSE);
        alert.showAndWait();
    }
    @FXML
    void handleClickOnLogoutButton(Event event) {
        Platform.runLater(() -> {
            try {
                services.logout();
                services.removeObserver(this);
            } catch (ExceptionBaseClass exceptionBaseClass) {
                showExceptionMessageBox(exceptionBaseClass);
            }
        });
        primaryStage.setScene(authenticationScene);
    }

    public void initializeModels() {
        try {
            rallyObservableList = FXCollections.observableArrayList();
            rallyObservableList.setAll(services.getAllRallies());
            ralliesTableView.setItems(rallyObservableList);
            engineCapacityRegistrationChoiceBox.setItems(rallyObservableList);
            teamObservableList = FXCollections.observableArrayList();
            teamObservableList.setAll(services.getAllTeams());
            teamNamesChoiceBox.setItems(teamObservableList);
            teamNameRegistrationChoiceBox.setItems(teamObservableList);
        } catch (ExceptionBaseClass exceptionBaseClass) {
            showExceptionMessageBox(exceptionBaseClass);
        }
    }

    @FXML
    void initialize() {

        engineCapacityOfRallyTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getEngineCapacity()));
        numberOfParticipantsTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getNumberOfParticipants()));

        participantNameTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getParticipantName()));
        engineCapacityOfParticipantTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getRally().getEngineCapacity()));

        foundTeamMembersTableView.setItems(foundMembersOfTeamObservableList);
        teamNamesChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
            @Override
            public void changed(ObservableValue<? extends Team> observable, Team oldValue, Team newValue) {
                if (observable.getValue() != null) {
                    String teamName = observable.getValue().getTeamName();
                    try {
                        foundMembersOfTeamObservableList.setAll(services.getAllMembersOfTeam(teamName));
                    } catch (ExceptionBaseClass exceptionBaseClass) {
                        showExceptionMessageBox(exceptionBaseClass);
                    }
                }
            }
        });

        teamNamesChoiceBox.setConverter(new StringConverter<Team>() {
            @Override
            public String toString(Team object) {
                if (object == null)
                    return null;
                return object.getTeamName();
            }

            @Override
            public Team fromString(String string) {
                return null;
            }
        });

        addRallyExceptionLabel.setText("");
        addTeamExceptionLabel.setText("");
        addParticipantExceptionLabel.setText("");


        teamNameRegistrationChoiceBox.setItems(teamObservableList);
        engineCapacityRegistrationChoiceBox.setItems(rallyObservableList);
        teamNameRegistrationChoiceBox.setConverter(new StringConverter<Team>() {
            @Override
            public String toString(Team object) {
                if (object == null)
                    return null;
                return object.getTeamName();
            }

            @Override
            public Team fromString(String string) {
                return null;
            }
        });

        engineCapacityRegistrationChoiceBox.setConverter(new StringConverter<Rally>() {
            @Override
            public String toString(Rally object) {
                if (object == null)
                    return null;
                return String.valueOf(object.getEngineCapacity());
            }

            @Override
            public Rally fromString(String string) {
                return null;
            }
        });


    }

    @FXML
    void handleClickOnAddRallyButton(Event event) {
        String engineCapacityString = engineCapacityTextField.getText();

        try {
            int engineCapacity = Integer.parseInt(engineCapacityString);
            if (engineCapacity < 50 || engineCapacity > 2000) {
                addRallyExceptionLabel.setText("Capacity must be between 50 and 2000");
                return;
            }

            Rally addedRally = services.addRally(engineCapacity);

            if (!rallyObservableList.contains(addedRally))
                rallyObservableList.add(addedRally);

            addRallyExceptionLabel.setText("");

        } catch (NumberFormatException exception) {
            addRallyExceptionLabel.setText("Invalid numerical value for engine capacity");
        } catch (ExceptionBaseClass exceptionBaseClass) {
            showExceptionMessageBox(exceptionBaseClass);
        }
    }

    @FXML
    void handleClickOnAddTeamButton(Event event) {
        String teamName = teamNameTextField.getText().strip();

        try {
            if (teamName.equals(""))
                addTeamExceptionLabel.setText("Team name can't be empty!");
            else if (services.getTeamByName(teamName).isPresent())
                addTeamExceptionLabel.setText(teamName + " team is already registered!");
            else {
                addTeamExceptionLabel.setText("");
                services.addTeam(teamName);
            }
        } catch (ExceptionBaseClass exceptionBaseClass) {
            showExceptionMessageBox(exceptionBaseClass);
        }
    }

    @FXML
    void handleClickOnRegisterButton(Event event) {
        String participantName = participantNameTextField.getText().strip();

        if (participantName.equals(""))
            addParticipantExceptionLabel.setText("Participant name can't be empty!");
        else {
            Team selectedTeam = teamNameRegistrationChoiceBox.getSelectionModel().getSelectedItem();

            if (selectedTeam == null) {
                addParticipantExceptionLabel.setText("You must select a team!");
                return;
            }

            Rally selectedRally = engineCapacityRegistrationChoiceBox.getSelectionModel().getSelectedItem();

            if (selectedRally == null) {
                addParticipantExceptionLabel.setText("You must select a rally!");
                return;
            }
            try {
                if (services.getParticipantByName(participantName).isPresent()) {
                    addParticipantExceptionLabel.setText(participantName + " is already registered!");
                    return;
                }
                addParticipantExceptionLabel.setText("");
                services.addParticipant(selectedTeam, selectedRally, participantName);
            } catch (ExceptionBaseClass exception) {
                showExceptionMessageBox(exception);
            }
        }
    }


    public void setServices(RallyApplicationServices services) {
        this.services = services;
    }

    @Override
    public void updateTeamWasAdded(Team addedTeam) {
        Platform.runLater(() -> teamObservableList.add(addedTeam));
    }

    @Override
    public void updateParticipantWasAdded(Participant addedParticipant) {
        Platform.runLater(() -> {
            Team selectedTeamInTableView = teamNamesChoiceBox.getSelectionModel().getSelectedItem();
                if (selectedTeamInTableView != null && selectedTeamInTableView.getTeamName().equals(addedParticipant.getTeam().getTeamName()))
            foundMembersOfTeamObservableList.add(addedParticipant);

            Rally rally = addedParticipant.getRally();
            int index = rallyObservableList.indexOf(rally);
            rallyObservableList.set(index, rally);

            ralliesTableView.refresh();
        });
    }

    @Override
    public void updateRallyWasAdded(Rally addedRally) {

        Platform.runLater(() -> {
            if (!rallyObservableList.contains(addedRally))
                rallyObservableList.add(addedRally);
        });
    }
}

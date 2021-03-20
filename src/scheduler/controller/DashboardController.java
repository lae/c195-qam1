package scheduler.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private ToggleButton allToggleButton, monthToggleButton, weekToggleButton;
    @FXML
    private Label appointmentMessageLabel;
    @FXML
    private ToggleGroup selectAppointmentFilter;

    /**
     * Exits the application.
     *
     * @param actionEvent an action a user performs.
     */
    @FXML
    public void onActionQuit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Attempts a login.
     *
     * @param actionEvent an action a user performs.
     */
    @FXML
    public void onActionLogin(ActionEvent actionEvent) {
        // not applicable
    }

    /**
     * Initializes the Dashboard scheduler.controller.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // WIP
    }

    public void onActionModifyAppt(ActionEvent actionEvent) {
    }

    public void onActionDeleteAppt(ActionEvent actionEvent) {
    }

    public void onActionScheduleAppt(ActionEvent actionEvent) {
    }

    /**
     * Updates appointments list to filter appointments by selected filter.
     *
     * @param actionEvent A user input event.
     */
    public void onActionChangeFilter(ActionEvent actionEvent) {
        if (monthToggleButton.isSelected()) {
            // select month
        } else if (weekToggleButton.isSelected()) {
            // select week
        } else {
            //select all
        }
    }
}

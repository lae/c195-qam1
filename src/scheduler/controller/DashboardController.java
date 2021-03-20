package scheduler.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import scheduler.model.Appointment;
import scheduler.model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private ToggleButton allToggleButton, monthToggleButton, weekToggleButton;
    @FXML
    private Label appointmentMessageLabel, appointmentTablePlaceholder, customerTablePlaceholder;
    @FXML
    private ToggleGroup selectAppointmentFilter;
    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIDCol, appointmentCustomerIDCol;
    @FXML
    private TableColumn<Appointment, String> appointmentTitleCol, appointmentDescriptionCol, appointmentLocationCol;
    //@FXML
    //private TableColumn<Appointment, Contact>;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIDCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol, customerAddressCol, customerPhoneNumberCol;

    @FXML
    public void onActionModifyAppt(ActionEvent actionEvent) {
    }

    @FXML
    public void onActionDeleteAppt(ActionEvent actionEvent) {
    }

    @FXML
    public void onActionScheduleAppt(ActionEvent actionEvent) {
    }

    /**
     * Updates appointments list to filter appointments by selected filter.
     *
     * @param actionEvent A user input event.
     */
    @FXML
    public void onActionChangeFilter(ActionEvent actionEvent) {
        if (monthToggleButton.isSelected()) {
            // select month
        } else if (weekToggleButton.isSelected()) {
            // select week
        } else {
            //select all
        }
    }

    @FXML
    public void onActionModifyCustomer(ActionEvent actionEvent) {
    }

    @FXML
    public void onActionDeleteCustomer(ActionEvent actionEvent) {
    }

    @FXML
    public void onActionNewCustomer(ActionEvent actionEvent) {
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
}

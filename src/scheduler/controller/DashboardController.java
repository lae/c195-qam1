package scheduler.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.DAO;
import scheduler.model.Appointment;
import scheduler.model.Customer;
import scheduler.util.FXUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {
    private static DAO<Appointment> appointmentDAO;
    private static DAO<Customer> customerDAO;
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
    private TableColumn<Appointment, String> appointmentTitleCol, appointmentDescriptionCol, appointmentLocationCol, appointmentContactCol, appointmentTypeCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartCol, appointmentEndCol;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIDCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol, customerAddressCol, customerPhoneNumberCol;

    /**
     * Refreshes the Appointments TableView.
     */
    private void refreshAppointmentTable() {
        appointmentTableView.setItems(appointmentDAO.listAll());
    }

    /**
     * Refreshes the Customers TableView.
     */
    private void refreshCustomerTable() {
        customerTableView.setItems(customerDAO.listAll());
    }

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
     * Updates appointments list to filter appointments when one of the toggle buttons are pressed.
     * Uses lambda expressions to filter appointments by current week or month.
     *
     * @param actionEvent A user input event.
     */
    @FXML
    public void onActionChangeFilter(ActionEvent actionEvent) {
        if (monthToggleButton.isSelected()) {
            // Updates the appointment table with the current month's appointments.
            appointmentTableView.setItems(appointmentDAO.listAll().stream()
                    .filter(a -> a.getStart().getMonth() == LocalDate.now().getMonth())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        } else if (weekToggleButton.isSelected()) {
            // Updates the appointment table with the current week's appointments.
            LocalDateTime weekStart = LocalDate.now().with(WeekFields.ISO.getFirstDayOfWeek()).atStartOfDay();
            LocalDateTime weekEnd = weekStart.plusWeeks(1);
            appointmentTableView.setItems(appointmentDAO.listAll().stream()
                    .filter(a -> a.getStart().isAfter(weekStart) && a.getStart().isBefore(weekEnd))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        } else {
            // Updates the appointment table with all appointments.
            refreshAppointmentTable();
        }
    }

    @FXML
    public void onActionModifyCustomer(ActionEvent actionEvent) throws IOException {
        if (customerTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a product to modify!");
            FXUtil.displayAlert(alert);
            return;
        }
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        Stage editStage = new Stage();
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(FXUtil.getStage(actionEvent));
        editStage.setTitle("Scheduler::Update Customer");
        FXMLLoader loader = FXUtil.loadView(editStage, "EditCustomer.fxml");
        EditCustomerController editCtrl = loader.getController();
        editCtrl.startEdit(selectedCustomer);
        editStage.showAndWait();
        refreshCustomerTable();
    }

    @FXML
    public void onActionDeleteCustomer(ActionEvent actionEvent) {
        if (customerTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a product to delete!");
            FXUtil.displayAlert(alert);
            return;
        }

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the following customer?\n\n" + selectedCustomer.getName());
        Optional<ButtonType> result = FXUtil.displayAlert(alert);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            customerDAO.delete(selectedCustomer);
            refreshCustomerTable();
        }
    }

    /**
     * Switches to an Add Customer form.
     *
     * @param actionEvent an action a user performs.
     * @throws IOException an I/O error.
     */
    @FXML
    public void onActionNewCustomer(ActionEvent actionEvent) throws IOException {
        Stage editStage = new Stage();
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(FXUtil.getStage(actionEvent));
        editStage.setTitle("Scheduler::New Customer");
        FXMLLoader loader = FXUtil.loadView(editStage, "EditCustomer.fxml");
        EditCustomerController editCtrl = loader.getController();
        editCtrl.startAdd();
        editStage.showAndWait();
        refreshCustomerTable();
    }

    /**
     * Initializes the Dashboard scheduler.controller.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerDAO = new CustomerDao();
        appointmentDAO = new AppointmentDao();

        // Associate the appointments tableview columns with the appropriate getters on Appointment
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        appointmentCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        refreshAppointmentTable();

        // Associate the customer tableview columns with the appropriate getters on Customer
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        refreshCustomerTable();
    }
}
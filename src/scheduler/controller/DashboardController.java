package scheduler.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    private ToggleButton monthToggleButton, weekToggleButton;
    @FXML
    private Label appointmentMessageLabel, customerMessageLabel, appointmentTablePlaceholder, customerTablePlaceholder;
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

    /**
     * Creates a new stage/modal to edit an appointment.
     *
     * @param event A user input event.
     * @throws IOException さあ
     */
    private void modifyAppointment(Event event) throws IOException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        Stage editStage = new Stage();
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(FXUtil.getStage(event));
        editStage.setTitle("Scheduler::Update Appointment");
        FXMLLoader loader = FXUtil.loadView(editStage, "EditAppointment.fxml");
        EditAppointmentController editCtrl = loader.getController();
        editCtrl.startEdit(selectedAppointment);
        editStage.showAndWait();
        refreshAppointmentTable();
    }

    /**
     * Opens an Update Appointment window when button is clicked.
     *
     * @param actionEvent A user input event.
     * @throws IOException さあ
     */
    @FXML
    public void onActionModifyAppointment(ActionEvent actionEvent) throws IOException {
        if (appointmentTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select an appointment to modify!");
            FXUtil.displayAlert(alert);
            return;
        }
        modifyAppointment(actionEvent);
    }

    /**
     * Opens an Update Appointment window when the TableView is double-clicked.
     *
     * @param mouseEvent mouse events performed by the user.
     * @throws IOException さあ
     */
    @FXML
    public void onClickModifyAppointment(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2 && !appointmentTableView.getSelectionModel().isEmpty()) {
            modifyAppointment(mouseEvent);
        }
    }

    /**
     * Prompts user to delete an Appointment and deletes on confirmation.
     */
    @FXML
    public void onActionDeleteAppointment() {
        if (appointmentTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select an appointment to delete!");
            FXUtil.displayAlert(alert);
            return;
        }
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the following appointment?\n\n" +
                selectedAppointment.getTitle() + " at " + selectedAppointment.getStart());
        Optional<ButtonType> result = FXUtil.displayAlert(alert);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appointmentDAO.delete(selectedAppointment);
            refreshAppointmentTable();
        }
    }

    /**
     * Switches to a New Appointment form.
     *
     * @param actionEvent an action a user performs.
     * @throws IOException an I/O error.
     */
    @FXML
    public void onActionScheduleAppointment(ActionEvent actionEvent) throws IOException {
        Stage editStage = new Stage();
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(FXUtil.getStage(actionEvent));
        editStage.setTitle("Scheduler::New Appointment");
        FXMLLoader loader = FXUtil.loadView(editStage, "EditAppointment.fxml");
        EditAppointmentController editCtrl = loader.getController();
        editCtrl.startAdd();
        editStage.showAndWait();
        refreshAppointmentTable();
    }

    /**
     * Updates appointments list to filter appointments when one of the toggle buttons are pressed.
     * Uses lambda expressions to filter appointments by current week or month.
     */
    @FXML
    public void onActionChangeFilter() {
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

    /**
     * Creates a new stage/modal to edit a customer.
     *
     * @param event A user input event.
     * @throws IOException さあ
     */
    private void modifyCustomer(Event event) throws IOException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        Stage editStage = new Stage();
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(FXUtil.getStage(event));
        editStage.setTitle("Scheduler::Update Customer");
        FXMLLoader loader = FXUtil.loadView(editStage, "EditCustomer.fxml");
        EditCustomerController editCtrl = loader.getController();
        editCtrl.startEdit(selectedCustomer);
        editStage.showAndWait();
        refreshCustomerTable();
    }

    /**
     * Opens an Update Customer window when button is clicked.
     *
     * @param actionEvent A user input event.
     * @throws IOException さあ
     */
    @FXML
    public void onActionModifyCustomer(ActionEvent actionEvent) throws IOException {
        if (customerTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a customer to modify!");
            FXUtil.displayAlert(alert);
            return;
        }
        modifyCustomer(actionEvent);
    }

    /**
     * Opens an Update Customer window when the TableView is double-clicked.
     *
     * @param mouseEvent mouse events performed by the user.
     * @throws IOException さあ
     */
    @FXML
    public void onClickModifyCustomer(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2 && !customerTableView.getSelectionModel().isEmpty()) {
            modifyCustomer(mouseEvent);
        }
    }

    /**
     * Prompts user to delete a Customer and deletes on confirmation.
     */
    @FXML
    public void onActionDeleteCustomer() {
        if (customerTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a customer to delete!");
            FXUtil.displayAlert(alert);
            return;
        }

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        String deleteMessage = "Are you sure you want to delete the customer, " + selectedCustomer.getName() + "?";
        ObservableList<Appointment> appointments = ((AppointmentDao) appointmentDAO).filterByCustomerID(selectedCustomer.getID());
        if (appointments.size() > 0) {
            deleteMessage += "\nThis procedure will also remove their " + appointments.size() + " appointment(s).";
        }

        Alert alert = FXUtil.detailedAlert(Alert.AlertType.CONFIRMATION, "", deleteMessage);
        Optional<ButtonType> result = FXUtil.displayAlert(alert);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Appointment a : appointments) {
                appointmentDAO.delete(a);
            }
            customerDAO.delete(selectedCustomer);
            customerMessageLabel.setText(String.format("Customer %s has been removed.", selectedCustomer.getName()));
            refreshCustomerTable();
            refreshAppointmentTable();
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
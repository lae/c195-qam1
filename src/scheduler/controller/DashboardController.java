package scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.dao.CustomerDao;
import scheduler.dao.DAO;
import scheduler.model.Appointment;
import scheduler.model.Customer;
import scheduler.util.FXUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
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
    private TableColumn<Appointment, String> appointmentTitleCol, appointmentDescriptionCol, appointmentLocationCol;
    //@FXML
    //private TableColumn<Appointment, Contact>;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIDCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol, customerAddressCol, customerPhoneNumberCol;

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

        // Associate the customer tableview columns with the appropriate getters on Customer
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        refreshCustomerTable();
    }
}
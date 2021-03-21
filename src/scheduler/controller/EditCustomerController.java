package scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import scheduler.model.Country;
import scheduler.model.Customer;
import scheduler.model.FirstLevelDivision;
import scheduler.util.FXUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for adding and modifying Customers.
 *
 * @author Musee Ullah
 */
public class EditCustomerController implements Initializable {
    private static final String newIdText = "Automatically Generated";
    private Stage stage;
    private int customerIndex = -1;
    @FXML
    private Label labelViewTitle;
    @FXML
    private TextField inputId, inputName, inputAddress, inputPostalCode, inputPhoneNumber;
    @FXML
    private ComboBox<Country> inputCountry;
    @FXML
    private ComboBox<FirstLevelDivision> inputDivision;

    /**
     * Updates the view to that of an Add Customer form.
     */
    public void startAdd() {
        labelViewTitle.setText("Add Customer");
        inputId.setText(newIdText);
    }

    /**
     * Updates the view to that of an Edit Customer form.
     * This populates fields with information from an existing customer.
     *
     * @param selectedIndex    the index of the Customer to edit.
     * @param selectedCustomer the Customer to edit.
     */
    public void startEdit(int selectedIndex, Customer selectedCustomer) {
        labelViewTitle.setText("Modify Customer");
        customerIndex = selectedCustomer.getID();
        inputId.setText(String.valueOf(customerIndex));
        inputName.setText(selectedCustomer.getName());
        inputAddress.setText(String.valueOf(selectedCustomer.getAddress()));
        inputPostalCode.setText(String.valueOf(selectedCustomer.getPostalCode()));
        inputPhoneNumber.setText(String.valueOf(selectedCustomer.getPhone()));
    }

    public void onActionSaveCustomer(ActionEvent actionEvent) {
    }

    /**
     * Cancels changes to return to the Dashboard.
     * This checks with the user whether or not they want to discard their changes and go back.
     *
     * @param actionEvent A user input event.
     * @throws IOException さあ
     */
    @FXML
    public void onActionCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will be discarded. Is this OK?");
        Optional<ButtonType> result = FXUtil.displayAlert(alert);
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        stage = FXUtil.getStage(actionEvent);
        FXUtil.loadView(stage, "Dashboard.fxml");
    }

    @FXML
    public void onActionSelectCountry(ActionEvent actionEvent) {
    }

    /**
     * Initializes the Customer scheduler.controller.
     * Initializes the TableViews with initial contents and TableColumn/scheduler.model associations.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

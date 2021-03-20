package scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCustomerController implements Initializable {
    @FXML
    private TextField inputId, inputName, inputAddress, inputPostalCode, inputPhoneNumber;
    @FXML
    private ComboBox inputCountry, inputDivision;
    @FXML
    private Label labelViewTitle;

    public void onActionSaveCustomer(ActionEvent actionEvent) {
    }

    public void onActionCancel(ActionEvent actionEvent) {
    }

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

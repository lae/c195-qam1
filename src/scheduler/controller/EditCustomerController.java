package scheduler.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import scheduler.State;
import scheduler.dao.CountryDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.DAO;
import scheduler.dao.FirstLevelDivisionDao;
import scheduler.model.Country;
import scheduler.model.Customer;
import scheduler.model.FirstLevelDivision;
import scheduler.util.FXUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for adding and modifying Customers.
 *
 * @author Musee Ullah
 */
public class EditCustomerController implements Initializable {
    private static final String newIdText = "Automatically Generated";
    private static DAO<Customer> customerDAO;
    private static CountryDao countryDAO;
    private static DAO<FirstLevelDivision> firstLevelDivisionDAO;
    private Customer customer;
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
        customer = new Customer();
    }

    /**
     * Populates the inputDivision ComboBox with first level divisions associated with the specified country ID.
     *
     * @param countryID the country ID to filter divisions by.
     */
    public void updateFLDs(int countryID) {
        inputDivision.setItems(
                firstLevelDivisionDAO.listAll().stream()
                        .filter(f -> f.getCountryID() == countryID)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );
    }

    /**
     * Updates the view to that of an Edit Customer form.
     * This populates fields with information from an existing customer.
     *
     * @param selectedCustomer the Customer to edit.
     */
    public void startEdit(Customer selectedCustomer) {
        customer = selectedCustomer;

        labelViewTitle.setText("Modify Customer");
        inputId.setText(String.valueOf(customer.getID()));
        inputName.setText(customer.getName());
        inputAddress.setText(customer.getAddress());
        inputPostalCode.setText(customer.getPostalCode());
        inputPhoneNumber.setText(customer.getPhone());
        FirstLevelDivision f = firstLevelDivisionDAO.find(new FirstLevelDivision(customer.getDivisionID()));
        Country c = countryDAO.find(new Country(f.getCountryID()));
        inputCountry.getSelectionModel().select(c);
        updateFLDs(c.getID());
        inputDivision.getSelectionModel().select(f);
    }

    /**
     * Saves changes from the form by either creating a new customer or updating one if editing.
     *
     * @param actionEvent a user input event.
     */
    public void onActionSaveCustomer(ActionEvent actionEvent) {
        ArrayList<String> validationErrors = new ArrayList<>();
        if (inputName.getText().isEmpty()) {
            validationErrors.add("Customer name must not be empty.");
        }
        if (inputDivision.getSelectionModel().isEmpty()) {
            validationErrors.add("Country and State/Province must be selected.");
        }
        // Inform the user if any validation failed, and return to the edit screen if so.
        if (validationErrors.size() > 0) {
            Alert alert = FXUtil.detailedAlert(Alert.AlertType.ERROR, "One or more fields failed to validate. Please " +
                    "review the following and make the necessary corrections.", String.join("\n", validationErrors));
            FXUtil.displayAlert(alert);
            return;
        }
        customer.setName(inputName.getText());
        customer.setAddress(inputAddress.getText());
        customer.setPostalCode(inputPostalCode.getText());
        customer.setPhone(inputPhoneNumber.getText());
        customer.setDivisionID(inputDivision.getSelectionModel().getSelectedItem().getID());
        customer.setLastUpdatedBy(State.getLoggedInUser().getUsername());

        if (customer.getID() > 0) {
            customerDAO.update(customer);
        } else {
            customer.setCreatedBy(State.getLoggedInUser().getUsername());
            customerDAO.add(customer);
        }

        FXUtil.getStage(actionEvent).close();
    }

    /**
     * Cancels changes to return to the Dashboard.
     * This checks with the user whether or not they want to discard their changes and go back.
     *
     * @param actionEvent A user input event.
     */
    @FXML
    public void onActionCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will be discarded. Is this OK?");
        Optional<ButtonType> result = FXUtil.displayAlert(alert);
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        FXUtil.getStage(actionEvent).close();
    }

    /**
     * Updates the list of states/provinces when user selects a country.
     *
     * @param actionEvent a user input event.
     */
    @FXML
    public void onActionSelectCountry(ActionEvent actionEvent) {
        Country selectedCountry = inputCountry.getSelectionModel().getSelectedItem();
        updateFLDs(selectedCountry.getID());
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
        inputCountry.setConverter(new StringConverter<>() {
            @Override
            public String toString(Country country) {
                return country != null ? country.getName() : "";
            }

            @Override
            public Country fromString(String s) {
                return null;
            }
        });
        inputDivision.setConverter(new StringConverter<>() {
            @Override
            public String toString(FirstLevelDivision f) {
                return f != null ? f.getDivision() : "";
            }

            @Override
            public FirstLevelDivision fromString(String s) {
                return null;
            }
        });

        customerDAO = new CustomerDao();
        countryDAO = new CountryDao();
        firstLevelDivisionDAO = new FirstLevelDivisionDao();

        inputCountry.setItems(countryDAO.listUsable());
    }
}

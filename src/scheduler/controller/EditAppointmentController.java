package scheduler.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import scheduler.State;
import scheduler.dao.*;
import scheduler.model.Appointment;
import scheduler.model.Contact;
import scheduler.model.Customer;
import scheduler.model.User;
import scheduler.util.FXUtil;
import scheduler.util.TimeUtil;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for scheduling and modifying Appointments.
 *
 * @author Musee Ullah
 */
public class EditAppointmentController implements Initializable {
    private static final String newIdText = "Automatically Generated";
    private static DAO<Appointment> appointmentDAO;
    private static DAO<Customer> customerDAO;
    private static DAO<User> userDAO;
    private static DAO<Contact> contactDAO;
    private Appointment appointment;
    @FXML
    private TextField inputId, inputTitle, inputLocation, inputType;
    @FXML
    private ComboBox<LocalTime> inputStart, inputEnd;
    @FXML
    private ComboBox<Customer> inputCustomer;
    @FXML
    private ComboBox<User> inputUser;
    @FXML
    private ComboBox<Contact> inputContact;
    @FXML
    private DatePicker inputDate;
    @FXML
    private TextArea inputDescription;
    @FXML
    private Label labelViewTitle;

    /**
     * Updates the view to that of an Schedule New Appointment form.
     */
    public void startAdd() {
        labelViewTitle.setText("Schedule New Appointment");
        inputId.setText(newIdText);
        inputUser.getSelectionModel().select(State.getLoggedInUser());
        appointment = new Appointment();
    }

    /**
     * Updates the view to that of an Update Appointment form.
     * This populates fields with information from an existing appointment.
     *
     * @param selectedAppointment the Appointment to edit.
     */
    public void startEdit(Appointment selectedAppointment) {
        appointment = selectedAppointment;

        labelViewTitle.setText("Update Appointment");
        inputId.setText(String.valueOf(appointment.getID()));
        inputTitle.setText(appointment.getTitle());
        inputLocation.setText(appointment.getLocation());
        inputType.setText(appointment.getType());
        inputDescription.setText(appointment.getDescription());

        // looks up the foreign keys in the appointment and selects the associated Customer/User/Contact
        Customer editCustomer = customerDAO.find(new Customer(appointment.getCustomerID()));
        inputCustomer.getSelectionModel().select(editCustomer);
        User editUser = userDAO.find(new User(appointment.getUserID()));
        inputUser.getSelectionModel().select(editUser);
        Contact editContact = contactDAO.find(new Contact(appointment.getContactID()));
        inputContact.getSelectionModel().select(editContact);

        // breaks up the date value of the start/end time for editing
        inputDate.setValue(appointment.getStart().toLocalDate());
        inputStart.getSelectionModel().select(appointment.getStart().toLocalTime());
        inputEnd.getSelectionModel().select(appointment.getEnd().toLocalTime());
    }

    /**
     * Saves changes from the form by either creating a new appointment or updating one if editing.
     *
     * @param actionEvent a user input event.
     */
    public void onActionSaveAppointment(ActionEvent actionEvent) {
        ArrayList<String> validationErrors = new ArrayList<>();
        Appointment changes = new Appointment(appointment);

        // Parse and validate the form data, storing validation errors in an array.
        if (inputDate.getValue() == null || inputStart.getSelectionModel().isEmpty() || inputEnd.getSelectionModel().isEmpty()) {
            validationErrors.add("A date, start and end time must all be selected.");
        } else if (inputStart.getSelectionModel().getSelectedIndex() > inputEnd.getSelectionModel().getSelectedIndex()) {
            validationErrors.add("Selected end time must be after the selected start time.");
        } else {
            changes.setStart(LocalDateTime.of(inputDate.getValue(), inputStart.getValue()));
            changes.setEnd(LocalDateTime.of(inputDate.getValue(), inputEnd.getValue()));
            /*
             * Since the start/end ComboBoxes are sorted in a way that the times are all consecutive from each (there's no
             * multiple hour gap when scrolling), an end time that technically falls before the start time *may* be selected
             * if they are in a significantly different timezone, and it should be treated as the next day, locally.
             */
            if (inputEnd.getValue().isBefore(inputStart.getValue())) {
                changes.setEnd(changes.getEnd().plusDays(1));
            }
        }

        if (inputCustomer.getSelectionModel().getSelectedItem() == null) {
            validationErrors.add("A customer must be selected.");
        } else if (validationErrors.size() == 0) {
            // Only continue with this validation if we haven't run into other validation errors.
            changes.setCustomerID(inputCustomer.getSelectionModel().getSelectedItem().getID());
            // Check if specified time conflicts with another of this customer's appointments.
            Appointment conflict = null;
            for (Appointment a : ((AppointmentDao) appointmentDAO).filterByCustomerID(changes.getCustomerID())) {
                if (changes.getStart().isBefore(a.getEnd())
                        && changes.getEnd().isAfter(a.getStart())
                        && changes.getID() != a.getID()) {
                    conflict = a;
                }
            }
            if (conflict != null) {
                validationErrors.add(String.format("The specified appointment time, %s, conflicts with another one of " +
                                "this customer's appointments, \"%s\", at %s.",
                        changes.getStart().toLocalDate() + " between " + changes.getStart().toLocalTime()
                                + "-" + changes.getEnd().toLocalTime(),
                        conflict.getTitle(),
                        conflict.getStart().toLocalDate() + " between " + conflict.getStart().toLocalTime()
                                + "-" + conflict.getEnd().toLocalTime()));
            }
        }

        changes.setTitle(inputTitle.getText());
        if (inputUser.getSelectionModel().getSelectedItem() != null) {
            changes.setUserID(inputUser.getSelectionModel().getSelectedItem().getID());
        }
        if (inputContact.getSelectionModel().getSelectedItem() != null) {
            changes.setContactID(inputContact.getSelectionModel().getSelectedItem().getID());
        }
        changes.setLocation(inputLocation.getText());
        changes.setType(inputType.getText());
        changes.setDescription(inputDescription.getText());
        changes.setLastUpdatedBy(State.getLoggedInUser().getUsername());

        // Inform the user if any validation failed, and return to the edit screen if so.
        if (validationErrors.size() > 0) {
            Alert alert = FXUtil.detailedAlert(Alert.AlertType.ERROR, "One or more fields failed to validate. Please " +
                    "review the following and make the necessary corrections.", String.join("\n", validationErrors));
            FXUtil.displayAlert(alert);
            return;
        }

        if (changes.getID() > 0) {
            appointmentDAO.update(changes);
        } else {
            changes.setCreatedBy(State.getLoggedInUser().getUsername());
            appointmentDAO.add(changes);
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
     * Initializes the EditAppointment controller.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputCustomer.setConverter(new StringConverter<>() {
            @Override
            public String toString(Customer customer) {
                return customer != null ? customer.getID() + ": " + customer.getName() : "";
            }

            @Override
            public Customer fromString(String s) {
                return null;
            }
        });
        inputUser.setConverter(new StringConverter<>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getUsername() : "";
            }

            @Override
            public User fromString(String s) {
                return null;
            }
        });
        inputContact.setConverter(new StringConverter<>() {
            @Override
            public String toString(Contact contact) {
                return contact != null ? contact.getName() : "";
            }

            @Override
            public Contact fromString(String s) {
                return null;
            }
        });

        appointmentDAO = new AppointmentDao();
        customerDAO = new CustomerDao();
        userDAO = new UserDao();
        contactDAO = new ContactDao();

        inputCustomer.setItems(customerDAO.listAll());
        inputUser.setItems(userDAO.listAll());
        inputContact.setItems(contactDAO.listAll());
        inputDate.setValue(LocalDate.now());
        List<ObservableList<LocalTime>> businessHours = TimeUtil.generateBusinessHours();
        inputStart.setItems(businessHours.get(0));
        inputEnd.setItems(businessHours.get(1));
    }
}

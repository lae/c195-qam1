package scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditAppointmentController {
    @FXML
    private TextField inputId, inputTitle, inputLocation, inputType;
    @FXML
    private ComboBox inputStart, inputEnd, inputCustomer, inputUser, inputContact;
    @FXML
    private DatePicker inputDate;
    @FXML
    private TextArea inputDescription;
    @FXML
    private Label labelViewTitle;

    public void onActionSaveAppointment(ActionEvent actionEvent) {
    }

    public void onActionCancel(ActionEvent actionEvent) {
    }
}

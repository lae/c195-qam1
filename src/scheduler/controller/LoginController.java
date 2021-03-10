package scheduler.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import scheduler.exceptions.MissingFieldsException;
import scheduler.model.User;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("i18n/Login", Locale.getDefault());
    @FXML
    private Label loginLabel, loginMessage, usernameLabel, passwordLabel, locationLabel;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Button quitButton, loginButton;

    /**
     * Initializes all of the labels in the login form with localized strings.
     */
    private void localizeLabels() {
        loginLabel.setText(rb.getString("login"));
        loginMessage.setText(rb.getString("message"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        quitButton.setText(rb.getString("quit"));

        String zone = ZoneId.systemDefault().toString();
        locationLabel.setText(MessageFormat.format(rb.getString("location"), zone));
    }

    private void attemptLogin(String username, String password) {
        try {
            User user = new User(username, password);
        } catch (MissingFieldsException e) {
            loginMessage.setText(rb.getString("login_failed"));
        }
    }

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
        attemptLogin(usernameInput.getText(), passwordInput.getText());
    }

    /**
     * Checks if Enter key was pressed and then attempt login.
     *
     * @param keyEvent a key
     */
    @FXML
    public void onKeyLogin(KeyEvent keyEvent) {
        KeyCode key = keyEvent.getCode();
        if (key == KeyCode.ENTER) {
            loginMessage.setText("You clicked Enter!");
        }
    }

    /**
     * Initializes the Login scheduler.controller.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        localizeLabels();
    }
}

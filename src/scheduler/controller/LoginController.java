package scheduler.controller;

import com.sun.javafx.css.StyleCacheEntry;
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
import scheduler.dao.DAO;
import scheduler.dao.UserDao;
import scheduler.exceptions.AuthenticationException;
import scheduler.exceptions.LookupException;
import scheduler.exceptions.MissingFieldsException;
import scheduler.model.User;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static scheduler.Main.loadView;

public class LoginController implements Initializable {
    private static DAO<User> userDao;
    private Stage stage;
    private ResourceBundle rb = ResourceBundle.getBundle("i18n/Login", Locale.getDefault());
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

    /**
     * Validates a login attempt
     *
     * @param username the login username provided by the end-user.
     * @param password the login password provided by the end-user.
     */
    private boolean attemptLogin(String username, String password) {
        try {
            User user = new User(username, password);
            User userLookup = userDao.find(user);
            if (userLookup.getID() == 0) {
                throw new LookupException("User does not exist.");
            } else if (!user.getPassword().contentEquals(userLookup.getPassword())) {
                throw new AuthenticationException("Credentials failed to verify.");
            }
            return true;
        } catch (MissingFieldsException e) {
            loginMessage.setText(rb.getString("login_failed"));
        } catch (LookupException e) {
            loginMessage.setText("Couldn't find user.");
        } catch (AuthenticationException e) {
            loginMessage.setText("Login credentials incorrect.");
        }
        return false;
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
    public void onActionLogin(ActionEvent actionEvent) throws IOException {
        if (attemptLogin(usernameInput.getText(), passwordInput.getText())) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            loadView(stage, "/scheduler/view/Dashboard.fxml");
        }
    }

    /**
     * Checks if Enter key was pressed and then attempt login.
     *
     * @param keyEvent a key
     */
    @FXML
    public void onKeyLogin(KeyEvent keyEvent) throws IOException {
        KeyCode key = keyEvent.getCode();
        if (key == KeyCode.ENTER) {
            if (attemptLogin(usernameInput.getText(), passwordInput.getText())) {
                stage = (Stage) ((TextField) keyEvent.getSource()).getScene().getWindow();
                loadView(stage, "/scheduler/view/Dashboard.fxml");
            }
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
        userDao = new UserDao();
    }
}
